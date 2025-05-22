package com.nexusfc.api.HttpException;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nexusfc.api.Auth.Exception.EmailAlreadyInUseException;
import com.nexusfc.api.Common.NotFoundException;
import com.nexusfc.api.Market.Exception.InsufficientBalance;
import com.nexusfc.api.Market.Exception.PlayerAlreadyOwnedException;
import com.nexusfc.api.Market.Exception.PlayerNotInTeamException;
import com.nexusfc.api.Simulation.Exception.IncompleteTeamException;
import com.nexusfc.api.Simulation.Exception.InvalidSimulationStateException;
import com.nexusfc.api.Simulation.Exception.UserIsNotInThisSimulationException;


@RestControllerAdvice
public class ExceptionsHandler {

    Logger LOG = LoggerFactory.getLogger(ExceptionsHandler.class);

    private ProblemDetail getProblemDetail(HttpStatusCode statusCode) {
        return ProblemDetail.forStatus(statusCode);
    }

    @ExceptionHandler({RuntimeException.class})
    public ProblemDetail handleInternalServerErrorException(RuntimeException ex) {
        LOG.info("\n <-----------------> INTERNAL SERVER ERROR <-----------------> \n Message: {} \n Cause: {} \n Localized: {} \n Class: {} \n <----------------->", ex.getMessage(), ex.getCause(), ex.getLocalizedMessage(), ex.getClass());
        
        ProblemDetail pd = getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Internal Server Error");
        pd.setType(URI.create("Nexusfc/internal-server-error"));
        pd.setDetail("Internal Server Error");

        return pd;
    }

    @ExceptionHandler({EmailAlreadyInUseException.class})
    public ProblemDetail handleConflictException(RuntimeException ex) {
        ProblemDetail pd = getProblemDetail(HttpStatus.CONFLICT);
        pd.setTitle("Conflict");
        pd.setType(URI.create("Nexusfc/conflict"));
        pd.setDetail(ex.getMessage());

        return pd;
    }

    @ExceptionHandler({NotFoundException.class})
    public ProblemDetail handleNotFoundException(RuntimeException ex) {
        ProblemDetail pd = getProblemDetail(HttpStatus.NOT_FOUND);
        pd.setTitle("Not Found");
        pd.setType(URI.create("Nexusfc/not-found"));
        pd.setDetail(ex.getMessage());

        return pd;
    }

    @ExceptionHandler({InsufficientBalance.class, PlayerAlreadyOwnedException.class, PlayerNotInTeamException.class, IncompleteTeamException.class, InvalidSimulationStateException.class})
    public ProblemDetail handleUnprocessableEntityException(RuntimeException ex) {
        ProblemDetail pd = getProblemDetail(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setTitle("Unprocessable Entity");
        pd.setType(URI.create("Nexusfc/unprocessable-entity"));
        pd.setDetail(ex.getMessage());

        return pd;
    }

    @ExceptionHandler({UserIsNotInThisSimulationException.class})
    public ProblemDetail handleUnauthorized(RuntimeException ex) {
        ProblemDetail pd = getProblemDetail(HttpStatus.UNAUTHORIZED);
        pd.setTitle("Unauthorized");
        pd.setType(URI.create("Nexusfc/unauthorized"));
        pd.setDetail(ex.getMessage());

        return pd;
    }
}
