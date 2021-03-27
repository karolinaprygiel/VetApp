package uj.jwzp2021.gp.VetApp.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp2021.gp.VetApp.core.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer> {
}
