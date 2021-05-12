package uj.jwzp2021.gp.VetApp.exception.visit;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class VisitNotFoundException extends VeterinaryAppException {

  public VisitNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public VisitNotFoundException(String message) {
    super(message);
  }

  public VisitNotFoundException() {
    super("Visit not found.");
  }

}
