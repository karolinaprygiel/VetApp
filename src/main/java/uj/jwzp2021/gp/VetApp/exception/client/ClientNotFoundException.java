package uj.jwzp2021.gp.VetApp.exception.client;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

public class ClientNotFoundException extends VeterinaryAppException {

  public ClientNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ClientNotFoundException(String message) {
    super(message);
  }
}
