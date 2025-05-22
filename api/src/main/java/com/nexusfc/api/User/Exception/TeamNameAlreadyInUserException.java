package com.nexusfc.api.User.Exception;

public class TeamNameAlreadyInUserException extends RuntimeException {
    public TeamNameAlreadyInUserException() {
        super("Team name already in use");
    }
}
