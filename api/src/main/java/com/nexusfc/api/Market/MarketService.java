package com.nexusfc.api.Market;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexusfc.api.Market.Dto.TransactionResponseDTO;
import com.nexusfc.api.Market.Exception.InsufficientBalance;
import com.nexusfc.api.Market.Exception.PlayerAlreadyOwnedException;
import com.nexusfc.api.Market.Exception.PlayerNotInTeamException;
import com.nexusfc.api.Model.ProfessionalPlayer;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Model.Component.ProfessionalPlayerEntry;
import com.nexusfc.api.ProfessionalPlayers.ProfessionalPlayersService;
import com.nexusfc.api.User.Service.UserService;
import com.nexusfc.api.User.Service.UserTeamService;

@Service
public class MarketService {

    private final UserService userService;
    private final UserTeamService userTeamService;
    private final ProfessionalPlayersService playersService;

    public MarketService(UserService userService, UserTeamService userTeamService,
            ProfessionalPlayersService playersService) {
        this.userService = userService;
        this.userTeamService = userTeamService;
        this.playersService = playersService;
    }

    @Transactional
    public TransactionResponseDTO buyPlayer(String userId, String playerId) {
        ProfessionalPlayer player = playersService.getProfessionalPlayerById(playerId);
        User user = userService.getUserData(userId);
        UserTeam team = userTeamService.getUserTeam(userId);

        if (team.hasPlayer(playerId))
            throw new PlayerAlreadyOwnedException();

        if (!user.hasEnoughCoins(player.getCost()))
            throw new InsufficientBalance(user.getCoins(), player.getCost());

        team.addPlayer(new ProfessionalPlayerEntry(player));
        user.decreaseCoins(player.getCost());

        userTeamService.save(team);
        userService.save(user);

        return new TransactionResponseDTO(user.getCoins(), team);
    }

    @Transactional
    public TransactionResponseDTO sellPlayer(String userId, String playerId) {
        ProfessionalPlayer player = playersService.getProfessionalPlayerById(playerId);
        User user = userService.getUserData(userId);
        UserTeam team = userTeamService.getUserTeam(userId);

        if (!team.hasPlayer(playerId))
            throw new PlayerNotInTeamException(playerId);

        team.removePlayer(team.getPlayerEntry(playerId));
        user.increaseCoins(player.getCost());
        
        userTeamService.save(team);
        userService.save(user);
        
        return new TransactionResponseDTO(user.getCoins(), team);
    }

}
