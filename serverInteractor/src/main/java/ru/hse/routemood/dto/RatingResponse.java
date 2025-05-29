package ru.hse.routemood.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hse.routemood.models.Route;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {

    public UUID id;
    public String name;
    public String description;
    public double rating;
    public UserResponse author;
    public Route route;
    public Integer rate;
}