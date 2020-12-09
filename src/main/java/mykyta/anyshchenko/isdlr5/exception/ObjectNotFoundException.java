package mykyta.anyshchenko.isdlr5.exception;

import org.flywaydb.core.api.ErrorCode;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
