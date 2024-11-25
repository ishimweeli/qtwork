package com.devskiller.race;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class RaceConstructionTest {

	@Test
	public void shouldAddFirstLeg() throws Exception {
		// given
		Race race = new Race();
		Leg leg = new Leg(Locations.GDYNIA(), LocalDateTime.now(), Locations.DOVER(), LocalDateTime.now());

		// when
		race.addLeg(leg);

		// then
		assertThat(race.getLegsCount()).isEqualTo(1);
	}

	@Test
	public void shouldAddTwoLegs() throws Exception {
		// given
		Race race = new Race();
		Leg leg1 = new Leg(Locations.GDYNIA(), LocalDateTime.now(), Locations.DOVER(), LocalDateTime.now());
		Leg leg2 = new Leg(Locations.DOVER(), LocalDateTime.now(), Locations.HAMILTON(), LocalDateTime.now());

		// when
		race.addLeg(leg1);
		race.addLeg(leg2);

		// then
		assertThat(race.getLegsCount()).isEqualTo(2);
	}

}