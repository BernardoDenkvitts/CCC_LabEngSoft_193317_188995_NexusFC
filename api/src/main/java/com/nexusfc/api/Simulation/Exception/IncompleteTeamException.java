package com.nexusfc.api.Simulation.Exception;

public class IncompleteTeamException extends RuntimeException {
    public IncompleteTeamException() {
        super("It is necessary to have at least one starter in each lane (TOP, JUNGLE, MID, ADC and SUP)");
    }
}
