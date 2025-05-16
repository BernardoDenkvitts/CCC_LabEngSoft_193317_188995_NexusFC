package com.nexusfc.api.Simulation.Dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.nexusfc.api.Model.Simulation;
import com.nexusfc.api.Model.Enum.SimulationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulationResponseDTO {
    private String id;
    private Boolean versusPlayer;
    private String challengerId;
    private String challengedId;
    private SimulationStatus status;
    private Float betValue;
    private Instant createdAt;
    private Boolean win;
    private List<ProfessionalPlayerResponseDTO> challengerTeam;
    private List<ProfessionalPlayerResponseDTO> challengedTeam;

    public static SimulationResponseDTO from(Simulation simulation) {
        return new SimulationResponseDTO(
            simulation.getId(),
            simulation.getVersusPlayer(),
            simulation.getDesafiante().getId(),
            simulation.getDesafiado().toHexString(),
            simulation.getStatus(),
            simulation.getBetValue(),
            simulation.getCreatedAt(),
            simulation.getWin(),
            simulation.getDesafianteTeamPlayers().stream().map(ProfessionalPlayerResponseDTO::from).toList(),
            simulation.getDesafiadoTeamPlayers() != null ? simulation.getDesafiadoTeamPlayers().stream().map(ProfessionalPlayerResponseDTO::from).toList() : new ArrayList<>()
        );
    }
}
