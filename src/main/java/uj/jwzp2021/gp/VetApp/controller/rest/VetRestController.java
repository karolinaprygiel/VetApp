package uj.jwzp2021.gp.VetApp.controller.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.model.dto.VetMapper;
import uj.jwzp2021.gp.VetApp.model.dto.VetRequestDto;
import uj.jwzp2021.gp.VetApp.service.VetService;

import java.util.stream.Collectors;

@RequestMapping("api/vets")
@RestController
public class VetRestController {

    private final VetService vetService;

    @Autowired
    public VetRestController(VetService vetService) {
        this.vetService = vetService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getAnimal(@PathVariable int id) {
        return ResponseEntity.ok(vetService.getVetById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(vetService.getAll());
    }

    @PostMapping
    public ResponseEntity<?> createVet(@RequestBody VetRequestDto vetRequestDto){
        var result = vetService.createVet(vetRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteVet(@PathVariable int id){
       var result =  vetService.deleteVet(id);
       return ResponseEntity.accepted().body(result);
    }



}
