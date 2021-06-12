package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.entity.User;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UserRepresentation extends RepresentationModel<UserRepresentation> {
    private final int id;
    private final String username;

    public static UserRepresentation fromUser(User user) {
        return new UserRepresentation(user.getId(), user.getUsername());
    }
}
