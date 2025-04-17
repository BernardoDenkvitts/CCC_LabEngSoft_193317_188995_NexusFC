package com.nexusfc.api.Repository;

import com.nexusfc.api.Model.ProfessionalTeam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalTeamsRepository extends MongoRepository<ProfessionalTeam, String> {
    ProfessionalTeam findByName(String name);
}
