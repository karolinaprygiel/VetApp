package uj.jwzp2021.gp.VetApp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uj.jwzp2021.gp.VetApp.exception.animal.AnimalNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.client.ClientNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.office.OfficeNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.user.UserAlreadyExistsException;
import uj.jwzp2021.gp.VetApp.exception.vet.VetNotAvailableException;
import uj.jwzp2021.gp.VetApp.exception.vet.VetNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.visit.VisitNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.visit.VisitOverlapsException;
import uj.jwzp2021.gp.VetApp.exception.visit.VisitStartsInPastException;
import uj.jwzp2021.gp.VetApp.exception.visit.VisitTooSoonException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(VeterinaryAppException.class)
  protected ResponseEntity<?> handleVeterinaryAppException(
      VeterinaryAppException ex, WebRequest request) {
    return handleExceptionInternal(
        ex,
        JsonFormatter.toResponseJson(ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        request);
  }

  @ExceptionHandler(AnimalNotFoundException.class)
  protected ResponseEntity<?> handleAnimalNotFoundException(
      AnimalNotFoundException ex, WebRequest request) {
    log.error("Returning httpStatus=404. Reason: " + ex.getMessage());

    return handleExceptionInternal(
        ex,
        JsonFormatter.toResponseJson(ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.NOT_FOUND,
        request);
  }

  @ExceptionHandler(OfficeNotFoundException.class)
  protected ResponseEntity<?> handleOfficeNotFoundException(
      OfficeNotFoundException ex, WebRequest request) {
    log.error("Returning httpStatus=404. Reason: " + ex.getMessage());
    return handleExceptionInternal(
        ex,
        JsonFormatter.toResponseJson(ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.NOT_FOUND,
        request);
  }

  @ExceptionHandler(ClientNotFoundException.class)
  protected ResponseEntity<?> handleClientNotFoundException(
      ClientNotFoundException ex, WebRequest request) {
    log.error("Returning httpStatus=404. Reason: " + ex.getMessage());
    return handleExceptionInternal(
        ex,
        JsonFormatter.toResponseJson(ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.NOT_FOUND,
        request);
  }

  @ExceptionHandler(VetNotFoundException.class)
  protected ResponseEntity<?> handleVetNotFoundException(
      VetNotFoundException ex, WebRequest request) {
    log.error("Returning httpStatus=404. Reason: " + ex.getMessage());
    return handleExceptionInternal(
        ex,
        JsonFormatter.toResponseJson(ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.NOT_FOUND,
        request);
  }

  @ExceptionHandler(VisitNotFoundException.class)
  protected ResponseEntity<?> handleVisitNotFoundException(
      VisitNotFoundException ex, WebRequest request) {
    log.error("Returning httpStatus=404. Reason: " + ex.getMessage());
    return handleExceptionInternal(
        ex,
        JsonFormatter.toResponseJson(ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.NOT_FOUND,
        request);
  }

  @ExceptionHandler(VisitOverlapsException.class)
  protected ResponseEntity<?> handleVisitOverlapsException(
      VisitOverlapsException ex, WebRequest request) {
    log.error("Returning httpStatus=409. Reason: " + ex.getMessage());
    return handleExceptionInternal(
        ex,
        JsonFormatter.toResponseJson(ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.CONFLICT,
        request);
  }

  @ExceptionHandler(VisitStartsInPastException.class)
  protected ResponseEntity<?> handleVisitStartsInPastException(
      VisitStartsInPastException ex, WebRequest request) {
    log.error("Returning httpStatus=406. Reason: " + ex.getMessage());
    return handleExceptionInternal(
        ex,
        JsonFormatter.toResponseJson(ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.NOT_ACCEPTABLE,
        request);
  }

  @ExceptionHandler(VisitTooSoonException.class)
  protected ResponseEntity<?> handleVisitTooSoonException(
      VisitTooSoonException ex, WebRequest request) {
    log.error("Returning httpStatus=406. Reason: " + ex.getMessage());
    return handleExceptionInternal(
        ex,
        JsonFormatter.toResponseJson(ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.NOT_ACCEPTABLE,
        request);
  }

  @ExceptionHandler(VetNotAvailableException.class)
  protected ResponseEntity<?> VetNotAvailableException(
      VetNotAvailableException ex, WebRequest request) {
    log.error("Returning httpStatus=406. Reason: " + ex.getMessage());
    return handleExceptionInternal(
        ex,
        JsonFormatter.toResponseJson(ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.NOT_ACCEPTABLE,
        request);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  protected ResponseEntity<?> handleUserAlreadyExistsException(
          UserAlreadyExistsException ex, WebRequest request) {
    log.error("Returning httpStatus=406. Reason: " + ex.getMessage());
    return handleExceptionInternal(
            ex,
            JsonFormatter.toResponseJson(ex.getMessage()),
            new HttpHeaders(),
            HttpStatus.NOT_ACCEPTABLE,
            request);
  }
}
