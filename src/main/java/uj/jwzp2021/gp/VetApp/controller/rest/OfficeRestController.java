package uj.jwzp2021.gp.VetApp.controller.rest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.service.OfficeService;

@RequestMapping("api/offices")
@RestController
public class OfficeRestController {

  private final OfficeService officeService;

  @Autowired
  public OfficeRestController(OfficeService officeService) {
    this.officeService = officeService;
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<?> getOffice(@PathVariable int id) {
    return ResponseEntity.ok(officeService.getOfficeById(id));
  }

  @GetMapping
  public ResponseEntity<?> getAllOffices() {
    return ResponseEntity.ok(
        officeService.getAll());
  }

  @PostMapping
  public ResponseEntity<?> createOffice(@RequestBody OfficeRequestDto officeRequestDto) {
    var office = officeService.createOffice(officeRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(office);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteOffice(@PathVariable int id) {
    var office = officeService.deleteOffice(id);
    return ResponseEntity.accepted().body(office);
  }
}
