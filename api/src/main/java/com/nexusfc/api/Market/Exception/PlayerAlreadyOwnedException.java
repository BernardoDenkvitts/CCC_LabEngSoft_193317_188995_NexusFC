package com.nexusfc.api.Market.Exception;

public class PlayerAlreadyOwnedException extends RuntimeException {
    public PlayerAlreadyOwnedException() {
        super("Player already owned");
    }
}
