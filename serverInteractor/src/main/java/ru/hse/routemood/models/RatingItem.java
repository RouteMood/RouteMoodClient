package ru.hse.routemood.models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RatingItem {

    public String authorUsername;
    @ElementCollection
    public List<Double> route; // [latitude_0, longitude_0, latitude_1, longitude_1, ...]
    @Builder.Default
    private int ratesSum = 0;
    @Builder.Default
    @ElementCollection
    private Map<String, Integer> usernameToRate = new HashMap<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    public double getRating() {
        return (double) ratesSum / usernameToRate.size();
    }
}
