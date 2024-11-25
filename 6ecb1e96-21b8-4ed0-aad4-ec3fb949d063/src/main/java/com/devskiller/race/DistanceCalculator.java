package com.devskiller.race;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;

class DistanceCalculator {

	static double betweenPoints(Point startLocation, Point finishLocation) {
		return DistanceUtils.DEG_TO_KM * SpatialContext.GEO.calcDistance(startLocation, finishLocation);
	}
}
