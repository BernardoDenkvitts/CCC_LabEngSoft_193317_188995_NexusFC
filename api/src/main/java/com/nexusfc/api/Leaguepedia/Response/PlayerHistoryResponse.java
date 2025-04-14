package com.nexusfc.api.Leaguepedia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerHistoryResponse {
    @JsonProperty("cargoquery")
    private List<CargoQueryResult> cargoquery;

    @Data
    public static class CargoQueryResult {
        @JsonProperty("title")
        private Title title;
    }

    @Data
    public static class Title {
        @JsonProperty("Link")
        private String link;
        @JsonProperty("Team")
        private String versus;
        @JsonProperty("Champion")
        private String champion;
        @JsonProperty("Kills")
        private String kills;
        @JsonProperty("Deaths")
        private String deaths;
        @JsonProperty("Assists")
        private String assists;
        @JsonProperty("Gold")
        private String gold;
        @JsonProperty("CS")
        private String cs;
        @JsonProperty("IngameRole")
        private String ingameRole;
        @JsonProperty("DamageToChampions")
        private String damageToChampions;
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
}
