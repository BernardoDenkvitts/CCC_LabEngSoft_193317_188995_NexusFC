package com.nexusfc.api.Simulation;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nexusfc.api.Model.Simulation;
import com.nexusfc.api.Simulation.Dto.SimulationRequestDTO;
import com.nexusfc.api.Simulation.Dto.SimulationResponseDTO;

@RestController
@RequestMapping("/simulations")
public class SimulationController {

    private final SimulationService service;

    public SimulationController(SimulationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SimulationResponseDTO> createSimulation(@RequestBody SimulationRequestDTO request) {
        Simulation simulation = service.create(request.getChallengerId(), request.getChallengedId(), request.getVersusPlayer(), request.getBetValue());
        SimulationResponseDTO responseDTO = SimulationResponseDTO.from(simulation);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(simulation.getId())
            .toUri();
        
        return ResponseEntity.created(location).body(responseDTO);
    }
}
