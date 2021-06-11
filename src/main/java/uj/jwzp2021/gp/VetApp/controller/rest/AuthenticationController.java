package uj.jwzp2021.gp.VetApp.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AuthenticationRequest;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.AuthenticationResponse;
import uj.jwzp2021.gp.VetApp.security.MyUserDetailsService;
import uj.jwzp2021.gp.VetApp.service.JWTService;

@RestController
@Slf4j
public class AuthenticationController {

  @Autowired JWTService jwtService;

  @Autowired AuthenticationManager authenticationManager;

  @Autowired MyUserDetailsService myUserDetailsService;

  @GetMapping("/hello")
  public ResponseEntity<?> hello() {
    return ResponseEntity.ok("Hello World! This endpoint is available to everyone!");
  }

  @GetMapping("/hello-vet")
  public ResponseEntity<?> helloVet() {
    return ResponseEntity.ok("Hello World! This endpoint is only available to vets.");
  }

  @GetMapping("/hello-admin")
  public ResponseEntity<?> helloAdmin() {
    return ResponseEntity.ok("Hello World! This endpoint is only available to admins.");
  }

  @GetMapping("/hello-client")
  public ResponseEntity<?> helloClient() {
    return ResponseEntity.ok("Hello World! This endpoint is only available to clients.");
  }

  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthentacationToken(
      @RequestBody AuthenticationRequest authenticationRequest) {

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authenticationRequest.getUsername(), authenticationRequest.getPassword()));
    } catch (BadCredentialsException e) {
      log.info(e.getMessage());
    }

    final UserDetails userDetails =
        myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    String jwt = jwtService.generateJWT(userDetails);
    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }
}
