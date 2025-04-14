package com.nexusfc.api.Model.Component;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchHistory {
    private String versus;
    private String champion;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Float gold;
    private Integer cs;
    private Boolean win;
    private String vod;
    private String tournament;
}
