package com.nexusfc.api.ProfessionalTeams;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nexusfc.api.Common.NotFoundException;
import com.nexusfc.api.Model.ProfessionalPlayer;
import com.nexusfc.api.Model.ProfessionalTeam;
import com.nexusfc.api.Repository.ProfessionalPlayersRepository;
import com.nexusfc.api.Repository.ProfessionalTeamsRepository;

@Service
public class ProfessionalTeamsService {

    private final ProfessionalTeamsRepository teamsRepository;
    private final ProfessionalPlayersRepository playersRepository;

    public ProfessionalTeamsService(ProfessionalTeamsRepository teamsRepository,
            ProfessionalPlayersRepository playersRepository) {
        this.teamsRepository = teamsRepository;
        this.playersRepository = playersRepository;
    }

    public ProfessionalTeam find(String id) {
        return teamsRepository.findById(id).orElseThrow(() -> new NotFoundException("Team with id " + id));
    }

    public Page<ProfessionalTeam> getProfessionalTeams(Pageable pageable) {
        return teamsRepository.findAll(pageable);
    }

    public List<ProfessionalPlayer> getTeamPlayers(String id) {
        ProfessionalTeam team = find(id);

        return playersRepository.findByTeam(team);
    }
}
