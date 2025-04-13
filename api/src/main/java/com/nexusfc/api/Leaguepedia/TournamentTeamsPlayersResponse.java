package com.nexusfc.api.Leaguepedia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TournamentTeamsPlayersResponse {
    @JsonProperty("cargoquery")
    private List<CargoQueryResult> cargoquery;

    @Data
    public static class CargoQueryResult {
        @JsonProperty("title")
        private Title title;
    }

    @Data
    public static class Title {
        @JsonProperty("TeamName")
        private String teamName;
        @JsonProperty("PlayerNames")
        private String playerNames;
    }
}

