package uj.jwzp2021.gp.VetApp.exception;

public class AnimalNotFoundException extends VeterinaryAppException{
  public AnimalNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public AnimalNotFoundException(String message) {
    super(message);
  }
}
