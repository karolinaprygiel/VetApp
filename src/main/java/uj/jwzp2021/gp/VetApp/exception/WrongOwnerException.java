package uj.jwzp2021.gp.VetApp.exception;

public class WrongOwnerException  extends VeterinaryAppException {

  public WrongOwnerException(String message, Throwable cause) {
    super(message, cause);
  }

  public WrongOwnerException(String message) {
    super(message);
  }
}
