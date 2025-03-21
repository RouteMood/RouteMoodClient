package ru.hse.routemood.models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String username;
    private String login;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
