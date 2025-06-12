package com.nexusfc.api.Repository;

import com.nexusfc.api.Model.ProfessionalPlayer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.nexusfc.api.Model.ProfessionalTeam;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalPlayersRepository extends MongoRepository<ProfessionalPlayer, String> {
    ProfessionalPlayer findByNick(String nick);

    Page<ProfessionalPlayer> findByLane(String lane, Pageable pageable);

    List<ProfessionalPlayer> findByTeam(ProfessionalTeam team);
}
