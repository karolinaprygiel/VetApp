package uj.jwzp2021.gp.VetApp.exception;

public class AnimalNotExistsException extends VeterinaryAppException {

  public AnimalNotExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public AnimalNotExistsException(String message) {
    super(message);
  }
}