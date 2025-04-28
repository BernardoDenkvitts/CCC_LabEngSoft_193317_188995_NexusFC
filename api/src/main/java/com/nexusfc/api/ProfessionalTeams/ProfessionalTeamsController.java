package com.nexusfc.api.ProfessionalTeams;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexusfc.api.Model.ProfessionalTeam;
import com.nexusfc.api.Repository.ProfessionalTeamsRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/professional/teams")
public class ProfessionalTeamsController {
    private final ProfessionalTeamsRepository repository;

    public ProfessionalTeamsController(ProfessionalTeamsRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<Page<ProfessionalTeam>> getProfessionalTeams(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(repository.findAll(pageable));
    }
    
}
