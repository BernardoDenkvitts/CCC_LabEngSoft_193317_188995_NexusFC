package com.nexusfc.api.Leaguepedia.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


public class PlayerFileNameResponse extends CargoQueryResponse<PlayerFileNameResponse.FileNameTitle> {
    @Data
    public static class FileNameTitle {
        @JsonProperty("FileName")
        private String fileName;
    }
}
