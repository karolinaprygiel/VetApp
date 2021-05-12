package uj.jwzp2021.gp.VetApp.exception.visit;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class VisitTooSoonException extends VeterinaryAppException {

  public VisitTooSoonException(String message, Throwable cause) {
    super(message, cause);
  }

  public VisitTooSoonException(String message) {
    super(message);
  }
}
