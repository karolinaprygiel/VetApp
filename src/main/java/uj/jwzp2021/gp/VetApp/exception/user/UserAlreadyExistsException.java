package uj.jwzp2021.gp.VetApp.exception.user;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class UserAlreadyExistsException extends VeterinaryAppException {
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}