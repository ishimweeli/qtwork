package com.devskiller.race;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class LegTest {

	@Test
	public void shouldCalculateDistance() throws Exception {
		// given
		Leg leg = new Leg(Locations.HAMILTON(), LocalDateTime.now(), Locations.CAPE_HORN(), LocalDateTime.now());

		// when
		double distance = leg.distance();

		// then
		assertThat(distance).isCloseTo(9817.92, within(0.01));
	}

	@Test
	public void shouldCalculateDurationBetweenSameTimeZones() throws Exception {
		// given
		LocalDateTime start  = LocalDateTime.of(2017, 5, 20, 16, 21);
		LocalDateTime finish = LocalDateTime.of(2017, 5, 20, 18, 45);

		Leg leg = new Leg(Locations.YSTAD(), start, Locations.GDYNIA(), finish);

		// when
		Duration duration = leg.duration();

		// then
		assertThat(duration).isEqualTo(Duration.ofHours(2).plusMinutes(24));
	}

	@Test
	public void shouldCalculateDurationBetweenDifferentTimeZones() throws Exception {
		// given
		LocalDateTime start  = LocalDateTime.of(2017, 5, 20, 16, 21);
		LocalDateTime finish = LocalDateTime.of(2017, 5, 20, 18, 45);

		Leg leg = new Leg(Locations.YSTAD(), start, Locations.DOVER(), finish);

		// when
		Duration duration = leg.duration();

		// then
		assertThat(duration).isEqualTo(Duration.ofHours(3).plusMinutes(24));
	}
}