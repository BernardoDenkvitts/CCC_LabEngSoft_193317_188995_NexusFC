package com.nexusfc.api.Auth.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewUserDTO {
    private String name;
    private String email;
    private String password;
}
