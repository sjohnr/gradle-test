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

import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

/**
 * A specification for a release train.
 *
 * @author Steve Riesenberg
 * @see SpringReleaseTrain
 */
public final class SpringReleaseTrainSpec {
	private final Train train;
	private final String version;
	private final WeekOfMonth weekOfMonth;
	private final DayOfWeek dayOfWeek;
	private final Year year;

	public SpringReleaseTrainSpec(Train train, String version, WeekOfMonth weekOfMonth, DayOfWeek dayOfWeek, Year year) {
		this.train = train;
		this.version = version;
		this.weekOfMonth = weekOfMonth;
		this.dayOfWeek = dayOfWeek;
		this.year = year;
	}

	public Train getTrain() {
		return train;
	}

	public String getVersion() {
		return version;
	}

	public WeekOfMonth getWeekOfMonth() {
		return weekOfMonth;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public Year getYear() {
		return year;
	}

	public static Builder builder() {
		return new Builder();
	}

	public enum WeekOfMonth {
		FIRST(0), SECOND(7), THIRD(14), FOURTH(21);

		private final int dayOffset;

		WeekOfMonth(int dayOffset) {
			this.dayOffset = dayOffset;
		}

		public int getDayOffset() {
			return dayOffset;
		}
	}

	public enum DayOfWeek {
		MONDAY(java.time.DayOfWeek.MONDAY),
		TUESDAY(java.time.DayOfWeek.TUESDAY),
		WEDNESDAY(java.time.DayOfWeek.WEDNESDAY),
		THURSDAY(java.time.DayOfWeek.THURSDAY),
		FRIDAY(java.time.DayOfWeek.FRIDAY);

		private final java.time.DayOfWeek dayOfWeek;

		DayOfWeek(java.time.DayOfWeek dayOfWeek) {
			this.dayOfWeek = dayOfWeek;
		}

		public java.time.DayOfWeek getDayOfWeek() {
			return dayOfWeek;
		}
	}

	public enum Train {
		ONE, TWO
	}

	public static class Builder {
		private Train train;
		private String version;
		private WeekOfMonth weekOfMonth;
		private DayOfWeek dayOfWeek;
		private Year year;

		private Builder() {
		}

		public Builder train(Train train) {
			this.train = train;
			return this;
		}

		public Builder nextTrain() {
			// Search for next train starting with this month
			return nextTrain(LocalDate.now().withDayOfMonth(1));
		}

		public Builder nextTrain(LocalDate startDate) {
			Train nextTrain = null;

			// Search for next train from a given start date
			LocalDate currentDate = startDate;
			while (nextTrain == null) {
				if (currentDate.getMonth() == Month.JANUARY) {
					nextTrain = Train.ONE;
				} else if (currentDate.getMonth() == Month.JULY) {
					nextTrain = Train.TWO;
				}

				currentDate = currentDate.plusMonths(1);
			}

			return train(nextTrain).year(currentDate.getYear());
		}

		public Builder version(String version) {
			this.version = version;
			return this;
		}

		public Builder weekOfMonth(WeekOfMonth weekOfMonth) {
			this.weekOfMonth = weekOfMonth;
			return this;
		}

		public Builder dayOfWeek(DayOfWeek dayOfWeek) {
			this.dayOfWeek = dayOfWeek;
			return this;
		}

		public Builder year(int year) {
			this.year = Year.of(year);
			return this;
		}

		public SpringReleaseTrainSpec build() {
			Assert.notNull(train, "train cannot be null");
			Assert.notNull(version, "version cannot be null");
			Assert.notNull(weekOfMonth, "weekOfMonth cannot be null");
			Assert.notNull(dayOfWeek, "dayOfWeek cannot be null");
			Assert.notNull(year, "year cannot be null");
			return new SpringReleaseTrainSpec(train, version, weekOfMonth, dayOfWeek, year);
		}
	}
}
