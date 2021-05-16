package uj.jwzp2021.gp.VetApp.mapper;

import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Office;

public class OfficeMapper {

  public static Office toOffice(OfficeRequestDto officeRequestDto) {
    return new Office(-1, officeRequestDto.getName());
  }
}
