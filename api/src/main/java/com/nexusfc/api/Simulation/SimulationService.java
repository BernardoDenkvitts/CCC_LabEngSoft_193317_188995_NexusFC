package com.nexusfc.api.Simulation;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nexusfc.api.AI.GeminiService;
import com.nexusfc.api.Market.Exception.InsufficientBalance;
import com.nexusfc.api.Model.ProfessionalPlayer;
import com.nexusfc.api.Model.Simulation;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Notification.NotificationDTO;
import com.nexusfc.api.Notification.NotificationMapper;
import com.nexusfc.api.Notification.NotificationService;
import com.nexusfc.api.ProfessionalPlayers.ProfessionalPlayersService;
import com.nexusfc.api.ProfessionalTeams.ProfessionalTeamsService;
import com.nexusfc.api.Repository.SimulationsRepository;
import com.nexusfc.api.Simulation.Exception.IncompleteTeamException;
import com.nexusfc.api.User.Service.UserService;
import com.nexusfc.api.User.Service.UserTeamService;

@Service
public class SimulationService {

    private final SimulationsRepository simulationsRepository;
    private final UserService userService;
    private final UserTeamService userTeamService;
    private final ProfessionalTeamsService professionalTeamsService;
    private final NotificationService notificationService;

    public SimulationService(SimulationsRepository simulationsRepository,
            UserService userService,
            UserTeamService userTeamService,
            ProfessionalTeamsService professionalTeamsService,
            ProfessionalPlayersService professionalPlayersService,
            GeminiService AIService,
            NotificationService notificationService) {
        this.simulationsRepository = simulationsRepository;
        this.userService = userService;
        this.userTeamService = userTeamService;
        this.professionalTeamsService = professionalTeamsService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Simulation create(String challegerUserId, String challengedId, Boolean versusPlayer, Float betValue) {

        User challenger = userService.find(challegerUserId);
        if (!challenger.hasEnoughCoins(betValue))
            throw new InsufficientBalance(challenger.getCoins(), betValue);

        challenger.decreaseCoins(betValue);

        UserTeam challengerTeam = userTeamService.find(challenger.getId());
        challengerTeam.getProfessionalPlayers().getFirst().getPlayer();
        if (!challengerTeam.hasCompleteTeam())
            throw new IncompleteTeamException();

        List<ProfessionalPlayer> challengerTeamPlayers = challengerTeam.getStarterPlayers().stream()
                .map(entry -> entry.getPlayer()).toList();

        Simulation simulation = new Simulation(challenger, betValue, challengerTeamPlayers, versusPlayer);

        if (versusPlayer) {
            userService.find(challengedId);
            simulation.setDesafiado(new ObjectId(challengedId));
        } else {
            List<ProfessionalPlayer> professionalTeamPlayers = professionalTeamsService.getTeamPlayers(challengedId);

            String professionalTeamId = professionalTeamPlayers.getFirst().getTeam().getId();
            simulation.setDesafiado(new ObjectId(professionalTeamId));

            simulation.setDesafiadoTeamPlayers(professionalTeamPlayers);
        }

        userService.save(challenger);
        Simulation newSimulation = simulationsRepository.save(simulation);

        if (versusPlayer) {
            NotificationDTO notification = NotificationMapper.toDto(challenger, challengerTeam, simulation.getCreatedAt());
            notificationService.notifyUser(challengedId, notification);
        }

        return newSimulation;
    }

    // TODO - start simulation

    // TODO - accept simulation

    // TODO - refuse simulation
}
