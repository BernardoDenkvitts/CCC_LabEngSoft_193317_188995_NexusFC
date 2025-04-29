package com.nexusfc.api.User.Dto;

import java.time.Instant;

import com.nexusfc.api.Model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDTO {
    private String id;

    private String name;

    private String email;

    private Instant createdAt;

    private Instant lastRewardedLogin;

    private Float coins;

    public static UserResponseDTO fromUserModel(User model) {
        return new UserResponseDTO(model.getId(), model.getName(), model.getEmail(), model.getCreatedAt(), model.getLastRewardedLogin(), model.getCoins());
    }
}
