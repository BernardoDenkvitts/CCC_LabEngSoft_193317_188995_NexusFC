package com.nexusfc.api.User.Service;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexusfc.api.Common.NotFoundException;
import com.nexusfc.api.Market.Exception.PlayerNotInTeamException;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.ProfessionalPlayers.ProfessionalPlayersService;
import com.nexusfc.api.ProfessionalTeams.ProfessionalTeamsService;
import com.nexusfc.api.Repository.UserTeamsRepository;
import com.nexusfc.api.User.Exception.TeamNameAlreadyInUserException;

@Service
public class UserTeamService {

    private final UserTeamsRepository userTeamsRepository;
    private final ProfessionalPlayersService professionalPlayersService;

    public UserTeamService(UserTeamsRepository userTeamsRepository, ProfessionalPlayersService professionalPlayersService) {
        this.userTeamsRepository = userTeamsRepository;
        this.professionalPlayersService = professionalPlayersService;
    }

    @Transactional
    public UserTeam save(UserTeam userTeam) {
        return userTeamsRepository.save(userTeam);
    }

    public UserTeam find(String userId) {
        return Optional.ofNullable(userTeamsRepository.findByUserId(new ObjectId(userId)))
                .orElseThrow(() -> new NotFoundException("User with id " + userId));
    }

    @Transactional
    public UserTeam updateTeamName(String userId, String newTeamName) {
        UserTeam team = find(userId);

        if (userTeamsRepository.findByName(newTeamName) != null)
            throw new TeamNameAlreadyInUserException();

        team.setName(newTeamName);
        userTeamsRepository.save(team);

        return team;
    }

    @Transactional
    public UserTeam changeStarterPlayer(String userId, String playerId) {
        UserTeam userTeam = find(userId);   
        professionalPlayersService.getProfessionalPlayerById(playerId);

        if (!userTeam.hasPlayer(playerId))
            throw new PlayerNotInTeamException(playerId);

        userTeam.setStarter(playerId);

        return save(userTeam);
    }
}
