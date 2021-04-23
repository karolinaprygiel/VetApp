package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.repository.VetRepository;

@Service
public class VetService {
    private final VetRepository vetRepository;

    @Autowired
    public VetService (VetRepository vetRepository){
        this.vetRepository = vetRepository;
    }

}
