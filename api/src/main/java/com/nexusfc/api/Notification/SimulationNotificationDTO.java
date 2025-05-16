package com.nexusfc.api.Notification;

import java.time.Instant;
import java.util.List;

import com.nexusfc.api.Simulation.Dto.ProfessionalPlayerResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulationNotificationDTO {
    private Instant instant;
    private String message;
    private String challengerId;
    private String challengerName;
    private String teamName;
    private List<ProfessionalPlayerResponseDTO> players;

    public SimulationNotificationDTO(String challengerId, String challengerName, String teamName, Instant instant, List<ProfessionalPlayerResponseDTO> players) {
        this.challengerId = challengerId;
        this.challengerName = challengerName;
        this.teamName = teamName;
        this.instant = instant;
        this.message = String.format("Usuário %s está te desafiando para uma simulação", challengerName);
        this.players = players;
    }

    
}
