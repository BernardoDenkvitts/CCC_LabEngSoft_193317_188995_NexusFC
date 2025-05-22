package com.nexusfc.api.Simulation;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nexusfc.api.Model.Simulation;
import com.nexusfc.api.Simulation.Dto.SimulationCreatedResponseDTO;
import com.nexusfc.api.Simulation.Dto.SimulationRequestDTO;
import com.nexusfc.api.Simulation.Dto.SimulationResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/simulations")
public class SimulationController {

    private final SimulationService service;

    public SimulationController(SimulationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SimulationCreatedResponseDTO> createSimulation(@RequestBody SimulationRequestDTO request) {
        Simulation simulation = service.create(request.getChallengerId(), request.getChallengedId(),
                request.getVersusPlayer(), request.getBetValue());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(simulation.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(SimulationCreatedResponseDTO.create(simulation, simulation.getDesafiante().getCoins()));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<Page<SimulationResponseDTO>> getHistory(@PathVariable String userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Simulation> history = service.getSimulationHistory(userId, pageable);
        
        return ResponseEntity.ok(history.map(SimulationResponseDTO::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimulationResponseDTO> getSimulation(@PathVariable String id, @RequestParam(required = true, name = "userId") String userId) {
        return ResponseEntity.ok(SimulationResponseDTO.from(service.getSimulationById(id, userId)));
    }   
    
    @GetMapping("/{id}/accept")
    public ResponseEntity<SimulationResponseDTO> acceptSimulationPVP(@PathVariable String id, @RequestParam(required = true) String challengedId) {
        Simulation simulation = service.accept(id, challengedId);

        return ResponseEntity.ok(SimulationResponseDTO.from(simulation));
    }

    @GetMapping("/{id}/start")
    public ResponseEntity<SimulationResponseDTO> startSimulationPVE(@PathVariable String id, @RequestParam String challengerId) {
        Simulation simulation = service.startPveSimulation(id, challengerId);

        return ResponseEntity.ok(SimulationResponseDTO.from(simulation));
    }
    
    @GetMapping("/{id}/reject")
    public ResponseEntity<SimulationResponseDTO> rejectSimulationPVP(@PathVariable String id, @RequestParam(required = true) String challengedId) {
        Simulation simulation = service.reject(id, challengedId);

        return ResponseEntity.ok(SimulationResponseDTO.from(simulation));
    }

}
