package com.nexusfc.api.Leaguepedia;

import com.nexusfc.api.FakeResponse;
import com.nexusfc.api.Leaguepedia.Response.PlayerFileNameResponse;
import com.nexusfc.api.Leaguepedia.Response.PlayerHistoryResponse;
import com.nexusfc.api.Leaguepedia.Response.TournamentTeamsPlayersResponse;
import com.nexusfc.api.Leaguepedia.Service.LeaguepediaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
public class LeaguepediaServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    LeaguepediaService service;

    @Test
    public void shouldFetchPlayerHistory() throws URISyntaxException, IOException {
        PlayerHistoryResponse fakeResponse = FakeResponse.create("fixtures/leaguepedia-player-history-response.json", PlayerHistoryResponse.class);

        when(restTemplate.getForObject(any(), eq(PlayerHistoryResponse.class))).thenReturn(fakeResponse);

        PlayerHistoryResponse historyResponse = service.fetchPlayerHistory("Faker");

        assertThat(historyResponse).isNotNull();

        assertThat(historyResponse.getCargoquery().getFirst().getTitle().getLink()).isEqualTo("Faker");
        assertThat(historyResponse.getCargoquery().getFirst().getTitle().getChampion()).isEqualTo("Viktor");
        assertThat(historyResponse.getCargoquery().getFirst().getTitle().getVersus()).isEqualTo("KT Rolster");
        assertThat(historyResponse.getCargoquery().getFirst().getTitle().getStandardName()).isEqualTo("LCK 2025 Rounds 1-2");
    }

    @Test
    public void shouldFetchTournamentTeamsAndPlayers() throws IOException {
        TournamentTeamsPlayersResponse fakeResponse = FakeResponse.create("fixtures/leaguepedia-tournament-team-and-players-response.json", TournamentTeamsPlayersResponse.class);

        when(restTemplate.getForObject(any(), eq(TournamentTeamsPlayersResponse.class))).thenReturn(fakeResponse);

        TournamentTeamsPlayersResponse response = service.fetchTournamentTeamsAndPlayers();

        assertThat(response).isNotNull();
        assertThat(response.getCargoquery().getFirst().getTitle().getTeamName()).isEqualTo("BNK FEARX");
        assertThat(response.getCargoquery().getFirst().getTitle().getPlayerNames()).isEqualTo("Clear (Song Hyeon-min);;Raptor (Jeon Eo-jin);;VicLa;;Diable;;Kellin;;Ryu;;Joker (Cho Jae-eup)");
    }

    @Test
    public void shouldReturnPlayerImageUrl() throws URISyntaxException, IOException {
        PlayerFileNameResponse fakeResponse = FakeResponse.create("fixtures/leaguepedia-player-filename-response.json", PlayerFileNameResponse.class);

        when(restTemplate.getForObject(any(), eq(PlayerFileNameResponse.class))).thenReturn(fakeResponse);

        String imageUrl = service.playerImageUrl("Raptor (Jeon Eo-jin)");

        assertThat(imageUrl).isNotEmpty();
        assertThat(imageUrl).isEqualTo("https://lol.fandom.com/wiki/Special:Redirect/file/BFX_Raptor_2025_Split_1.png");
    }

}
