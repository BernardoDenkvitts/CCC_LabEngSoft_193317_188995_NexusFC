package com.nexusfc.api.User.Service;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.nexusfc.api.Common.NotFoundException;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Repository.UserRepository;
import com.nexusfc.api.Repository.UserTeamsRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserTeamsRepository userTeamsRepository;

    public UserService(UserRepository userRepository, UserTeamsRepository userTeamsRepository) {
        this.userRepository = userRepository;
        this.userTeamsRepository = userTeamsRepository;
    }

    private String notFoundMessage(String id) {
        return String.format("User with id %s", id);
    }

    public User getUserData(String id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(notFoundMessage(id)));
    }

    public UserTeam getUserTeam(String userId) {
        UserTeam team = Optional.ofNullable(userTeamsRepository.findByUserId(new ObjectId(userId))).orElseThrow(() -> new NotFoundException(notFoundMessage(userId)));
                
        return team;
    }

}
