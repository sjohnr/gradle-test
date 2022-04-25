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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Spring release train generator based on rules contained in a specification.
 * <p>
 * The rules are:
 * <ol>
 *     <li>Train 1 (January-May) or 2 (July-November)</li>
 *     <li>Version number (e.g. 0.1.2, 1.0.0, etc.)</li>
 *     <li>Week of month (1st, 2nd, 3rd, 4th)</li>
 *     <li>Day of week (Monday-Friday)</li>
 *     <li>Year (e.g. 2020, 2021, etc.)</li>
 * </ol>
 *
 * The release train generated will contain M1, M2, M3, RC1 and GA versions
 * mapped to their respective dates in the train.
 *
 * @author Steve Riesenberg
 */
public final class SpringReleaseTrain {
	private final SpringReleaseTrainSpec releaseTrainSpec;

	public SpringReleaseTrain(SpringReleaseTrainSpec releaseTrainSpec) {
		this.releaseTrainSpec = releaseTrainSpec;
	}

	public Map<String, LocalDate> getTrainDates() {
		Map<String, LocalDate> releaseDates = new LinkedHashMap<>();
		switch (this.releaseTrainSpec.getTrain()) {
			case ONE:
				addTrainDate(releaseDates, "M1", Month.JANUARY);
				addTrainDate(releaseDates, "M2", Month.FEBRUARY);
				addTrainDate(releaseDates, "M3", Month.MARCH);
				addTrainDate(releaseDates, "RC1", Month.APRIL);
				addTrainDate(releaseDates, null, Month.MAY);
				break;
			case TWO:
				addTrainDate(releaseDates, "M1", Month.JULY);
				addTrainDate(releaseDates, "M2", Month.AUGUST);
				addTrainDate(releaseDates, "M3", Month.SEPTEMBER);
				addTrainDate(releaseDates, "RC1", Month.OCTOBER);
				addTrainDate(releaseDates, null, Month.NOVEMBER);
				break;
		}

		return releaseDates;
	}

	public boolean isTrainDate(String version, LocalDate expectedDate) {
		return expectedDate.isEqual(getTrainDates().get(version));
	}

	private void addTrainDate(Map<String, LocalDate> releaseDates, String milestone, Month month) {
		LocalDate releaseDate = calculateTrainDate(
				this.releaseTrainSpec.getYear(),
				month,
				this.releaseTrainSpec.getDayOfWeek().getDayOfWeek(),
				this.releaseTrainSpec.getWeekOfMonth().getDayOffset()
		);
		String suffix = (milestone == null) ? "" : "-" + milestone;
		releaseDates.put(this.releaseTrainSpec.getVersion() + suffix, releaseDate);
	}

	private LocalDate calculateTrainDate(Year year, Month month, DayOfWeek dayOfWeek, int dayOffset) {
		LocalDate firstDayOfMonth = year.atMonth(month).atDay(1);
		int dayOfWeekOffset = dayOfWeek.getValue() - firstDayOfMonth.getDayOfWeek().getValue();
		if (dayOfWeekOffset < 0) {
			dayOfWeekOffset += 7;
		}

		return firstDayOfMonth.plusDays(dayOfWeekOffset).plusDays(dayOffset);
	}
}
