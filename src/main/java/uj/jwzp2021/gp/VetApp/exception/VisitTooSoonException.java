package uj.jwzp2021.gp.VetApp.exception;

public class VisitTooSoonException extends VeterinaryAppException {

  public VisitTooSoonException(String message, Throwable cause) {
    super(message, cause);
  }

  public VisitTooSoonException(String message) {
    super(message);
  }
}
