package uj.jwzp2021.gp.VetApp.exception;

public class VetNotFoundException extends VeterinaryAppException{

  public VetNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public VetNotFoundException(String message) {
    super(message);
  }
}
