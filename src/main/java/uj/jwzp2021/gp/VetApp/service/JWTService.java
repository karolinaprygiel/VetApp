package uj.jwzp2021.gp.VetApp.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.user.UserNotFoundException;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AuthenticationRequest;
import uj.jwzp2021.gp.VetApp.model.entity.User;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
public class JWTService {

  private final String secret;
  private final UserService userService;

  @Autowired
  public JWTService(@Value("${app.security.jwt.secret}") String secret, UserService userService) {
    this.secret = secret;
    this.userService = userService;
  }

  public String generateJWT(AuthenticationRequest request) {
    if (!userService.checkPassword(request.getUsername(), request.getPassword())) {
      log.info("Wrong password");
      throw new UserNotFoundException("Credentials are not valid.");
    }

    User user = userService.getUserByName(request.getUsername());
    try {
      JWSSigner signer = new MACSigner(secret.getBytes(StandardCharsets.UTF_8));
      JWTClaimsSet jwtClaimsSet =
          new JWTClaimsSet.Builder()
              .claim("login", user.getUsername())
              .claim("role", "ROLE_" + user.getRole())
              .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000))
              .build();
      SignedJWT signedJWT =
          new SignedJWT(
              new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(),
              jwtClaimsSet);
      signedJWT.sign(signer);

      return signedJWT.serialize();
    } catch (JOSEException e) {
      e.printStackTrace();
      throw new IllegalStateException();
    }
  }
}
