package com.devskiller.race;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class RaceCalculationTest {

	@Test
	public void shouldCalculateTotalDistance() throws Exception {
		// given
		Race race = new Race();

		race.addLeg(new Leg(Locations.GDYNIA(), LocalDateTime.now(), Locations.DOVER(), LocalDateTime.now()));
		race.addLeg(new Leg(Locations.DOVER(), LocalDateTime.now(), Locations.HAMILTON(), LocalDateTime.now()));

		// when
		double totalDistance = race.getTotalDistance();

		// then
		assertThat(totalDistance).isCloseTo(6870.90, within(0.01));
	}

	@Test
	public void shouldCalculateAverageLegDistance() throws Exception {
		// given
		Race race = new Race();

		race.addLeg(new Leg(Locations.GDYNIA(), LocalDateTime.now(), Locations.DOVER(), LocalDateTime.now()));
		race.addLeg(new Leg(Locations.DOVER(), LocalDateTime.now(), Locations.HAMILTON(), LocalDateTime.now()));

		// when
		double averageLegDistance = race.getAverageLegDistance();

		// then
		assertThat(averageLegDistance).isCloseTo(3435.45, within(0.01));
	}

	@Test
	public void shouldCalculateTotalDuration() throws Exception {
		// given
		Race race = new Race();
		LocalDateTime leg1Start  = LocalDateTime.of(2017,5, 20, 10, 0);
		LocalDateTime leg1Finish = LocalDateTime.of(2017,5, 20, 12, 0);
		LocalDateTime leg2Start  = LocalDateTime.of(2017,5, 20, 13, 0);
		LocalDateTime leg2Finish = LocalDateTime.of(2017,5, 20, 14, 0);

		race.addLeg(new Leg(Locations.GDYNIA(), leg1Start, Locations.YSTAD(), leg1Finish));
		race.addLeg(new Leg(Locations.YSTAD(), leg2Start, Locations.GDYNIA(), leg2Finish));

		// when
		Duration totalDuration = race.getTotalDuration();

		// then
		assertThat(totalDuration).isEqualTo(Duration.ofHours(3));
	}

	@Test
	public void shouldCalculateAverageLegDuration() throws Exception {
		// given
		Race race = new Race();
		LocalDateTime leg1Start  = LocalDateTime.of(2017,5, 20, 10, 0);
		LocalDateTime leg1Finish = LocalDateTime.of(2017,5, 20, 12, 0);
		LocalDateTime leg2Start  = LocalDateTime.of(2017,5, 20, 13, 0);
		LocalDateTime leg2Finish = LocalDateTime.of(2017,5, 20, 14, 0);

		race.addLeg(new Leg(Locations.GDYNIA(), leg1Start, Locations.YSTAD(), leg1Finish));
		race.addLeg(new Leg(Locations.YSTAD(), leg2Start, Locations.GDYNIA(), leg2Finish));

		// when
		Duration averageLegDuration = race.getAverageLegDuration();

		// then
		assertThat(averageLegDuration).isEqualTo(Duration.ofHours(1).plusMinutes(30));
	}

}