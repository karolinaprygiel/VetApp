package uj.jwzp2021.gp.VetApp.mapper;

import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.OfficeResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Office;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;

import java.util.stream.Collectors;

public class OfficeMapper {
  public static OfficeResponseDto toOfficeResponseDto(Office office) {
    return new OfficeResponseDto(
        office.getId(),
        office.getName(),
        office.getVisits().stream().map(Visit::getId).collect(Collectors.toList()));
  }

  public static Office toOffice(OfficeRequestDto officeRequestDto) {
    return new Office(-1, officeRequestDto.getName());
  }
}
