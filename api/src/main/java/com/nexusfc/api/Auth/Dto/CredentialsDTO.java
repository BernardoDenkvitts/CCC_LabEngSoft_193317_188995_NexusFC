package com.nexusfc.api.Auth.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CredentialsDTO {
    private String email;
    private String password;
}
