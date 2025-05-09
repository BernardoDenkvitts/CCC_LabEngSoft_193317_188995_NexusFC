package com.nexusfc.api.Model;

import com.nexusfc.api.Model.Enum.SimulationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
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
    private Boolean versusPlayer;

    @Field("desafiante_id")
    @DocumentReference
    private User desafiante;

    @Field("desafiado_id")
    private ObjectId desafiado;

    @Field("status")
    private SimulationStatus status = SimulationStatus.REQUESTED;

    @Field("bet_value")
    private Float betValue;

    @Field("created_at")
    private Instant createdAt = Instant.now();

    @Field("win")
    private Boolean win;

    @Field("desafiante_team")
    @DocumentReference
    private List<ProfessionalPlayer> desafianteTeamPlayers;

    @Field("desafiado_team")
    @DocumentReference
    private List<ProfessionalPlayer> desafiadoTeamPlayers;

    public Simulation(User challenger, Float betValue, List<ProfessionalPlayer> challengerTeamPlayers, Boolean versusPlayer) {
        this.desafiante = challenger;
        this.betValue = betValue;
        this.desafianteTeamPlayers = challengerTeamPlayers;
        this.versusPlayer = versusPlayer;
    }
}
