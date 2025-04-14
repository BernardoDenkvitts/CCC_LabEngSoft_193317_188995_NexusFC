package com.nexusfc.api.Model.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalPlayerEntry {
    @Field("player_id")
    private String playerId;

    @Field("is_starter")
    private Boolean isStarter;
}
