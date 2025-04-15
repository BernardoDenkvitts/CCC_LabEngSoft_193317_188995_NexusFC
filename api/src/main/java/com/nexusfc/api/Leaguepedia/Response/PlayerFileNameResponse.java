package com.nexusfc.api.Leaguepedia.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerFileNameResponse {
    @JsonProperty("cargoquery")
    private List<CargoQueryResult> cargoquery;

    @Data
    public static class CargoQueryResult {
        @JsonProperty("title")
        private Title title;
    }

    @Data
    public static class Title {
        @JsonProperty("FileName")
        private String fileName;
    }
}
