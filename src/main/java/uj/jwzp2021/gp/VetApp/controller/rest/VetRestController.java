package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.jwzp2021.gp.VetApp.repository.VetRepository;
import uj.jwzp2021.gp.VetApp.service.VetService;

@RequestMapping("api/vets")
@RestController
public class VetRestController {
    private final VetService vetService;

    @Autowired
    public VetRestController(VetService vetService) {
        this.vetService = vetService;
    }
}
