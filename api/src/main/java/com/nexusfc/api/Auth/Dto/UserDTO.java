package com.nexusfc.api.Auth.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;


@Data
@AllArgsConstructor
public class UserDTO {
    private String _id;
    private String name;
    private String email;
    private String created_at;
    private String last_rewarded_login;
    private Float coins;
}
