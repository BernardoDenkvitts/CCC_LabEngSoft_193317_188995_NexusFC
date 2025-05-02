package com.nexusfc.api.User.Service;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.nexusfc.api.Common.NotFoundException;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Repository.UserTeamsRepository;

@Service
public class UserTeamService {

    private final UserTeamsRepository userTeamsRepository;

    public UserTeamService(UserTeamsRepository userTeamsRepository) {
        this.userTeamsRepository = userTeamsRepository;
    }

    public UserTeam save(UserTeam userTeam) {
        return userTeamsRepository.save(userTeam);
    }

    public UserTeam getUserTeam(String userId) {
        return Optional.ofNullable(userTeamsRepository.findByUserId(new ObjectId(userId))).orElseThrow(() -> new NotFoundException("User with id " + userId));
    }

}
