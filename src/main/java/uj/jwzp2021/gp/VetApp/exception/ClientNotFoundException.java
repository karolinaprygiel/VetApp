package uj.jwzp2021.gp.VetApp.exception;

public class ClientNotFoundException extends VeterinaryAppException {

  public ClientNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ClientNotFoundException(String message) {
    super(message);
  }
}