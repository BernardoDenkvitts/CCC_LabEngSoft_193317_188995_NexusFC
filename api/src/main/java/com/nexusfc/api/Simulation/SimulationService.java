package com.nexusfc.api.Simulation;

import java.time.Instant;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nexusfc.api.AI.GeminiService;
import com.nexusfc.api.Common.NotFoundException;
import com.nexusfc.api.Market.Exception.InsufficientBalance;
import com.nexusfc.api.Model.ProfessionalPlayer;
import com.nexusfc.api.Model.Simulation;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Model.Enum.SimulationStatus;
import com.nexusfc.api.Notification.NotificationMapper;
import com.nexusfc.api.Notification.NotificationService;
import com.nexusfc.api.Notification.SimulationNotificationDTO;
import com.nexusfc.api.ProfessionalPlayers.ProfessionalPlayersService;
import com.nexusfc.api.ProfessionalTeams.ProfessionalTeamsService;
import com.nexusfc.api.Repository.SimulationsRepository;
import com.nexusfc.api.Simulation.Exception.IncompleteTeamException;
import com.nexusfc.api.Simulation.Exception.InvalidSimulationStateException;
import com.nexusfc.api.Simulation.Exception.UserIsNotInThisSimulationException;
import com.nexusfc.api.User.Service.UserService;
import com.nexusfc.api.User.Service.UserTeamService;

@Service
public class SimulationService {

    private final SimulationsRepository simulationsRepository;
    private final UserService userService;
    private final UserTeamService userTeamService;
    private final ProfessionalTeamsService professionalTeamsService;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher publisher;

    public SimulationService(SimulationsRepository simulationsRepository,
            UserService userService,
            UserTeamService userTeamService,
            ProfessionalTeamsService professionalTeamsService,
            ProfessionalPlayersService professionalPlayersService,
            GeminiService AIService,
            NotificationService notificationService,
            ApplicationEventPublisher publisher) {
        this.simulationsRepository = simulationsRepository;
        this.userService = userService;
        this.userTeamService = userTeamService;
        this.professionalTeamsService = professionalTeamsService;
        this.notificationService = notificationService;
        this.publisher = publisher;
    }

    public Simulation find(String id, String userId) {
        Simulation simulation = simulationsRepository.findById(id).orElseThrow(() -> new NotFoundException("Simulation with id " + id));
        
        if (!simulation.getDesafiante().getId().equals(userId) & !simulation.getDesafiadoId().equals(userId))
            throw new UserIsNotInThisSimulationException();

        return simulation;
    }

    public Page<Simulation> getSimulationHistory(String userId, Pageable pageable) {
        userService.find(userId);

        return simulationsRepository.findSimulationHistoryByUserId(pageable, new ObjectId(userId));
    }

    public Simulation getSimulationById(String id, String userId) {
        userService.find(userId);
        
        return find(id, userId);
    }

    @Transactional
    public Simulation create(String challegerUserId, String challengedId, Boolean versusPlayer, Float betValue) {
        User challenger = validateChallengerAndGetUser(challegerUserId, betValue);
        List<ProfessionalPlayer> challengerTeamPlayers = prepareAndValidateTeam(challenger.getId());

        Simulation simulation = new Simulation(challenger, betValue, challengerTeamPlayers, versusPlayer);

        setupOpponent(simulation, versusPlayer, challengedId);

        userService.save(challenger);
        Simulation newSimulation = simulationsRepository.save(simulation);

        if (versusPlayer)
            notifyChallenge(challenger, challengedId, newSimulation.getCreatedAt());

        return newSimulation;
    }

    @Transactional
    public Simulation accept(String simulationId, String challengedId) {
        User challenged = userService.find(challengedId);
        Simulation simulation = find(simulationId, challengedId);

        if (!simulation.getStatus().equals(SimulationStatus.REQUESTED))
            throw new InvalidSimulationStateException(simulationId, simulation.getStatus());

        if (!challenged.hasEnoughCoins(simulation.getBetValue()))
            throw new InsufficientBalance(challenged.getCoins(), simulation.getBetValue());

        UserTeam challengedTeam = userTeamService.find(challenged.getId());
        if (!challengedTeam.hasCompleteTeam())
            throw new IncompleteTeamException();

        List<ProfessionalPlayer> challengedTeamPlayers = challengedTeam.getStarterPlayers().stream()
                .map(entry -> entry.getPlayer())
                .toList();

        simulation.setDesafiadoTeamPlayers(challengedTeamPlayers);

        startSimulation(simulation);

        return simulationsRepository.save(simulation);
    }

    @Transactional
    public Simulation startPveSimulation(String simulationId, String challengerId) {
        Simulation simulation = find(simulationId, challengerId);
        if (!simulation.getStatus().equals(SimulationStatus.REQUESTED))
            throw new InvalidSimulationStateException(simulationId, simulation.getStatus());

        validateChallenger(simulation, challengerId);
        startSimulation(simulation);

        return simulationsRepository.save(simulation);
    }

    private void startSimulation(Simulation simulation) {
        simulation.setStatus(SimulationStatus.IN_PROGRESS);
        publishSimulationStartEvent(simulation);
    }

    private void notifyChallenge(User challenger, String challengedId, Instant createdAt) {
        UserTeam challengerTeam = userTeamService.find(challenger.getId());
        SimulationNotificationDTO notification = NotificationMapper.toDto(
                challenger,
                challengerTeam,
                createdAt);
        notificationService.notifyUser(challengedId, notification);
    }

    @Transactional
    public Simulation reject(String simulationId, String challengedId) {
        Simulation simulation = find(simulationId, challengedId);
        
        simulation.setStatus(SimulationStatus.DENIED);

        User challenger = simulation.getDesafiante();
        challenger.increaseCoins(simulation.getBetValue());

        simulationsRepository.save(simulation);
        userService.save(challenger);

        return simulation;
    }

    private void publishSimulationStartEvent(Simulation simulation) {
        publisher.publishEvent(new SimulationAcceptedEvent(simulation));
    }

    private void validateChallenger(Simulation simulation, String challengerId) {
        if (!simulation.getDesafiante().getId().equals(challengerId))
            throw new RuntimeException("Only the challenger can start this simulation");
    }

    private List<ProfessionalPlayer> prepareAndValidateTeam(String userId) {
        UserTeam team = userTeamService.find(userId);
        if (!team.hasCompleteTeam())
            throw new IncompleteTeamException();

        return team.getStarterPlayers().stream()
                .map(entry -> entry.getPlayer())
                .toList();
    }

    private void setupOpponent(Simulation simulation, Boolean versusPlayer, String opponentId) {
        if (versusPlayer) {
            userService.find(opponentId);
            simulation.setDesafiado(new ObjectId(opponentId));
        } else {
            List<ProfessionalPlayer> professionalTeamPlayers = professionalTeamsService.getTeamPlayers(opponentId);
            String professionalTeamId = professionalTeamPlayers.getFirst().getTeam().getId();

            simulation.setDesafiado(new ObjectId(professionalTeamId));
            simulation.setDesafiadoTeamPlayers(professionalTeamPlayers);
        }
    }

    private User validateChallengerAndGetUser(String challengerUserId, Float betValue) {
        User challenger = userService.find(challengerUserId);
        if (!challenger.hasEnoughCoins(betValue))
            throw new InsufficientBalance(challenger.getCoins(), betValue);

        challenger.decreaseCoins(betValue);

        return challenger;
    }
}
