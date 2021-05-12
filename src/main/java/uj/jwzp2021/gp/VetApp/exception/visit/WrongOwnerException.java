package uj.jwzp2021.gp.VetApp.exception.visit;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class WrongOwnerException  extends VeterinaryAppException {

  public WrongOwnerException(String message, Throwable cause) {
    super(message, cause);
  }

  public WrongOwnerException(String message) {
    super(message);
  }
}
