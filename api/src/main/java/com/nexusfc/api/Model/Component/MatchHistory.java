package com.nexusfc.api.Model.Component;


import com.nexusfc.api.Leaguepedia.Response.PlayerHistoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchHistory {
    private String versus;
    private String champion;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Integer gold;
    private Integer cs;
    private Boolean win;
    private String vod;
    private String tournament;

    public static MatchHistory toMatchHistory(PlayerHistoryResponse.PlayerHistoryTitle data)  {
        return new MatchHistory(
            data.getVersus(),
            data.getChampion(),
            data.getKills(),
            data.getDeaths(),
            data.getAssists(),
            data.getGold(),
            data.getCs(),
            Objects.equals(data.getPlayerWin(), "Yes"),
            data.getVod(),
            data.getStandardName()
        );
    }
}
