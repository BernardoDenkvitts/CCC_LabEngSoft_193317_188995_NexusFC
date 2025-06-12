package com.nexusfc.api.Auth;

import com.nexusfc.api.Auth.Dto.CredentialsDTO;
import com.nexusfc.api.Auth.Dto.LoginResponseDTO;
import com.nexusfc.api.Auth.Dto.NewUserDTO;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;


    public AuthController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody NewUserDTO dto) {
        User newUser = authService.create(dto);
        URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/users/{id}")
            .buildAndExpand(newUser.getId())
            .toUri();

        // Gerar token
        String token = tokenService.generateToken(newUser);

        // Montar DTO para resposta
        LoginResponseDTO responseDTO = new LoginResponseDTO(
            newUser.getId(),
            newUser.getName(),
            newUser.getEmail(),
            newUser.getCreatedAt().toString(),
            newUser.getLastRewardedLogin() != null ? newUser.getLastRewardedLogin().toString() : Instant.now().toString(),
            newUser.getCoins(),
            token
        );

        // Retorna status 201 com Location e body com o DTO
        return ResponseEntity.created(location).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody CredentialsDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
