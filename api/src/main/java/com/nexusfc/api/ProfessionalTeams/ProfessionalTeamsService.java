package com.nexusfc.api.ProfessionalTeams;

import java.util.List;
import java.util.Optional;

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

    public Page<ProfessionalTeam> getProfessionalTeams(Pageable pageable) {
        return teamsRepository.findAll(pageable);
    }

    public List<ProfessionalPlayer> getTeamAndPlayers(String teamName) {
        ProfessionalTeam team = Optional.ofNullable(teamsRepository.findByName(teamName)).orElseThrow(() -> new NotFoundException("Team with name " + teamName));

        return playersRepository.findByTeam(team);
    }
}
