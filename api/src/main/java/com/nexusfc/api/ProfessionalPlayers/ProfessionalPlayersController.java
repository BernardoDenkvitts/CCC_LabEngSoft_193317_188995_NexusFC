package com.nexusfc.api.ProfessionalPlayers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexusfc.api.Model.ProfessionalPlayer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/professional/players")
public class ProfessionalPlayersController {
    private final ProfessionalPlayersService service;

    public ProfessionalPlayersController(ProfessionalPlayersService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ProfessionalPlayer>> getPlayers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        return ResponseEntity.ok(service.getProfessionalPlayers(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalPlayer> getPlayerById(@RequestParam String id) {
        return ResponseEntity.ok(service.getProfessionalPlayerById(id));
    }

}
