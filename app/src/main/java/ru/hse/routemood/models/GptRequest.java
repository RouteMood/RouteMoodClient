package ru.hse.routemood.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class GptRequest {
    private @Id
    @GeneratedValue Long id;
    private String request;
    private Double longitude;
    private Double latitude;
}
