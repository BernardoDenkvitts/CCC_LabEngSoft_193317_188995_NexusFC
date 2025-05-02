package com.nexusfc.api.Market.Dto;

import com.nexusfc.api.Model.UserTeam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {
    private Float newBalance;
    private UserTeam updatedTeam;
}
