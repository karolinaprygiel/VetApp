package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.office.OfficeNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.OfficeMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.OfficeResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Office;
import uj.jwzp2021.gp.VetApp.repository.OfficeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfficeService {
  private final OfficeRepository officeRepository;

  @Autowired
  public OfficeService(OfficeRepository officeRepository) {
    this.officeRepository = officeRepository;
  }

  Office getRawOfficeById(int id) {
    var office = officeRepository.findById(id);
    return office.orElseThrow(
        () -> {
          throw new OfficeNotFoundException("Office with id:" + id + " not found.");
        });
  }

  public OfficeResponseDto getOfficeById(int id) {
    return OfficeMapper.toOfficeResponseDto(getRawOfficeById(id));
  }

  public List<OfficeResponseDto> getAll() {
    return getRawAll().stream().map(OfficeMapper::toOfficeResponseDto).collect(Collectors.toList());
  }

  public List<Office> getRawAll() {
    return officeRepository.findAll();
  }

  public OfficeResponseDto createOffice(OfficeRequestDto officeRequestDto) {
    var office = officeRepository.save(OfficeMapper.toOffice(officeRequestDto));
    return OfficeMapper.toOfficeResponseDto(office);
  }

  public OfficeResponseDto deleteOffice(int id) {
    var office = getRawOfficeById(id);
    officeRepository.delete(office);
    return OfficeMapper.toOfficeResponseDto(office);
  }
}
