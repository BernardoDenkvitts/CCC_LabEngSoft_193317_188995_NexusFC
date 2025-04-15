package com.nexusfc.api.Leaguepedia.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CargoQueryResponse<T> {
    @JsonProperty("cargoquery")
    private List<CargoQueryResult<T>> cargoquery;

    @Data
    public static class CargoQueryResult<T> {
        @JsonProperty("title")
        private T title;
    }
}
