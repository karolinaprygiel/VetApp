package uj.jwzp2021.gp.VetApp.controller.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.model.dto.AnimalMapper;
import uj.jwzp2021.gp.VetApp.model.dto.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.VetMapper;
import uj.jwzp2021.gp.VetApp.model.dto.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.service.ClientService;
import uj.jwzp2021.gp.VetApp.service.VetService;
import uj.jwzp2021.gp.VetApp.util.ClientCreationError;

import java.util.Optional;
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
        var vet = vetService.getVetById(id);
        if (vet.isPresent()) {
            var vetDto = VetMapper.toVetResponseDto(vet.get());
            return ResponseEntity.ok(vetDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(RestUtil.response("No vet with such ID found."));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(vetService.getAll().stream().map(VetMapper::toVetResponseDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?> createVet(@RequestBody VetRequestDto vetRequestDto){
        var result = vetService.createVet(vetRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteVet(@PathVariable int id){
       var result =  vetService.deleteVet(id);
       if (result.isPresent()){
           return ResponseEntity.accepted().body(result);
       }else{
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
               .body(RestUtil.response("No vet with such ID found."));
       }
    }



}
