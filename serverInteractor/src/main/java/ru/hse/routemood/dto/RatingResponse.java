package ru.hse.routemood.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hse.routemood.models.Route;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {

    public UUID id;
    public String name;
    public String description;
    public double rating;
    public String authorUsername;
    public Route route;
    public Integer rate;
}