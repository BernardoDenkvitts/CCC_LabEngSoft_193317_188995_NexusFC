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

@RestControllerAdvice
public class ExceptionsHandler {

    Logger LOG = LoggerFactory.getLogger(ExceptionsHandler.class);

    private ProblemDetail getProblemDetail(HttpStatusCode statusCode) {
        return ProblemDetail.forStatus(statusCode);
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleInternalServerErrorException(RuntimeException ex) {
        LOG.info("INTERNAL SERVER ERROR -> {} \n {} \n {} \n {} \n", ex.getMessage(), ex.getCause(), ex.getLocalizedMessage(), ex.getClass());
        
        ProblemDetail pd = getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Internal Server Error");
        pd.setType(URI.create("Nexusfc/internal-server-error"));
        pd.setDetail("Internal Server Error");

        return pd;
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ProblemDetail handleConflictException(RuntimeException ex) {
        ProblemDetail pd = getProblemDetail(HttpStatus.CONFLICT);
        pd.setTitle("Conflict");
        pd.setType(URI.create("Nexusfc/conflict"));
        pd.setDetail(ex.getMessage());

        return pd;
    }
}
