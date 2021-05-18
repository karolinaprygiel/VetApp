package uj.jwzp2021.gp.VetApp.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;
import uj.jwzp2021.gp.VetApp.exception.animal.AnimalNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.office.OfficeNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.OfficeMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;
import uj.jwzp2021.gp.VetApp.model.entity.Office;
import uj.jwzp2021.gp.VetApp.repository.OfficeRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfficeServiceTest {

  @InjectMocks
  private OfficeService officeService;
  @Mock
  private OfficeRepository officeRepository;
  @Mock
  private OfficeMapper officeMapper;

  private static Office office;
  private static Office office2;

  @BeforeAll
  static void setUp(){
    office = new Office(1, "gabinet1");
    office2 = new Office(2, "gabinet2");
  }


  @Test
  void getOfficeById_ValidId_Returns_Office() {
    given(officeRepository.findById(1)).willReturn(Optional.of(office));
    assertThat(officeService.getOfficeById(1)).isNotNull().isEqualTo(office);
  }

  @Test
  void getOfficeById_invalidId_Throws_OfficeNotFoundException() {
    given(officeRepository.findById(1)).willReturn(Optional.empty());
    VeterinaryAppException exception = assertThrows(OfficeNotFoundException.class, () -> officeService.getOfficeById(1));
    assertEquals("Office with id=1 not found", exception.getMessage());
  }

  @Test
  void getAll_Returns_ListOfOffices() {
    given(officeRepository.findAll()).willReturn(List.of(office2, office));
    assertThat(officeService.getAll()).isNotNull().hasSize(2).containsExactlyInAnyOrderElementsOf(List.of(office, office2));
  }

  @Test
  void getAll_Returns_ListOfOneOffice() {
    given(officeRepository.findAll()).willReturn(List.of(office));
    assertThat(officeService.getAll()).isNotNull().hasSize(1).containsExactlyInAnyOrderElementsOf(List.of(office));
  }

  @Test
  void getAll_Returns_EmptyList() {
    given(officeRepository.findAll()).willReturn(Collections.emptyList());
    assertThat(officeService.getAll()).isNotNull().isEmpty();
  }

  @Test
  void deleteOffice_OfficeExists_DeleteAndReturns_Office() {
    given(officeRepository.findById(1)).willReturn(Optional.of(office));
    assertThat(officeService.deleteOffice(1)).isEqualTo(office);
    verify(officeRepository, Mockito.times(1)).delete(office);
    verify(officeRepository, Mockito.times(1)).findById(1);
  }

  @Test
  void deleteOffice_OfficeNotExists_Throws_OfficeNotFoundException() {
    given(officeRepository.findById(1)).willReturn(Optional.empty());
    VeterinaryAppException exception = assertThrows(OfficeNotFoundException.class, () -> officeService.getOfficeById(1));
    assertEquals("Office with id=1 not found", exception.getMessage());
    verifyNoMoreInteractions(officeRepository);
  }

  @Test
  void createOffice_ReturnsAndSave_Animal() {
    OfficeRequestDto officeReq = new OfficeRequestDto("gabinet1");
    given(officeMapper.toOffice(officeReq)).willReturn(office);
    given(officeRepository.save(office)).willReturn(office);

    assertThat(officeService.createOffice(officeReq)).isNotNull().isEqualTo(office);
    verify(officeRepository, Mockito.times(1)).save(office);
  }
}