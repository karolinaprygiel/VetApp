package uj.jwzp2021.gp.VetApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.office.OfficeNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.OfficeMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Office;
import uj.jwzp2021.gp.VetApp.repository.OfficeRepository;

import java.util.List;

@Slf4j
@Service
public class OfficeService {
  private final OfficeRepository officeRepository;
  private final OfficeMapper officeMapper;

  @Autowired
  public OfficeService(OfficeRepository officeRepository, OfficeMapper officeMapper) {
    this.officeRepository = officeRepository;
    this.officeMapper = officeMapper;
  }

  public Office getOfficeById(int id) {
    log.info("Looking up office with id=" + id);
    var office = officeRepository.findById(id);
    return office.orElseThrow(
        () -> {
          throw new OfficeNotFoundException("Office with id=" + id + " not found");
        });
  }

  public List<Office> getAll() {
    log.info("Looking up all offices");
    return officeRepository.findAll();
  }

  public Office createOffice(OfficeRequestDto officeRequestDto) {
    log.info("Creating office for: " + officeRequestDto);
    Office office;
    try{
      office = officeRepository.save(officeMapper.toOffice(officeRequestDto));
    }catch (DataAccessException ex){
      log.error("Repository problem while saving office for request: " + officeRequestDto);
      throw ex;
    }
    log.info("Office for request: " + officeRequestDto + " created successfully");
    return office;
  }

  public Office deleteOffice(int id) {
    log.info("Deleting office with id=" + id);
    var office = getOfficeById(id);
    try{
    officeRepository.delete(office);
    } catch (DataAccessException ex){
      log.error("Repository error while deleting office with id=" + id);
      throw ex;
    }
    log.info("Office with id=" + id + " deleted successfully");
    return office;
  }
}
