package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AuthenticationRequest;
import uj.jwzp2021.gp.VetApp.service.JWTService;

@RestController
public class AuthenticationController {

    JWTService jwtService;

    @Autowired
    public AuthenticationController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthentacationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        var result = jwtService.generateJWT(authenticationRequest);
        return ResponseEntity.ok(result);
    }
}
