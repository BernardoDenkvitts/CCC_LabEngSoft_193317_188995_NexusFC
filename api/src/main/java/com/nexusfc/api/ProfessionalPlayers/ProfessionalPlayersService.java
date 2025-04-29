package com.nexusfc.api.ProfessionalPlayers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nexusfc.api.Common.NotFoundException;
import com.nexusfc.api.Model.ProfessionalPlayer;
import com.nexusfc.api.Repository.ProfessionalPlayersRepository;


@Service
public class ProfessionalPlayersService {

    private final ProfessionalPlayersRepository playersRepository;

    public ProfessionalPlayersService(ProfessionalPlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    public Page<ProfessionalPlayer> getProfessionalPlayers(Pageable pageable) {
        return playersRepository.findAll(pageable);
    }

    public ProfessionalPlayer getProfessionalPlayerById(String id) {
        return playersRepository.findById(id).orElseThrow(() -> new NotFoundException("Player with id " + id));
    }
}
