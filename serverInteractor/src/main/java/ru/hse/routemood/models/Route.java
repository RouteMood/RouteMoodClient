package ru.hse.routemood.models;

import java.util.List;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Route {

    private List<RouteItem> route;

    @Override
    public String toString() {
        StringJoiner result = new StringJoiner("\n");
        for (RouteItem it : route) {
            result.add(it.toString());
        }
        return result.toString();
    }

    @Getter
    @AllArgsConstructor
    public static class RouteItem {

        private double latitude;
        private double longitude;

        @Override
        public String toString() {
            return "[latitude = " + latitude + ", longitude = " + longitude + "]";
        }
    }
}