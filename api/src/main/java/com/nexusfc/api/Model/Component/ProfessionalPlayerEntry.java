package com.nexusfc.api.Model.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalPlayerEntry {
    @Field("player_id")
    private ObjectId playerId;

    @Field("is_starter")
    private Boolean isStarter;
}
