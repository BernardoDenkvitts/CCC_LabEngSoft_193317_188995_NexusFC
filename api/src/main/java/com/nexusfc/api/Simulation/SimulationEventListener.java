package com.nexusfc.api.Simulation;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.nexusfc.api.AI.GeminiService;
import com.nexusfc.api.Model.ProfessionalPlayer;
import com.nexusfc.api.Model.Simulation;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.Enum.Lane;
import com.nexusfc.api.Model.Enum.SimulationStatus;
import com.nexusfc.api.Notification.NotificationService;
import com.nexusfc.api.Notification.SimulationResultNotificationDTO;
import com.nexusfc.api.Repository.SimulationsRepository;
import com.nexusfc.api.User.Service.UserService;

@Component
public class SimulationEventListener {

    private final GeminiService geminiService;
    private final NotificationService notificationService;
    private final SimulationsRepository simulationsRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(SimulationEventListener.class);

    public SimulationEventListener(GeminiService geminiService, NotificationService notificationService,
            SimulationsRepository simulationsRepository, UserService userService) {
        this.geminiService = geminiService;
        this.notificationService = notificationService;
        this.simulationsRepository = simulationsRepository;
        this.userService = userService;
    }

    @Async("eventTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, condition = "#event.simulation.versusPlayer")
    public void onAcceptedVersusPlayer(SimulationAcceptedEvent event) {
        logger.info("Received new Simulation PVP: {} vs {}", event.simulation().getDesafiante().getId(),
                event.simulation().getDesafiadoId());
        handleSimulation(event.simulation());
    }

    @Async("eventTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, condition = "!#event.simulation.versusPlayer")
    public void onAcceptedVersusTeam(SimulationAcceptedEvent event) {
        logger.info("Received new Simulation PVE: {} vs {}", event.simulation().getDesafiante().getId(),
                event.simulation().getDesafiadoId());
        handleSimulation(event.simulation());
    }

    private void handleSimulation(Simulation simulation) {
        User challenger = simulation.getDesafiante();
        User challenged = simulation.getVersusPlayer()
                ? userService.find(simulation.getDesafiadoId())
                : null;

        var team1 = simulation.getDesafianteTeamPlayers().stream().map(NecessaryStats::from).toList();
        var team2 = simulation.getDesafiadoTeamPlayers().stream().map(NecessaryStats::from).toList();

        int result = Integer.parseInt(geminiService.request(buildPrompt(team1, team2)).trim());

        logger.info("Resultado do GEMINI: {}", result);

        processSimulationResult(result, simulation, challenger, challenged);

        simulation.setStatus(SimulationStatus.COMPLETED);
        userService.save(challenger);
        if (simulation.getVersusPlayer())
            userService.save(challenged);

        simulationsRepository.save(simulation);
    }

    private String buildPrompt(List<NecessaryStats> team1, List<NecessaryStats> team2) {
        String s1 = team1.stream()
                .map(NecessaryStats::format)
                .collect(Collectors.joining("\n"));
        String s2 = team2.stream()
                .map(NecessaryStats::format)
                .collect(Collectors.joining("\n"));

        return """
                You are an oracle of match outcomes. Given two teams with their players and stats, decide which team should win.

                Team 1:
                %s

                Team 2:
                %s

                Respond strictly with 1 or 2 and nothing else. Even if the stats are identical, break ties by choosing “1”.
                """
                .formatted(s1, s2);
    }

    private void processSimulationResult(int result, Simulation simulation, User challenger, User challenged) {
        float betValue = simulation.getBetValue();
        boolean team1Wins = result == 1;

        if (team1Wins) {
            handleChallengerWin(simulation, challenger, challenged, betValue);
        } else {
            handleChallengedWin(simulation, challenger, challenged, betValue);
        }
    }

    private void handleChallengerWin(Simulation simulation, User challenger, User challenged, float betValue) {
        simulation.setWin(true);
        // When simulation is created, bet value is decreased from the challenger user
        challenger.increaseCoins(betValue * 2);
        if (simulation.getVersusPlayer()) {
            challenged.decreaseCoins(betValue);
            sendNotifications(challenged.getId(), simulation, true);
        }
    }

    private void handleChallengedWin(Simulation simulation, User challenger, User challenged, float betValue) {
        simulation.setWin(false);
        if (simulation.getVersusPlayer())
            challenged.increaseCoins(betValue);

        sendNotifications(challenger.getId(), simulation, false);
    }

    private void sendNotifications(String loserId, Simulation simulation, boolean isChallengerWinner) {
        String winMessage = "Você venceu a simulação contra o usuário ";
        String loseMessage = "Você perdeu a simulação contra o usuário ";

        String opponentId = simulation.getDesafiadoId();

        String msgToChallenger = (isChallengerWinner ? winMessage : loseMessage) + opponentId;

        notificationService.notifyUser(simulation.getDesafiante().getId(),
                SimulationResultNotificationDTO.create(msgToChallenger, simulation));

        if (simulation.getVersusPlayer()) {
            String msgToChallenged = (isChallengerWinner ? loseMessage : winMessage)
                    + simulation.getDesafiante().getId();
            notificationService.notifyUser(loserId,
                    SimulationResultNotificationDTO.create(msgToChallenged, simulation));
        }
    }

    private static record NecessaryStats(
            String nick,
            Lane lane,
            float kills,
            float deaths,
            float assists,
            float damage,
            float gold,
            float cs,
            float winRate,
            float cost) {
        static NecessaryStats from(ProfessionalPlayer p) {
            return new NecessaryStats(
                    p.getNick(),
                    p.getLane(),
                    p.getOverallKill(),
                    p.getOverallDeath(),
                    p.getOverallAssist(),
                    p.getOverallDamage(),
                    p.getOverallGold(),
                    p.getOverallCs(),
                    p.getOverallWinRate(),
                    p.getCost());
        }

        String format() {
            return String.format(
                    "Nick=%s, Lane=%s, K/D/A=%.0f/%.0f/%.0f, Overall Dmg=%.0f, Overall Gold=%.0f, Overall cs=%.0f, Overall WinRate=%.2f, cost=%.2f",
                    nick, lane, kills, deaths, assists, damage, gold, cs, winRate, cost);
        }
    }
}
