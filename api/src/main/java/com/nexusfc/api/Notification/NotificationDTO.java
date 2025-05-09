package com.nexusfc.api.Notification;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import com.nexusfc.api.Model.ProfessionalPlayer;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Simulation.Dto.ProfessionalPlayerResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDTO {
    private Instant instant;
    private String message;
    private String challengerId;
    private String challengerName;
    private String teamName;
    private List<ProfessionalPlayerResponseDTO> players;

    public NotificationDTO(String challengerId, String challengerName, String teamName, Instant instant, List<ProfessionalPlayerResponseDTO> players) {
        this.challengerId = challengerId;
        this.challengerName = challengerName;
        this.teamName = teamName;
        this.instant = instant;
        this.message = String.format("User %s is challenging you for a simulation", challengerName);
        this.players = players;
    }

    
}
