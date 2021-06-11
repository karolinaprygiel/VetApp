package uj.jwzp2021.gp.VetApp.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;

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

  public String generateJWT(UserDetails userDetails) {
    try {
      JWSSigner signer = new MACSigner(secret.getBytes(StandardCharsets.UTF_8));
      JWTClaimsSet jwtClaimsSet =
          new JWTClaimsSet.Builder()
              .claim("username", userDetails.getUsername())
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

  public String extractUsername(String token) {
    SignedJWT decodedJWT;
    try{
      decodedJWT = SignedJWT.parse(token);
      return (String) decodedJWT.getJWTClaimsSet().getClaim("username");
    } catch (Exception e) {
      log.info("Unable to extract username");
    }
    throw new VeterinaryAppException("username not found in token");
  }

  public boolean isJwtValid(String token) {
    SignedJWT signedJWT;
    try{
      signedJWT = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(secret.getBytes());
      if (signedJWT.verify(verifier)) {
        return true;
      }
    } catch (Exception e) {
      log.info("token invalid");
      return false;
    }
    return false;
  }
}
