package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.office.OfficeNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.OfficeMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Office;
import uj.jwzp2021.gp.VetApp.repository.OfficeRepository;

import java.util.List;

@Service
public class OfficeService {
  private final OfficeRepository officeRepository;

  @Autowired
  public OfficeService(OfficeRepository officeRepository) {
    this.officeRepository = officeRepository;
  }

  public Office getOfficeById(int id) {
    var office = officeRepository.findById(id);
    return office.orElseThrow(
        () -> {
          throw new OfficeNotFoundException("Office with id:" + id + " not found.");
        });
  }

  public List<Office> getAll() {
    return officeRepository.findAll();
  }

  public Office createOffice(OfficeRequestDto officeRequestDto) {
    var office = officeRepository.save(OfficeMapper.toOffice(officeRequestDto));
    return office;
  }

  public Office deleteOffice(int id) {
    var office = getOfficeById(id);
    officeRepository.delete(office);
    return office;
  }
}
