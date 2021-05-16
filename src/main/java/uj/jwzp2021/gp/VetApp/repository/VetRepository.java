package uj.jwzp2021.gp.VetApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;

@Repository
public interface VetRepository extends JpaRepository<Vet, Integer> {}
