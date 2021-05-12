package uj.jwzp2021.gp.VetApp.exception.office;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class OfficeNotFoundException extends VeterinaryAppException {
  public OfficeNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public OfficeNotFoundException(String message) {
    super(message);
  }
}
