package com.nexusfc.api.Leaguepedia.Service;

import com.nexusfc.api.Leaguepedia.Response.PlayerHistoryResponse;
import com.nexusfc.api.Model.ProfessionalPlayer;

public class PlayerStats {
    private Float overallKill;
    private Float overallDeath;
    private Float overallAssist;
    private Float overallDamage;
    private Float overallGold;
    private Float overallCs;
    private Float wins;

    private PlayerStats() {
        overallKill = 0.0f;
        overallDeath = 0.0f;
        overallAssist = 0.0f;
        overallDamage = 0.0f;
        overallGold = 0.0f;
        overallCs = 0.0f;
        wins = 0.0f;
    }

    public static PlayerStats calculateFromHistoryResponse(PlayerHistoryResponse historyResponse) {
        PlayerStats stats = new PlayerStats();
        int totalMatches = historyResponse.getCargoquery().size();

        for (var matchData : historyResponse.getCargoquery()) stats.addMatchData(matchData.getTitle());

        stats.calculateAverages(totalMatches);

        return stats;
    }

    private void addMatchData(PlayerHistoryResponse.PlayerHistoryTitle match) {
        overallKill += match.getKills();
        overallDeath += match.getDeaths();
        overallAssist += match.getAssists();
        overallDamage += match.getDamageToChampions();
        overallGold += match.getGold();
        overallCs += match.getCs();
        if ("Yes".equals(match.getPlayerWin())) wins++;
    }

    private void calculateAverages(int totalMatches) {
        overallKill = overallKill / totalMatches;
        overallDeath = overallDeath / totalMatches ;
        overallAssist = overallAssist / totalMatches;
        overallDamage = overallDamage / totalMatches;
        overallGold = overallGold / totalMatches;
        overallCs = overallCs / totalMatches;
        wins = (wins / totalMatches) * 100;
    }

    public void applyToPlayer(ProfessionalPlayer player) {
        player.setOverallKill(overallKill);
        player.setOverallDeath(overallDeath);
        player.setOverallAssist(overallAssist);
        player.setOverallDamage(overallDamage);
        player.setOverallGold(overallGold);
        player.setOverallCs(overallCs);
        player.setOverallWinRate(wins);
    }
}
