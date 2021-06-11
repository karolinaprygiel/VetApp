package uj.jwzp2021.gp.VetApp.model.dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import uj.jwzp2021.gp.VetApp.security.Role;

@Data
@AllArgsConstructor
public class UserCreationRequestDto {
    private final String username;
    private final String password;
    private final Role role;
}
