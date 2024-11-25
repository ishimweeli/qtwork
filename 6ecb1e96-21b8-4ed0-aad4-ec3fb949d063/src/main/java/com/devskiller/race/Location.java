package com.devskiller.race;

import org.locationtech.spatial4j.shape.Point;

import java.time.ZoneId;

record Location(Point point, ZoneId zone) {
}
