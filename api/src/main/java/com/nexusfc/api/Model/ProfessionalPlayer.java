package com.nexusfc.api.Model;

import com.nexusfc.api.Model.Component.MatchHistory;
import com.nexusfc.api.Model.Enum.Lane;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ProfessionalPlayers")
public class ProfessionalPlayer {
    @Id
    private String id;

    @Field("nick")
    private String nick;

    @Field("lane")
    private Lane lane;

    @Field("team")
    @DocumentReference
    private ProfessionalTeam team;

    @Field("match_history")
    private List<MatchHistory> matchHistory;

    @Field("overall_kill")
    private Float overallKill;

    @Field("overall_death")
    private Float overallDeath;

    @Field("overall_assist")
    private Float overallAssist;

    @Field("overall_damage")
    private Float overallDamage;

    @Field("overall_gold")
    private Float overallGold;

    @Field("overall_cs")
    private Float overallCs;

    @Field("overall_win_rate")
    private Float overallWinRate;

    @Field("cost")
    private Float cost;

    @Field("image_url")
    private String imageUrl;

    public String statsToString() {
        StringBuilder sb = new StringBuilder();

        sb.append("nick: ").append(nick).append("\n");
        sb.append("lane: ").append(lane.name()).append("\n");
        sb.append("overall kill: ").append(String.format("%.2f", overallKill)).append("\n");
        sb.append("overall death: ").append(String.format("%.2f", overallDeath)).append("\n");
        sb.append("overall assist: ").append(String.format("%.2f", overallAssist)).append("\n");
        sb.append("overall Damage: ").append(String.format("%.2f", overallDamage)).append("\n");
        sb.append("overall gold: ").append(String.format("%.2f", overallGold)).append("\n");
        sb.append("overall cs: ").append(String.format("%.2f", overallCs)).append("\n");
        sb.append("overall win rate: ").append(String.format("%.2f", overallWinRate));

        return sb.toString();
    }
}
