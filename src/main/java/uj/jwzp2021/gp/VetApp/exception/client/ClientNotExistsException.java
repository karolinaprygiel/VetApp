package uj.jwzp2021.gp.VetApp.exception.client;

import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

// possible duplicate - ClientNotFoundException
public class ClientNotExistsException extends VeterinaryAppException {

  public ClientNotExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public ClientNotExistsException(String message) {
    super(message);
  }
}
