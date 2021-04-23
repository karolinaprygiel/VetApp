package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.jwzp2021.gp.VetApp.service.AnimalService;

@RestController
@RequestMapping("api/animals")
public class AnimalRestController {
    private final AnimalService animalService;

    @Autowired
    public AnimalRestController(AnimalService animalService) {
        this.animalService = animalService;
    }

}
