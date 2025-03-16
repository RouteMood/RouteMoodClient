package ru.hse.routemood.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class AuthRequest {
    private String username;
    private String password;
}
