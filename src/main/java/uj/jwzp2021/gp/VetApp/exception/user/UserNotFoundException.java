package uj.jwzp2021.gp.VetApp.exception.user;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class UserNotFoundException extends VeterinaryAppException {
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
