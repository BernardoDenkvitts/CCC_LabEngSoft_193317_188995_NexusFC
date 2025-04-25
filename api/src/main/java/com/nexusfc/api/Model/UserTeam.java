package com.nexusfc.api.Model;


import com.nexusfc.api.Model.Component.ProfessionalPlayerEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "UserTeams")
public class UserTeam {
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("user_id")
    private ObjectId userId;

    @Field("professional_players")
    private List<ProfessionalPlayerEntry> professionalPlayers = new ArrayList<>();

    public UserTeam(String userId) {
        this.userId = new ObjectId(userId);
        this.name = String.format("UserTeam%s", this.userId.toString().substring(0, 6) + this.userId.toString().substring(this.userId.toString().length() - 6));
    }

}

