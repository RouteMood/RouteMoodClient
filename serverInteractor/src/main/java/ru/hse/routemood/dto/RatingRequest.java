package ru.hse.routemood.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hse.routemood.models.Route;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {

    public String name;
    public String description;
    public String authorUsername;
    public Route route;
}

