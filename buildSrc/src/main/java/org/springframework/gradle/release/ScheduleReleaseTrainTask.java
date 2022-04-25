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

package org.springframework.gradle.release;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import com.github.api.GitHubMilestoneApi;
import com.github.api.Milestone;
import com.github.api.RepositoryRef;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import org.springframework.support.SpringReleaseTrain;
import org.springframework.support.SpringReleaseTrainSpec;

/**
 * @author Steve Riesenberg
 */
public class ScheduleReleaseTrainTask extends DefaultTask {
	@Input
	private RepositoryRef repository = new RepositoryRef();

	@Input
	private String gitHubAccessToken;

	@Input
	private String version;

	@Input
	private Integer weekOfMonth;

	@Input
	private Integer dayOfWeek;

	@TaskAction
	public void scheduleReleaseTrain() {
		GitHubMilestoneApi gitHubMilestoneApi = new GitHubMilestoneApi(this.gitHubAccessToken);
		boolean hasExistingMilestone = gitHubMilestoneApi.getMilestones(this.repository).stream()
				.anyMatch(milestone -> this.version.equals(milestone.getTitle()));

		if (!hasExistingMilestone) {
			// Create M1, M2, M3, RC1 and GA milestones
			getReleaseTrainDates().forEach((milestoneTitle, dueOn) -> {
				Milestone milestone = new Milestone();
				milestone.setTitle(milestoneTitle);
				// Note: GitHub seems to store full date/time as UTC then displays
				// as a date (no time) in your timezone, which means the date will
				// not always be the same date as we intend.
				// For example, midnight UTC is actually 8pm CDT (the previous day).
				// We use 12pm/noon UTC to be as far from anybody's midnight as we can.
				milestone.setDueOn(dueOn.atTime(LocalTime.NOON));
				gitHubMilestoneApi.createMilestone(this.repository, milestone);
			});
		}
	}

	private Map<String, LocalDate> getReleaseTrainDates() {
		SpringReleaseTrainSpec releaseTrainSpec =
				SpringReleaseTrainSpec.builder()
						.nextTrain()
						.version(this.version)
						.weekOfMonth(this.weekOfMonth)
						.dayOfWeek(this.dayOfWeek)
						.build();
		SpringReleaseTrain springReleaseTrain =
				new SpringReleaseTrain(releaseTrainSpec);

		return springReleaseTrain.getTrainDates();
	}

	public RepositoryRef getRepository() {
		return this.repository;
	}

	public void setRepository(RepositoryRef repository) {
		this.repository = repository;
	}

	public String getGitHubAccessToken() {
		return this.gitHubAccessToken;
	}

	public void setGitHubAccessToken(String gitHubAccessToken) {
		this.gitHubAccessToken = gitHubAccessToken;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getWeekOfMonth() {
		return weekOfMonth;
	}

	public void setWeekOfMonth(Integer weekOfMonth) {
		this.weekOfMonth = weekOfMonth;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
}
