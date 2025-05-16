package com.nexusfc.api.Notification;

import com.nexusfc.api.Model.Simulation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulationResultNotificationDTO {
    private String message;
    private String simulationId;
    private String challengerId;
    private String challengedId;
    private Float betValue;

    public static SimulationResultNotificationDTO create(String message, Simulation simulation) {
        return new SimulationResultNotificationDTO(
                message, simulation.getId(), simulation.getDesafiante().getId(), simulation.getDesafiado().toHexString(), simulation.getBetValue());
    }
}
