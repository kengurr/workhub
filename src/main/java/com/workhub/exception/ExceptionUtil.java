package com.workhub.exception;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ExceptionUtil {

    public void logErrorAndThrow(WorkhubException workhubException)
            throws ServiceProcessingException {
        log.error(getLogErrorMessage(workhubException));
        throwException(workhubException);
    }

    public ServiceProcessingException logAndBuildException(
            WorkhubException workhubException) {
        log.error(getLogErrorMessage(workhubException));
        return new ServiceProcessingException(
                workhubException.getHttp(),
                workhubException.getHttp(),
                workhubException.getType(),
                workhubException.getMessage());
    }

    public void logErrorAndThrowWithCause(
            WorkhubException workhubException, Throwable cause)
            throws ServiceProcessingException {
        log.error(getLogErrorMessage(workhubException), cause);
        throwExceptionWithCause(workhubException, cause);
    }

    private void throwException(WorkhubException workhubException) {
        throw new ServiceProcessingException(
                workhubException.getHttp(),
                workhubException.getHttp(),
                workhubException.getType(),
                workhubException.getMessage());
    }

    private void throwExceptionWithCause(
            WorkhubException workhubException, Throwable cause)
            throws ServiceProcessingException {
        throw new ServiceProcessingException(
                workhubException.getHttp(),
                workhubException.getHttp(),
                workhubException.getType(),
                workhubException.getMessage(),
                cause);
    }

    private String getLogErrorMessage(WorkhubException workhubException) {
        return workhubException.getCode()
                + " - "
                + workhubException.getMessage();
    }
}
