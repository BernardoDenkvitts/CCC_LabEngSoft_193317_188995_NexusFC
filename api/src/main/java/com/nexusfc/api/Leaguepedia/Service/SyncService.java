package com.nexusfc.api.Leaguepedia.Service;

import com.nexusfc.api.Leaguepedia.Response.CargoQueryResponse;
import com.nexusfc.api.Leaguepedia.Response.PlayerHistoryResponse;
import com.nexusfc.api.Leaguepedia.Response.TournamentTeamsPlayersResponse;
import com.nexusfc.api.Model.Component.MatchHistory;
import com.nexusfc.api.Model.Enum.Lane;
import com.nexusfc.api.Model.ProfessionalPlayer;
import com.nexusfc.api.Model.ProfessionalTeam;
import com.nexusfc.api.Repository.ProfessionalPlayersRepository;
import com.nexusfc.api.Repository.ProfessionalTeamsRepository;
import org.bson.types.ObjectId;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SyncService {

    private final LeaguepediaService leaguepediaService;
    private final ProfessionalPlayersRepository professionalPlayersRepository;
    private final ProfessionalTeamsRepository professionalTeamsRepository;

    private static final Duration PLAYER_FETCH_DELAY = Duration.ofSeconds(5);
    private static final Duration IMAGE_FETCH_DELAY = Duration.ofSeconds(3);

    private final Map<String, Lane> laneMap = Map.ofEntries(
        Map.entry("Top", Lane.TOP),
        Map.entry("Jungle", Lane.JUNGLE),
        Map.entry("Mid", Lane.MID),
        Map.entry("Bot", Lane.ADC),
        Map.entry("Support", Lane.SUP)
    );

    public SyncService(LeaguepediaService leaguepediaService, ProfessionalPlayersRepository professionalPlayersRepository, ProfessionalTeamsRepository professionalTeamsRepository) {
        this.leaguepediaService = leaguepediaService;
        this.professionalPlayersRepository = professionalPlayersRepository;
        this.professionalTeamsRepository = professionalTeamsRepository;
    }

    @Scheduled(cron = "0 0 3 * * MON")
    @Transactional
    public void updatePlayersData() throws InterruptedException {
        TournamentTeamsPlayersResponse tournamentTeamsPlayersResponse = leaguepediaService.fetchTournamentTeamsAndPlayers();

        for (var teamPlayerData : tournamentTeamsPlayersResponse.getCargoquery()) {
            String[] players = teamPlayerData.getTitle().getPlayerNames().split(";;");
            ProfessionalTeam team = professionalTeamsRepository.findByName(teamPlayerData.getTitle().getTeamName());

            for (String playerName : players) {
                // Avoid rate limit
                Thread.sleep(PLAYER_FETCH_DELAY);
                processPlayer(playerName, team);
            }
        }
    }

    private void processPlayer(String playerNick, ProfessionalTeam team) throws InterruptedException {
        PlayerHistoryResponse historyResponse = leaguepediaService.fetchPlayerHistory(playerNick);
        if (historyResponse.getCargoquery().isEmpty()) return;

        System.out.println("Updating data for " + playerNick);
        PlayerStats stats = PlayerStats.calculateFromHistoryResponse(historyResponse);
        ProfessionalPlayer player = professionalPlayersRepository.findByNick(playerNick);
        if (player == null) {
            player = new ProfessionalPlayer();
            player.setNick(playerNick);
        } else {
            return;
        }
        updatePlayerData(player, historyResponse, stats, team);

        Thread.sleep(IMAGE_FETCH_DELAY);
        String imgUrl = leaguepediaService.playerImageUrl(playerNick);
        player.setImageUrl(imgUrl);

        professionalPlayersRepository.save(player);
        System.out.println("Data updated for " + playerNick);
    }

    private void updatePlayerData(ProfessionalPlayer player, PlayerHistoryResponse historyResponse,
                                  PlayerStats stats, ProfessionalTeam team) {
        player.setMatchHistory(historyResponse.getCargoquery().stream()
                .map(match -> MatchHistory.toMatchHistory(match.getTitle()))
                .collect(Collectors.toList()));
        player.setLane(laneMap.get(historyResponse.getCargoquery().getFirst().getTitle().getIngameRole()));
        player.setTeam(new ObjectId(team.getId()));
        // TODO - ask Gemini a cost
        player.setCost(0.0f);
        stats.applyToPlayer(player);
    }

}
