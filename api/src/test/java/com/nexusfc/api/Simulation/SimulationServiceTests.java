package com.nexusfc.api.Simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexusfc.api.AI.GeminiService;
import com.nexusfc.api.Model.ProfessionalTeam;
import com.nexusfc.api.Model.Simulation;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Model.Component.ProfessionalPlayerEntry;
import com.nexusfc.api.Model.Enum.SimulationStatus;
import com.nexusfc.api.Notification.NotificationDTO;
import com.nexusfc.api.Notification.NotificationMapper;
import com.nexusfc.api.Notification.NotificationService;
import com.nexusfc.api.ProfessionalPlayers.ProfessionalPlayersService;
import com.nexusfc.api.ProfessionalTeams.ProfessionalTeamsService;
import com.nexusfc.api.Repository.SimulationsRepository;
import com.nexusfc.api.User.Service.UserService;
import com.nexusfc.api.User.Service.UserTeamService;

@ExtendWith(MockitoExtension.class)
public class SimulationServiceTests {
    @Mock
    private SimulationsRepository simulationsRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserTeamService userTeamService;
    @Mock
    private ProfessionalTeamsService professionalTeamsService;
    @Mock
    private ProfessionalPlayersService professionalPlayersService;
    @Mock
    private GeminiService AIService;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SimulationService simulationService;

    @Test
    void shouldCreatSimulationAgainstProfessionalTeam() {
        Float betValue = 75f;

        User challenger = TestData.createTestUser();
        UserTeam challengerTeam = TestData.createTestUserTeam(challenger.getId());
        ProfessionalTeam professionalTeam = TestData.createTestProfessionalTeam();

        when(userService.find(challenger.getId())).thenReturn(challenger);
        when(userTeamService.find(challenger.getId())).thenReturn(challengerTeam);

        List<ProfessionalPlayerEntry> proEntries = challengerTeam.getProfessionalPlayers();
        when(professionalTeamsService.getTeamPlayers(professionalTeam.getId()))
                .thenReturn(proEntries.stream().map(e -> e.getPlayer()).toList());

        when(simulationsRepository.save(any(Simulation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Simulation simulation = simulationService.create(challenger.getId(), professionalTeam.getId(), false, betValue);

        verify(userService).save(challenger);
        verify(simulationsRepository).save(any(Simulation.class));
        verify(userService, times(1)).find(anyString());
        verify(userTeamService).find(challenger.getId());

        assertNotNull(simulation);
        assertNotNull(simulation.getDesafianteTeamPlayers());
        assertNotNull(simulation.getDesafiadoTeamPlayers());
        assertEquals(challenger.getCoins(), 25f);
        assertEquals(simulation.getStatus(), SimulationStatus.REQUESTED);
        assertEquals(challengerTeam.getStarterPlayers().size(), simulation.getDesafianteTeamPlayers().size());
    }

    @Test
    void shouldCreateVersusUser() {
        Float betValue = 75f;

        User challenger = TestData.createTestUser();
        User challenged = TestData.createTestUser();
        UserTeam challengerTeam = TestData.createTestUserTeam(challenger.getId());

        when(userService.find(challenger.getId())).thenReturn(challenger);
        when(userTeamService.find(anyString())).thenReturn(challengerTeam);
        when(userService.find(challenged.getId())).thenReturn(challenged);
        when(simulationsRepository.save(any(Simulation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Simulation simulation = simulationService.create(challenger.getId(), challenged.getId(), true, betValue);

        verify(userService).save(challenger);

        NotificationDTO notification = NotificationMapper.toDto(challenger, challengerTeam, simulation.getCreatedAt());
        verify(notificationService, times(1)).notifyUser(challenged.getId(), notification);

        verify(simulationsRepository).save(any(Simulation.class));
        verify(userService).find(challenger.getId());
        verify(userService).find(challenged.getId());
        verify(userTeamService).find(challenger.getId());

        assertNotNull(simulation);
        assertNotNull(simulation.getDesafianteTeamPlayers());
        assertEquals(simulation.getDesafiado(), new ObjectId(challenged.getId()));
        assertEquals(challenger.getCoins(), 25f);
        assertEquals(simulation.getStatus(), SimulationStatus.REQUESTED);
        assertEquals(challengerTeam.getStarterPlayers().size(), simulation.getDesafianteTeamPlayers().size());
    }

}
