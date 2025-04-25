package com.nexusfc.api.Auth;


import com.nexusfc.api.Auth.Dto.CredentialsDTO;
import com.nexusfc.api.Auth.Dto.LoginResponseDTO;
import com.nexusfc.api.Auth.Dto.NewUserDTO;
import com.nexusfc.api.Auth.Exception.EmailAlreadyInUseException;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Repository.UserRepository;
import com.nexusfc.api.Repository.UserTeamsRepository;
import com.nexusfc.api.Security.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserTeamsRepository userTeamsRepository;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private TokenService tokenService;

    @Mock
    private User user;

    @InjectMocks
    AuthService authService;

    @Test
    public void shouldCreateUser() {
        NewUserDTO dto = new NewUserDTO();
        dto.setName("John Doe");
        dto.setEmail("john@gmail.com");
        dto.setPassword("securePassword123");
        
        User user = getTestUser();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(null);
        when(passwordEncoder.encode("securePassword123")).thenReturn("encodedPassword123");
        when(userTeamsRepository.save(new UserTeam(user.getId()))).thenReturn(null);

        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = authService.create(dto);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@gmail.com", result.getEmail());
        assertEquals("securePassword123", result.getPassword());
        assertEquals(120f, result.getCoins());

        verify(userRepository).findByEmail("john@gmail.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void shouldReturnEmailAlreadyInUseException() {
        NewUserDTO dto = new NewUserDTO();
        dto.setName("John Doe");
        dto.setEmail("john@gmail.com");
        dto.setPassword("securePassword");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(getTestUser());

        assertThrows(EmailAlreadyInUseException.class, () -> {
            authService.create(dto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldAddCoinsAndReturnLogin() {
        CredentialsDTO dto = new CredentialsDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("password");

        when(applicationContext.getBean(AuthenticationManager.class)).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.isFirstDailyLogin()).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("mocked-token");

        LoginResponseDTO loginResponseDTO = authService.login(dto);

        verify(user).addCoins(10f);
        assertEquals("mocked-token", loginResponseDTO.getToken());
    }

    private User getTestUser() {
        User user = new User();
        user.setId("680ad2510611750dc0d72539");
        user.setName("John Doe");
        user.setEmail("john@gmail.com");
        user.setPassword("securePassword123");

        return user;
    }

}

