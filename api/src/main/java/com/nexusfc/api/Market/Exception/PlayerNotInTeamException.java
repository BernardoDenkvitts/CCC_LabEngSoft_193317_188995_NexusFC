package com.nexusfc.api.Market.Exception;

public class PlayerNotInTeamException extends RuntimeException {
    public PlayerNotInTeamException(String playerId) {
        super(String.format("Player with id %s is not in your team", playerId));
    }
}
