package com.nexusfc.api.Model.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import com.nexusfc.api.Model.ProfessionalPlayer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalPlayerEntry {
    @Field("player_id")
    @DocumentReference
    private ProfessionalPlayer player;

    @Field("is_starter")
    private Boolean isStarter;

    public ProfessionalPlayerEntry(ProfessionalPlayer player) {
        this.player = player;
        this.isStarter = false;
    }

}
