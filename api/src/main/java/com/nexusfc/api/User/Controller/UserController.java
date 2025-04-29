package com.nexusfc.api.User.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.User.Dto.UserResponseDTO;
import com.nexusfc.api.User.Service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserData(@PathVariable String id) {
        return UserResponseDTO.fromUserModel(service.getUserData(id));
    }

    @GetMapping("/{id}/team")
    public UserTeam getUserTeam(@PathVariable String id) {
        return service.getUserTeam(id);
    }

}
