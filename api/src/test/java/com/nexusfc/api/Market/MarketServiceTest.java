package com.nexusfc.api.Market;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexusfc.api.Market.Dto.TransactionResponseDTO;
import com.nexusfc.api.Market.Exception.InsufficientBalance;
import com.nexusfc.api.Model.ProfessionalPlayer;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Model.Component.ProfessionalPlayerEntry;
import com.nexusfc.api.ProfessionalPlayers.ProfessionalPlayersService;
import com.nexusfc.api.User.Service.UserService;
import com.nexusfc.api.User.Service.UserTeamService;

@ExtendWith(MockitoExtension.class)
public class MarketServiceTest {
    @Mock
    private ProfessionalPlayersService playersService;

    @Mock
    private UserService userService;

    @Mock
    private UserTeamService userTeamService;

    @InjectMocks
    private MarketService marketService;

    @Test
    public void shouldBuyPlayer() {
        ProfessionalPlayer player = getTestProfessionalPlayer();
        User user = getTestUser();
        UserTeam team = getTestUserTeam();

        when(playersService.getProfessionalPlayerById(player.getId())).thenReturn(player);
        when(userService.find(user.getId())).thenReturn(user);
        when(userTeamService.find(user.getId())).thenReturn(team);

        TransactionResponseDTO response = marketService.buyPlayer(user.getId(), player.getId());

        assertEquals(1, response.getNewBalance());
        assertTrue(response.getUpdatedTeam().hasPlayer(player.getId()));
        assertEquals(1f, user.getCoins());

        verify(userTeamService, times(1)).find(user.getId());
    }

    @Test
    public void shouldThrowInsufficientBalanceException() {
        ProfessionalPlayer player = getTestProfessionalPlayer();
        User user = getTestUser();
        user.setCoins(50f); // Saldo menor que o custo do jogador (99f)
        UserTeam team = getTestUserTeam();

        when(playersService.getProfessionalPlayerById(player.getId())).thenReturn(player);
        when(userService.find(user.getId())).thenReturn(user);
        when(userTeamService.find(user.getId())).thenReturn(team);

        assertThrows(InsufficientBalance.class, () -> {
            marketService.buyPlayer(user.getId(), player.getId());
        });

        assertEquals(50f, user.getCoins(), 0.001);
    }

    @Test
    public void shouldSellPlayerAndIncreaseUserCoins() {
        ProfessionalPlayer player = getTestProfessionalPlayer();
        User user = getTestUser();
        UserTeam team = getTestUserTeam();

        team.addPlayer(new ProfessionalPlayerEntry(player));

        when(playersService.getProfessionalPlayerById(player.getId())).thenReturn(player);
        when(userService.find(user.getId())).thenReturn(user);
        when(userTeamService.find(user.getId())).thenReturn(team);

        TransactionResponseDTO response = marketService.sellPlayer(user.getId(), player.getId());

        assertEquals(199, response.getNewBalance());
        assertFalse(response.getUpdatedTeam().hasPlayer(player.getId()));

        verify(userTeamService).save(team);
        verify(userService).save(user);
    }

    private User getTestUser() {
        User user = new User();
        user.setId("680ad2510611750dc0d72539");
        user.setName("John Doe");
        user.setCoins(100f);

        return user;
    }

    private ProfessionalPlayer getTestProfessionalPlayer() {
        ProfessionalPlayer player = new ProfessionalPlayer();
        player.setId("321ad2510611750dc0d72698");
        player.setNick("Faker");
        player.setCost(99f);

        return player;
    }

    private UserTeam getTestUserTeam() {
        UserTeam team = new UserTeam();
        team.setId("876ad2510611750dc0d72654");
        team.setName("TestTeam");

        return team;
    }
}
