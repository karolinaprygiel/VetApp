package uj.jwzp2021.gp.VetApp.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {

  @Query(
      "select v from visits v where ((v.vet.id = :vetId or v.office.id = :officeId) and ((:timeFrom >= v.startTime and :timeFrom < (v.startTime + v.duration)) or (:timeTo > v.startTime and :timeTo <= (v.startTime + v.duration))))")
  List<Visit> overlaps(LocalDateTime timeFrom, LocalDateTime timeTo, int vetId, int officeId);

  @Transactional
  @Modifying
  @Query(
      "update visits v set v.visitStatus = uj.jwzp2021.gp.VetApp.model.entity.VisitStatus.FINISHED_AUTOMATICALLY WHERE (v.startTime + v.duration) < :time and v.visitStatus = uj.jwzp2021.gp.VetApp.model.entity.VisitStatus.PLANNED")
  void finishOutOfDateVisits(LocalDateTime time);
}
