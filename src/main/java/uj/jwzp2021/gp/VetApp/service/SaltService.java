package uj.jwzp2021.gp.VetApp.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SaltService {
    public String generateSalt() {
        return UUID.randomUUID().toString();
    }
}
