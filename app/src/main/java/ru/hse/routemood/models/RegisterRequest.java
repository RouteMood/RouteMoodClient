package ru.hse.routemood.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
@AllArgsConstructor
public class RegisterRequest {

    private String username;
    private String login;
    private String password;
}
