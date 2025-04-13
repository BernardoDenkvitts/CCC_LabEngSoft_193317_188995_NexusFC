package com.nexusfc.api.Leaguepedia;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Year;

@Service
public class LeaguepediaService {

    private final RestTemplate restTemplate;
    private final static String baseUrl = "https://lol.fandom.com/api.php";

    public LeaguepediaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PlayerHistoryResponse fetchPlayerHistory(String nick) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("action", "cargoquery")
                .queryParam("format", "json")
                .queryParam("tables", "ScoreboardGames=SG,Tournaments=IT,ScoreboardPlayers=SP,ScoreboardPlayers=SPVs,PlayerRedirects=PR,MatchScheduleGame=MSG,MatchSchedule=MS")
                .queryParam("fields", "SP.Link,SP.Team,SP.Champion,SP.Kills,SP.Deaths,SP.Assists,SP.Gold,SP.CS,SP.IngameRole,SP.DamageToChampions,IT.StandardName,SP.PlayerWin,SP.GameId,SPVs.Team,SG.DateTime_UTC,SG.Patch,SG.VOD")
                .queryParam("where", "SP.Link=\"" + nick + "\" AND IT.Year=\"" + Year.now()+ "\" AND IT.TournamentLevel='Primary'")
                .queryParam("join_on", "SG.GameId=SP.GameId,IT.OverviewPage=SG.OverviewPage,SP.UniqueRoleVs=SPVs.UniqueRole,SP.Link=PR.AllName,SG.GameId=MSG.GameId,MSG.MatchId=MS.MatchId")
                .queryParam("order_by", "SG.DateTime_UTC DESC")
                .queryParam("limit", "10");

        URI uri = builder.build().toUri();

        return this.restTemplate.getForObject(uri, PlayerHistoryResponse.class);
    }

    public TournamentTeamsPlayersResponse fetchTournamentTeamsAndPlayers() {
        String baseUrl = "https://lol.fandom.com/api.php";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("action", "cargoquery")
                .queryParam("format", "json")
                .queryParam("tables", "TournamentRosters=TR")
                .queryParam("fields", "TR.Team=TeamName,TR.RosterLinks=PlayerNames")
                .queryParam("where", "TR.Tournament LIKE '%LCK Cup " + Year.now() + "%'")
                .queryParam("limit", 20);

        URI uri = builder.build().toUri();

        return restTemplate.getForObject(uri, TournamentTeamsPlayersResponse.class);
    }
}
