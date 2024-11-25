package com.devskiller.race;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.impl.PointImpl;

import java.time.ZoneId;

class Locations {

	static Location GDYNIA() {
		return new Location(new PointImpl(18.530, 54.519, SpatialContext.GEO), ZoneId.of("Europe/Warsaw"));
	}

	static Location YSTAD() {
		return new Location(new PointImpl(13.820, 55.429, SpatialContext.GEO), ZoneId.of("Europe/Stockholm"));
	}

	static Location DOVER() {
		return new Location(new PointImpl(1.313, 51.128, SpatialContext.GEO), ZoneId.of("Europe/London"));
	}

	static Location HAMILTON() {
		return new Location(new PointImpl(-64.783, 32.295, SpatialContext.GEO), ZoneId.of("Atlantic/Bermuda"));
	}

	static Location CAPE_HORN() {
		return new Location(new PointImpl(-67.270, -55.974, SpatialContext.GEO), ZoneId.of("Chile/Continental"));
	}

	static Location HOBART() {
		return new Location(new PointImpl(147.327, -42.882, SpatialContext.GEO), ZoneId.of("Australia/Hobart"));
	}

}
