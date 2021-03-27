package uj.jwzp2021.gp.VetApp.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp2021.gp.VetApp.core.Vet;

@Repository
public interface VetRepository extends JpaRepository<Vet, Integer> {
}
