package ru.hse.routemood.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptRequest {

    private String request;
    private Double latitude;
    private Double longitude;
}
