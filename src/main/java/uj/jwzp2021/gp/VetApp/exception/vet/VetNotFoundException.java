package uj.jwzp2021.gp.VetApp.exception.vet;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class VetNotFoundException extends VeterinaryAppException {

  public VetNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public VetNotFoundException(String message) {
    super(message);
  }
}
