package uj.jwzp2021.gp.VetApp.exception.animal;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class AnimalNotFoundException extends VeterinaryAppException {
  public AnimalNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public AnimalNotFoundException(String message) {
    super(message);
  }
}
