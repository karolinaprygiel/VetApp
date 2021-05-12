package uj.jwzp2021.gp.VetApp.exception.visit;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class VisitOverlapsException extends VeterinaryAppException {

  public VisitOverlapsException(String message, Throwable cause) {
    super(message, cause);
  }

  public VisitOverlapsException(String message) {
    super(message);
  }
}