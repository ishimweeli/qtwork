package com.devskiller.race;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

record Leg(Location startLocation, Location finishLocation, Duration duration, double distance) {

    Leg(Location startLocation, LocalDateTime startLocalTime, Location finishLocation, LocalDateTime finishLocalTime) {
        this(startLocation, finishLocation,
                figureOutDuration(startLocation, startLocalTime, finishLocation, finishLocalTime),
                DistanceCalculator.betweenPoints(startLocation.point(), finishLocation.point()));
    }

    private static Duration figureOutDuration(Location start, LocalDateTime startTime,
                                              Location finish, LocalDateTime finishTime) {
        // Convert both times to UTC for fair comparison
        var startUtc = startTime.atZone(start.zone()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        var finishUtc = finishTime.atZone(finish.zone()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

        // Calculate and return the duration
        return Duration.between(startUtc, finishUtc);
    }
}