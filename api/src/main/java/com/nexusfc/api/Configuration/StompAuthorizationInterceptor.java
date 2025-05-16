package com.nexusfc.api.Configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.nexusfc.api.Repository.UserRepository;
import com.nexusfc.api.Security.TokenService;

@Component
public class StompAuthorizationInterceptor implements ChannelInterceptor {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(StompAuthorizationInterceptor.class);

    public StompAuthorizationInterceptor(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand()))
            authenticateUser(accessor);

        return message;
    }

    private void authenticateUser(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if (authHeader == null) throw new MessagingException("Missing Authorization header");
        
        String token = authHeader.substring(7);
        try {
            DecodedJWT decodedJWT = tokenService.validateToken(token);
            String userId = decodedJWT.getSubject();
            UserDetails user = userRepository.findById(userId).orElseThrow(() -> new MessagingException("Unauthorized"));

            var auth = new UsernamePasswordAuthenticationToken(userId, null, user.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(auth);
            accessor.setUser(auth);
            logger.info("User with identifier {} connected", auth.getName());
        } catch (Exception e) {
            throw new MessagingException("Invalid token");
        }
    }
}
