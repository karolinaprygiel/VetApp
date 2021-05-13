package uj.jwzp2021.gp.VetApp.exception.vet;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class VetNotAvailableException extends VeterinaryAppException {
  public VetNotAvailableException(String message, Throwable cause) {
    super(message, cause);
  }

  public VetNotAvailableException(String message) {
    super(message);
  }
}
