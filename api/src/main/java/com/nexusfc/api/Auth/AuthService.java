package com.nexusfc.api.Auth;

import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Repository.UserTeamsRepository;
import com.nexusfc.api.Security.TokenService;
import org.springframework.context.ApplicationContext;
import com.nexusfc.api.Auth.Dto.CredentialsDTO;
import com.nexusfc.api.Auth.Dto.LoginResponseDTO;
import com.nexusfc.api.Auth.Dto.NewUserDTO;
import com.nexusfc.api.Auth.Exception.EmailAlreadyInUseException;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    private ApplicationContext applicationContext;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final UserTeamsRepository userTeamsRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(ApplicationContext applicationContext, TokenService tokenService, UserRepository userRepository, UserTeamsRepository userTeamsRepository, PasswordEncoder passwordEncoder) {
        this.applicationContext = applicationContext;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.userTeamsRepository = userTeamsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User create(NewUserDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()) != null)
            throw new EmailAlreadyInUseException();

        User user = userRepository.save(new User(dto.getEmail(), dto.getName(), passwordEncoder.encode(dto.getPassword())));

        userTeamsRepository.save(new UserTeam(user.getId()));

        return user;
    }

    @Transactional
    public LoginResponseDTO login(CredentialsDTO dto) {
        var authenticationManager = applicationContext.getBean(AuthenticationManager.class);

        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        User user = (User) auth.getPrincipal();
        if (user.isFirstDailyLogin()) {
            user.increaseCoins(10f);
            userRepository.save(user);
        }

        return new LoginResponseDTO(token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByEmail(username)).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
