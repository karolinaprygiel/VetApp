package uj.jwzp2021.gp.VetApp.mapper;

import org.springframework.stereotype.Component;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Office;

@Component
public class OfficeMapper {

  public Office toOffice(OfficeRequestDto officeRequestDto) {
    return new Office(-1, officeRequestDto.getName());
  }
}
