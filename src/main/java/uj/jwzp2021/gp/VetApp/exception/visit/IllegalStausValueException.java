package uj.jwzp2021.gp.VetApp.exception.visit;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class IllegalStausValueException extends VeterinaryAppException {

  public IllegalStausValueException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalStausValueException(String message) {
    super(message);
  }

  public IllegalStausValueException() {
    super("Visit not found.");
  }

}
