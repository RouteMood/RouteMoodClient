package ru.hse.routemood.models;

import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String login;
    private String password;
}
