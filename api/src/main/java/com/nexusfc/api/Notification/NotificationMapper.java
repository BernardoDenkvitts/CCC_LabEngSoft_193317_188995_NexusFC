package com.nexusfc.api.Notification;

import java.time.Instant;
import java.util.List;

import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Simulation.Dto.ProfessionalPlayerResponseDTO;

public class NotificationMapper {

    public static NotificationDTO toDto(User challenger, UserTeam challengerTeam, Instant createdAt) {
        List<ProfessionalPlayerResponseDTO> playerDtos = challengerTeam.getStarterPlayers()
                .stream()
                .map(ProfessionalPlayerResponseDTO::from)
                .toList();

        NotificationDTO dto = new NotificationDTO(challenger.getId(), challenger.getName(), challengerTeam.getName(),
                createdAt, playerDtos);

        return dto;
    }
}
