package com.nexusfc.api.User.Service;

import org.springframework.stereotype.Service;

import com.nexusfc.api.Common.NotFoundException;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String notFoundMessage(String id) {
        return String.format("User with id %s", id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User find(String id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(notFoundMessage(id)));
    }

}
