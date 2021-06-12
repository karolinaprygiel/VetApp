package uj.jwzp2021.gp.VetApp.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.UserRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.UserCreationRequestDto;
import uj.jwzp2021.gp.VetApp.service.UserService;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserRestController {
  private final UserService userService;

  @Autowired
  public UserRestController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<?> createUser(@RequestBody UserCreationRequestDto request) {
    return ResponseEntity.ok(UserRepresentation.fromUser(userService.createUser(request)));
  }

  @GetMapping
  public ResponseEntity<?> getAllUsers() {
    var userRepresentations = userService.getAll().stream().map(UserRepresentation::fromUser);
    return ResponseEntity.ok(userRepresentations);
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getUser(@PathVariable int id) {
    log.info("Received GET request for /api/users/" + id);
    var user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping(path = "{id}")
  public ResponseEntity<?> deleteUser(@PathVariable int id) {
    log.info("Received DELETE request for /api/users/" + id);
    var user = userService.deleteUser(id);
    return ResponseEntity.ok(user);
  }

  @GetMapping("/validate")
  public ResponseEntity<?> authenticateUser(
      @RequestParam("username") String username, @RequestParam("password") String password) {
    return ResponseEntity.ok(userService.checkPassword(username, password) ? "VALID" : "INVALID");
  }
}
