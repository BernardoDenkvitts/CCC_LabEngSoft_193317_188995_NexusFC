package com.nexusfc.api.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ProfessionalTeams")
public class ProfessionalTeam {
    @Id
    private String id;

    @Field("league")
    private String league;

    @Field("name")
    private String name;

    @Field("region")
    private String region;
}
