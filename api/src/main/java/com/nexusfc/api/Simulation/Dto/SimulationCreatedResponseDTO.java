package com.nexusfc.api.Simulation.Dto;

import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.nexusfc.api.Model.Simulation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulationCreatedResponseDTO {
    @JsonUnwrapped
    private SimulationResponseDTO simulationResponseDTO; 
    private Float remainingCoins;

    public static SimulationCreatedResponseDTO create(Simulation simulation, Float remainingCoins) {
        SimulationResponseDTO simulationResponseDTO = SimulationResponseDTO.from(simulation);
        SimulationCreatedResponseDTO dto = new SimulationCreatedResponseDTO(simulationResponseDTO, remainingCoins);
        dto.setRemainingCoins(remainingCoins);

        return dto;
    }

}
