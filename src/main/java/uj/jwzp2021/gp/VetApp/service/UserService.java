package uj.jwzp2021.gp.VetApp.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;
import uj.jwzp2021.gp.VetApp.exception.user.UserNotFoundException;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.UserCreationRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.User;
import uj.jwzp2021.gp.VetApp.repository.UserRepository;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Service
@Slf4j
public class UserService {

  private final int iterations = 1337;
  private final int keyLength = 512;
  private final String pepper;

  private final UserRepository userRepository;
  private final SaltService saltService;

  @Autowired
  public UserService(UserRepository userRepository, @Value("${app.security.pepper}") String pepper, SaltService saltService) {
    this.userRepository = userRepository;
    this.pepper = pepper;
    this.saltService = saltService;
  }

  public User getUserById(int id) {
    log.info("Looking up user with id=" + id);
    var user = userRepository.findById(id);
    return user.orElseThrow(
        () -> {
          throw new UserNotFoundException("User with id=" + id + " not found");
        });
  }

  public User createUser(UserCreationRequestDto request) {
    log.info("Creating user for: " + request);
    String salt = saltService.generateSalt();
    String password = request.getPassword();
    String hashedPassword = combineAndHash(salt, password);
    User user;
    try {
      String name = request.getUsername();
      user =
          userRepository.save(
              User.newUser(name, hashedPassword, salt, request.getRole()));
    } catch (DataAccessException ex) {
      log.error("Repository problem while saving user for request: " + request);
      throw ex;
    }
    log.info("User created.");
    return user;
  }

  byte[] hashPassword(
      final char[] password, final byte[] salt, final int iterations, final int keyLength) {
    try {
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
      PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
      SecretKey key = skf.generateSecret(spec);
      return key.getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new VeterinaryAppException(e.getMessage());
    }
  }

  public User getUserByName(String name) {
    return userRepository.findUserByUsername(name).orElseThrow();
  }

  public List<User> getAll() {
    return userRepository.findAll();
  }

  public boolean checkPassword(String name, String password) {
    User user = userRepository.findUserByUsername(name).orElseThrow();
    String salt = user.getSalt();
    String hashedPassword = combineAndHash(salt, password);
    return hashedPassword.equals(user.getPassword());
  }

  private String combineAndHash(String salt, String password) {
    log.info("combining: salt:" + salt + ", password:" + password + ", pepper:" + pepper);
    char[] passwordChars = password.toCharArray();
    byte[] saltBytes = (salt + pepper).getBytes();
    byte[] hashedBytes = hashPassword(passwordChars, saltBytes, iterations, keyLength);
    return Hex.encodeHexString(hashedBytes);
  }
}
