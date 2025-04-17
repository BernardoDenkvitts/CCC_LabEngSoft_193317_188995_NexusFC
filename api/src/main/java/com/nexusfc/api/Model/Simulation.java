package com.nexusfc.api.Model;

import com.nexusfc.api.Model.Enum.SimulationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Simulations")
public class Simulation {
    @Id
    private String id;

    @Field("versus_player")
    private String versusPlayer;

    @Field("desafiante_id")
    private ObjectId desafianteId;

    @Field("desafiado_id")
    private ObjectId desafiadoId;

    @Field("status")
    private SimulationStatus status;

    @Field("bet_value")
    private Float betValue;

    @Field("created_at")
    private Instant createdAt;

    @Field("win")
    private Boolean win;

    @Field("desafiante_team")
    private List<String> desafianteTeamId;

    @Field("desafiado_team")
    private List<String> desafiadoTeamId;
}
