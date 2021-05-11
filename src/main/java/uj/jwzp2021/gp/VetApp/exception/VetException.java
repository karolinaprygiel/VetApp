package uj.jwzp2021.gp.VetApp.exception;

public class VetException extends RuntimeException {

  public VetException(String message, Throwable cause) {
    super(message, cause);
  }

  public VetException(String message) {
    super(message);
  }
}
