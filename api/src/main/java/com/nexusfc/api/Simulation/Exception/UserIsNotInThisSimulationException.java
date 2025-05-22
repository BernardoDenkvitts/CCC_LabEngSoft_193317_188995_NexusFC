package com.nexusfc.api.Simulation.Exception;

public class UserIsNotInThisSimulationException extends RuntimeException {
    public UserIsNotInThisSimulationException() {
        super("User is not in this simulation");
    }
}
