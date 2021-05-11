package uj.jwzp2021.gp.VetApp.exception;

public class VisitStartsInPastException extends VeterinaryAppException {

  public VisitStartsInPastException(String message, Throwable cause) {
    super(message, cause);
  }

  public VisitStartsInPastException(String message) {
    super(message);
  }
}
