package com.nexusfc.api.Simulation.Dto;

import com.nexusfc.api.Model.ProfessionalPlayer;
import com.nexusfc.api.Model.Component.ProfessionalPlayerEntry;
import com.nexusfc.api.Model.Enum.Lane;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfessionalPlayerResponseDTO {
    private String id;
    private String nick;
    private Lane lane;
    private String imageUrl;

    public static ProfessionalPlayerResponseDTO from(ProfessionalPlayerEntry entry) {
        var player = entry.getPlayer();

        return new ProfessionalPlayerResponseDTO(player.getId(), player.getNick(), player.getLane(), player.getImageUrl());
    }

    public static ProfessionalPlayerResponseDTO from(ProfessionalPlayer player) {
        return new ProfessionalPlayerResponseDTO(player.getId(), player.getNick(), player.getLane(), player.getImageUrl());
    }
}
