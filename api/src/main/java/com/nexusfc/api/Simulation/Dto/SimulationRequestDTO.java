package com.nexusfc.api.Simulation.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulationRequestDTO {
    private String challengerId;
    private String challengedId;
    private Boolean versusPlayer;
    private Float betValue;
}
