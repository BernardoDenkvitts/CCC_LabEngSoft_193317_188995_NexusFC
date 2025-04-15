package com.nexusfc.api.Leaguepedia.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class TournamentTeamsPlayersResponse extends CargoQueryResponse<TournamentTeamsPlayersResponse.TournamentTeamPlayerTitle> {
    @Data
    public static class TournamentTeamPlayerTitle {
        @JsonProperty("TeamName")
        private String teamName;
        @JsonProperty("PlayerNames")
        private String playerNames;
    }
}

