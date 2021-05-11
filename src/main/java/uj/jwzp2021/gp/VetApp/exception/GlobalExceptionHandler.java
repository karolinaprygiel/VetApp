package uj.jwzp2021.gp.VetApp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ClientNotFoundException.class)
  protected ResponseEntity<?> handleClientNotFoundException(ClientNotFoundException ex, WebRequest request) {
    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(AnimalNotFoundException.class)
  protected ResponseEntity<?> handleAnimalNotFoundException(AnimalNotFoundException ex, WebRequest request) {
    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(VetNotFoundException.class)
  protected ResponseEntity<?> handleVetNotFoundException(VetNotFoundException ex, WebRequest request) {
    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }





}
