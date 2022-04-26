/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.support;

import java.time.LocalDate;
import java.time.Year;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.support.SpringReleaseTrainSpec.Train;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Steve Riesenberg
 */
public class SpringReleaseTrainTests {
	@Test
	public void nextTrainWhenEndOf2019ThenReturnsTrainOneOf2020() {
		SpringReleaseTrainSpec releaseTrainSpec =
				SpringReleaseTrainSpec.builder()
						.nextTrain(LocalDate.of(2019, 12, 31))
						.version("1.0.0")
						.weekOfMonth(2)
						.dayOfWeek(2)
						.build();

		assertThat(releaseTrainSpec.getTrain()).isEqualTo(Train.ONE);
		assertThat(releaseTrainSpec.getYear()).isEqualTo(Year.of(2020));
	}

	@Test
	public void nextTrainWhenMiddleOf2020ThenReturnsTrainTwoOf2020() {
		SpringReleaseTrainSpec releaseTrainSpec =
				SpringReleaseTrainSpec.builder()
						.nextTrain(LocalDate.of(2020, 6, 30))
						.version("1.0.0")
						.weekOfMonth(2)
						.dayOfWeek(2)
						.build();

		assertThat(releaseTrainSpec.getTrain()).isEqualTo(Train.TWO);
		assertThat(releaseTrainSpec.getYear()).isEqualTo(Year.of(2020));
	}

	@Test
	public void getTrainDatesWhenTrainOneIsSecondTuesdayOf2020ThenSuccess() {
		SpringReleaseTrainSpec releaseTrainSpec =
				SpringReleaseTrainSpec.builder()
						.train(1)
						.version("1.0.0")
						.weekOfMonth(2)
						.dayOfWeek(2)
						.year(2020)
						.build();

		SpringReleaseTrain releaseTrain = new SpringReleaseTrain(releaseTrainSpec);
		Map<String, LocalDate> trainDates = releaseTrain.getTrainDates();
		assertThat(trainDates).hasSize(5);
		assertThat(trainDates.get("1.0.0-M1")).isEqualTo(LocalDate.of(2020, 1, 14));
		assertThat(trainDates.get("1.0.0-M2")).isEqualTo(LocalDate.of(2020, 2, 11));
		assertThat(trainDates.get("1.0.0-M3")).isEqualTo(LocalDate.of(2020, 3, 10));
		assertThat(trainDates.get("1.0.0-RC1")).isEqualTo(LocalDate.of(2020, 4, 14));
		assertThat(trainDates.get("1.0.0")).isEqualTo(LocalDate.of(2020, 5, 12));
	}

	@Test
	public void getTrainDatesWhenTrainTwoIsSecondTuesdayOf2020ThenSuccess() {
		SpringReleaseTrainSpec releaseTrainSpec =
				SpringReleaseTrainSpec.builder()
						.train(2)
						.version("1.0.0")
						.weekOfMonth(2)
						.dayOfWeek(2)
						.year(2020)
						.build();

		SpringReleaseTrain releaseTrain = new SpringReleaseTrain(releaseTrainSpec);
		Map<String, LocalDate> trainDates = releaseTrain.getTrainDates();
		assertThat(trainDates).hasSize(5);
		assertThat(trainDates.get("1.0.0-M1")).isEqualTo(LocalDate.of(2020, 7, 14));
		assertThat(trainDates.get("1.0.0-M2")).isEqualTo(LocalDate.of(2020, 8, 11));
		assertThat(trainDates.get("1.0.0-M3")).isEqualTo(LocalDate.of(2020, 9, 8));
		assertThat(trainDates.get("1.0.0-RC1")).isEqualTo(LocalDate.of(2020, 10, 13));
		assertThat(trainDates.get("1.0.0")).isEqualTo(LocalDate.of(2020, 11, 10));
	}

	@Test
	public void getTrainDatesWhenTrainOneIsSecondTuesdayOf2022ThenSuccess() {
		SpringReleaseTrainSpec releaseTrainSpec =
				SpringReleaseTrainSpec.builder()
						.train(1)
						.version("1.0.0")
						.weekOfMonth(2)
						.dayOfWeek(2)
						.year(2022)
						.build();

		SpringReleaseTrain releaseTrain = new SpringReleaseTrain(releaseTrainSpec);
		Map<String, LocalDate> trainDates = releaseTrain.getTrainDates();
		assertThat(trainDates).hasSize(5);
		assertThat(trainDates.get("1.0.0-M1")).isEqualTo(LocalDate.of(2022, 1, 11));
		assertThat(trainDates.get("1.0.0-M2")).isEqualTo(LocalDate.of(2022, 2, 8));
		assertThat(trainDates.get("1.0.0-M3")).isEqualTo(LocalDate.of(2022, 3, 8));
		assertThat(trainDates.get("1.0.0-RC1")).isEqualTo(LocalDate.of(2022, 4, 12));
		assertThat(trainDates.get("1.0.0")).isEqualTo(LocalDate.of(2022, 5, 10));
	}

	@Test
	public void getTrainDatesWhenTrainTwoIsSecondTuesdayOf2022ThenSuccess() {
		SpringReleaseTrainSpec releaseTrainSpec =
				SpringReleaseTrainSpec.builder()
						.train(2)
						.version("1.0.0")
						.weekOfMonth(2)
						.dayOfWeek(2)
						.year(2022)
						.build();

		SpringReleaseTrain releaseTrain = new SpringReleaseTrain(releaseTrainSpec);
		Map<String, LocalDate> trainDates = releaseTrain.getTrainDates();
		assertThat(trainDates).hasSize(5);
		assertThat(trainDates.get("1.0.0-M1")).isEqualTo(LocalDate.of(2022, 7, 12));
		assertThat(trainDates.get("1.0.0-M2")).isEqualTo(LocalDate.of(2022, 8, 9));
		assertThat(trainDates.get("1.0.0-M3")).isEqualTo(LocalDate.of(2022, 9, 13));
		assertThat(trainDates.get("1.0.0-RC1")).isEqualTo(LocalDate.of(2022, 10, 11));
		assertThat(trainDates.get("1.0.0")).isEqualTo(LocalDate.of(2022, 11, 8));
	}

	@Test
	public void getTrainDatesWhenTrainOneIsThirdMondayOf2022ThenSuccess() {
		SpringReleaseTrainSpec releaseTrainSpec =
				SpringReleaseTrainSpec.builder()
						.train(1)
						.version("1.0.0")
						.weekOfMonth(3)
						.dayOfWeek(1)
						.year(2022)
						.build();

		SpringReleaseTrain releaseTrain = new SpringReleaseTrain(releaseTrainSpec);
		Map<String, LocalDate> trainDates = releaseTrain.getTrainDates();
		assertThat(trainDates).hasSize(5);
		assertThat(trainDates.get("1.0.0-M1")).isEqualTo(LocalDate.of(2022, 1, 17));
		assertThat(trainDates.get("1.0.0-M2")).isEqualTo(LocalDate.of(2022, 2, 21));
		assertThat(trainDates.get("1.0.0-M3")).isEqualTo(LocalDate.of(2022, 3, 21));
		assertThat(trainDates.get("1.0.0-RC1")).isEqualTo(LocalDate.of(2022, 4, 18));
		assertThat(trainDates.get("1.0.0")).isEqualTo(LocalDate.of(2022, 5, 16));
	}

	@Test
	public void getTrainDatesWhenTrainTwoIsThirdMondayOf2022ThenSuccess() {
		SpringReleaseTrainSpec releaseTrainSpec =
				SpringReleaseTrainSpec.builder()
						.train(2)
						.version("1.0.0")
						.weekOfMonth(3)
						.dayOfWeek(1)
						.year(2022)
						.build();

		SpringReleaseTrain releaseTrain = new SpringReleaseTrain(releaseTrainSpec);
		Map<String, LocalDate> trainDates = releaseTrain.getTrainDates();
		assertThat(trainDates).hasSize(5);
		assertThat(trainDates.get("1.0.0-M1")).isEqualTo(LocalDate.of(2022, 7, 18));
		assertThat(trainDates.get("1.0.0-M2")).isEqualTo(LocalDate.of(2022, 8, 15));
		assertThat(trainDates.get("1.0.0-M3")).isEqualTo(LocalDate.of(2022, 9, 19));
		assertThat(trainDates.get("1.0.0-RC1")).isEqualTo(LocalDate.of(2022, 10, 17));
		assertThat(trainDates.get("1.0.0")).isEqualTo(LocalDate.of(2022, 11, 21));
	}

	@Test
	public void isTrainDateWhenTrainOneIsThirdMondayOf2022ThenSuccess() {
		SpringReleaseTrainSpec releaseTrainSpec =
				SpringReleaseTrainSpec.builder()
						.train(1)
						.version("1.0.0")
						.weekOfMonth(3)
						.dayOfWeek(1)
						.year(2022)
						.build();

		SpringReleaseTrain releaseTrain = new SpringReleaseTrain(releaseTrainSpec);
		for (int dayOfMonth = 1; dayOfMonth <= 31; dayOfMonth++) {
			assertThat(releaseTrain.isTrainDate("1.0.0-M1", LocalDate.of(2022, 1, dayOfMonth))).isEqualTo(dayOfMonth == 17);
		}
		for (int dayOfMonth = 1; dayOfMonth <= 28; dayOfMonth++) {
			assertThat(releaseTrain.isTrainDate("1.0.0-M2", LocalDate.of(2022, 2, dayOfMonth))).isEqualTo(dayOfMonth == 21);
		}
		for (int dayOfMonth = 1; dayOfMonth <= 31; dayOfMonth++) {
			assertThat(releaseTrain.isTrainDate("1.0.0-M3", LocalDate.of(2022, 3, dayOfMonth))).isEqualTo(dayOfMonth == 21);
		}
		for (int dayOfMonth = 1; dayOfMonth <= 30; dayOfMonth++) {
			assertThat(releaseTrain.isTrainDate("1.0.0-RC1", LocalDate.of(2022, 4, dayOfMonth))).isEqualTo(dayOfMonth == 18);
		}
		for (int dayOfMonth = 1; dayOfMonth <= 31; dayOfMonth++) {
			assertThat(releaseTrain.isTrainDate("1.0.0", LocalDate.of(2022, 5, dayOfMonth))).isEqualTo(dayOfMonth == 16);
		}
	}
}
