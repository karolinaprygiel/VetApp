package uj.jwzp2021.gp.VetApp.exception;

public class VeterinaryAppException extends RuntimeException {

  public VeterinaryAppException(String message, Throwable cause) {
    super(message, cause);
  }

  public VeterinaryAppException(String message) {
    super(message);
  }
}
