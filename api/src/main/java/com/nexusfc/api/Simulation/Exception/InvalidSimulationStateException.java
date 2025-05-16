package com.nexusfc.api.Simulation.Exception;

import com.nexusfc.api.Model.Enum.SimulationStatus;

public class InvalidSimulationStateException extends RuntimeException {
    public InvalidSimulationStateException(String simulationId, SimulationStatus currentStatus) {
        super(String.format(
                "Cannot accept simulation %s: current status is %s, but expected REQUESTED",
                simulationId, currentStatus.name()));
    }
}
