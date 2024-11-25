package com.devskiller.race;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

record Race(List<Leg> legs) {

	Race() {
		this(new ArrayList<>());
	}

	void addLeg(Leg leg) throws IllegalStartPointException {
		if (!legs.isEmpty()) {
			Leg lastLeg = legs.get(legs.size() - 1);
			if (!lastLeg.finishLocation().equals(leg.startLocation())) {
				throw new IllegalStartPointException();
			}
		}
		legs.add(leg);
	}

	int getLegsCount() {
		return legs.size();
	}

	Duration getTotalDuration() {
		return legs.stream()
				.map(Leg::duration)
				.reduce(Duration.ZERO, Duration::plus);
	}

	Duration getAverageLegDuration() {
		if (legs.isEmpty()) {
			return Duration.ZERO;
		}
		return getTotalDuration().dividedBy(legs.size());
	}

	double getTotalDistance() {
		return legs.stream()
				.mapToDouble(Leg::distance)
				.sum();
	}

	double getAverageLegDistance() {
		if (legs.isEmpty()) {
			return 0.0;
		}
		return getTotalDistance() / legs.size();
	}
}