package com.nexusfc.api.Simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.nexusfc.api.Model.ProfessionalTeam;
import com.nexusfc.api.Model.Simulation;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Model.Component.ProfessionalPlayerEntry;
import com.nexusfc.api.Model.Enum.SimulationStatus;
import com.nexusfc.api.Notification.SimulationNotificationDTO;
import com.nexusfc.api.Notification.NotificationMapper;
import com.nexusfc.api.Notification.NotificationService;
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
    private NotificationService notificationService;
    @Mock
    private ApplicationEventPublisher publisher;

    @Spy
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

        SimulationNotificationDTO notification = NotificationMapper.toDto(challenger, challengerTeam,
                simulation.getCreatedAt());
        verify(notificationService, times(1)).notifyUser(challenged.getId(), notification);

        verify(simulationsRepository).save(any(Simulation.class));
        verify(userService).find(challenger.getId());
        verify(userService).find(challenged.getId());
        verify(userTeamService, times(2)).find(anyString());

        assertNotNull(simulation);
        assertNotNull(simulation.getDesafianteTeamPlayers());
        assertEquals(simulation.getDesafiado(), new ObjectId(challenged.getId()));
        assertEquals(challenger.getCoins(), 25f);
        assertEquals(simulation.getStatus(), SimulationStatus.REQUESTED);
        assertEquals(challengerTeam.getStarterPlayers().size(), simulation.getDesafianteTeamPlayers().size());
    }

    @Test
    void shouldAcceptSimulation() {
        String simId = new ObjectId().toHexString();
        Simulation sim = new Simulation();
        sim.setId(simId);
        sim.setBetValue(50f);

        User challenged = TestData.createTestUser();
        challenged.setCoins(200f);

        UserTeam challengedTeam = TestData.createTestUserTeam(challenged.getId());

        doReturn(sim).when(simulationService).find(simId, challenged.getId());

        when(userService.find(challenged.getId())).thenReturn(challenged);
        when(userTeamService.find(challenged.getId())).thenReturn(challengedTeam);
        when(simulationsRepository.save(sim)).thenReturn(sim);

        Simulation result = simulationService.accept(simId, challenged.getId());

        assertSame(sim, result);
        assertEquals(
                challengedTeam.getStarterPlayers().stream()
                        .map(entry -> entry.getPlayer())
                        .toList(),
                sim.getDesafiadoTeamPlayers());

        assertEquals(SimulationStatus.IN_PROGRESS, result.getStatus());
        verify(publisher, times(1)).publishEvent(any(SimulationAcceptedEvent.class));
        verify(simulationService).find(simId, challenged.getId());
        verify(userService).find(challenged.getId());
        verify(userTeamService).find(challenged.getId());
        verify(simulationsRepository).save(sim);
    }

    @Test
    void shouldStartPveSimulationSuccessfully() {
        String simId = new ObjectId().toHexString();

        Simulation sim = new Simulation();
        sim.setId(simId);
        sim.setVersusPlayer(false);
        User challenger = TestData.createTestUser();
        sim.setDesafiante(challenger);
        sim.setStatus(SimulationStatus.REQUESTED);

        doReturn(sim).when(simulationService).find(simId, challenger.getId());

        when(simulationsRepository.save(sim)).thenReturn(sim);

        Simulation result = simulationService.startPveSimulation(simId, challenger.getId());

        assertSame(sim, result);
        assertEquals(SimulationStatus.IN_PROGRESS, sim.getStatus());
        verify(simulationService).find(simId, challenger.getId());
        verify(publisher, times(1)).publishEvent(any(SimulationAcceptedEvent.class));
        verify(simulationsRepository).save(sim);
    }

}
