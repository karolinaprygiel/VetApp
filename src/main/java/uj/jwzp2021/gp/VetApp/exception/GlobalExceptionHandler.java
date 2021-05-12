package uj.jwzp2021.gp.VetApp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uj.jwzp2021.gp.VetApp.controller.rest.RestUtil;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(VeterinaryAppException.class)
  protected ResponseEntity<?> handleVeterinaryAppException(VeterinaryAppException ex, WebRequest request) {
    return handleExceptionInternal(ex, RestUtil.response(ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  @ExceptionHandler(AnimalNotFoundException.class)
  protected ResponseEntity<?> handleAnimalNotFoundException(AnimalNotFoundException ex, WebRequest request) {
    return handleExceptionInternal(ex, RestUtil.response(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(ClientNotFoundException.class)
  protected ResponseEntity<?> handleClientNotFoundException(ClientNotFoundException ex, WebRequest request) {
    return handleExceptionInternal(ex, RestUtil.response(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(VetNotFoundException.class)
  protected ResponseEntity<?> handleVetNotFoundException(VetNotFoundException ex, WebRequest request) {
    return handleExceptionInternal(ex, RestUtil.response(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(VisitNotFoundException.class)
  protected ResponseEntity<?> handleVisitNotFoundException(VisitNotFoundException ex, WebRequest request) {
    return handleExceptionInternal(ex, RestUtil.response(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(VisitOverlapsException.class)
  protected ResponseEntity<?> handleVisitOverlapsException(VisitOverlapsException ex, WebRequest request) {
    return handleExceptionInternal(ex, RestUtil.response(ex.getMessage()), new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(VisitStartsInPastException.class)
  protected ResponseEntity<?> handleVisitStartsInPastException(VisitStartsInPastException ex, WebRequest request) {
    return handleExceptionInternal(ex, RestUtil.response(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
  }

  @ExceptionHandler(VisitTooSoonException.class)
  protected ResponseEntity<?> handleVisitTooSoonException(VisitTooSoonException ex, WebRequest request) {
    return handleExceptionInternal(ex, RestUtil.response(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
  }

}