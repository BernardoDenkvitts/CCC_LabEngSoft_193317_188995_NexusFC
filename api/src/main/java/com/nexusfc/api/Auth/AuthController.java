package com.nexusfc.api.Auth;

import com.nexusfc.api.Auth.Dto.CredentialsDTO;
import com.nexusfc.api.Auth.Dto.LoginResponseDTO;
import com.nexusfc.api.Auth.Dto.NewUserDTO;
import com.nexusfc.api.Model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody NewUserDTO dto) {
        User newUser = authService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{id}").buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody CredentialsDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
