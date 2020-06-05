package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String name;

    public static UserResponse fromUser(User u) {
        final UserResponse us = new UserResponse();
        us.name = u.getName();
        us.id = u.getId();
        us.username = u.getUsername();
        return us;
    }
}
