package uj.jwzp2021.gp.VetApp.exception;

public class VetNotExistsException extends VeterinaryAppException {

  public VetNotExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public VetNotExistsException(String message) {
    super(message);
  }
}
