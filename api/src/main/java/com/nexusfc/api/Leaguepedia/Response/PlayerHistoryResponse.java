package com.nexusfc.api.Leaguepedia.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class PlayerHistoryResponse extends CargoQueryResponse<PlayerHistoryResponse.PlayerHistoryTitle> {
    @Data
    public static class PlayerHistoryTitle {
        @JsonProperty("Link")
        private String link;
        @JsonProperty("Team")
        private String versus;
        @JsonProperty("Champion")
        private String champion;
        @JsonProperty("Kills")
        private Integer kills;
        @JsonProperty("Deaths")
        private Integer deaths;
        @JsonProperty("Assists")
        private Integer assists;
        @JsonProperty("Gold")
        private Integer gold;
        @JsonProperty("CS")
        private Integer cs;
        @JsonProperty("IngameRole")
        private String ingameRole;
        @JsonProperty("DamageToChampions")
        private Integer damageToChampions;
        @JsonProperty("StandardName")
        private String standardName;
        @JsonProperty("PlayerWin")
        private String playerWin;
        @JsonProperty("GameId")
        private String gameId;
        @JsonProperty("DateTime UTC")
        private String dateTimeUTC;
        @JsonProperty("Patch")
        private String patch;
        @JsonProperty("VOD")
        private String vod;
        @JsonProperty("DateTime UTC__precision")
        private String dateTimeUTCPrecision;
    }

//    public static MatchHistory toMatchHistory(PlayerHistoryResponse.Title data) {
//        return new MatchHistory(
//                data.getVersus(),
//                data.getChampion(),
//                data.getKills(),
//                data.getDeaths(),
//                data.getAssists(),
//                data.getGold(),
//                data.getCs(),
//                data.getPlayerWin() == "Yes" ? true : false,
//                data.getVod(),
//                data.getStandardName()
//        );
//    }
}
