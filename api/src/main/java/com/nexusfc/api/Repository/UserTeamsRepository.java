package com.nexusfc.api.Repository;

import com.nexusfc.api.Model.UserTeam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamsRepository extends MongoRepository<UserTeam, String> {
}
