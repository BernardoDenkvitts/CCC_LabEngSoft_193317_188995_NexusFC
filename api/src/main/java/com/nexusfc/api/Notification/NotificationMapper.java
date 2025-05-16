package com.nexusfc.api.Notification;

import java.time.Instant;
import java.util.List;

import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Simulation.Dto.ProfessionalPlayerResponseDTO;

public class NotificationMapper {

    public static SimulationNotificationDTO toDto(User challenger, UserTeam challengerTeam, Instant createdAt) {
        List<ProfessionalPlayerResponseDTO> playerDtos = challengerTeam.getStarterPlayers()
                .stream()
                .map(ProfessionalPlayerResponseDTO::from)
                .toList();

        return new SimulationNotificationDTO(challenger.getId(), challenger.getName(), challengerTeam.getName(),
                createdAt, playerDtos);
    }
}
