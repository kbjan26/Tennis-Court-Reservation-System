package com.yieldbroker.sportscenter.exception;

import com.yieldbroker.sportscenter.domain.CourtErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CourtAssignmentExceptionHandler {

    @ExceptionHandler(CourtAlreadyAssignedException.class)
    @ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
    public @ResponseBody
    CourtErrorResponse handleCourtAlreadyAssigned(final CourtAlreadyAssignedException exception) {

        CourtErrorResponse error = new CourtErrorResponse();
        error.setErrorMessage(exception.getMessage());
        return error;
    }

    @ExceptionHandler(CourtNotAvailableException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public @ResponseBody CourtErrorResponse handleCourtNotAvailable(final CourtNotAvailableException exception) {

        CourtErrorResponse error = new CourtErrorResponse();
        error.setErrorMessage(exception.getMessage());

        return error;
    }
}

