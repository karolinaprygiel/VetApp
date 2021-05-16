package uj.jwzp2021.gp.VetApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer> {}
