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

import com.github.api.RepositoryRef;
import groovy.lang.MissingPropertyException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @author Steve Riesenberg
 */
public class SpringReleasePlugin implements Plugin<Project> {
	@Override
	public void apply(Project project) {
		// Apply additional plugins
		project.getPluginManager().apply(GitHubChangelogPlugin.class);

		// Register configuration extension
		SpringReleaseExtension releaseSettings = project.getExtensions().create("springRelease", SpringReleaseExtension.class);
		SpringReleaseExtension.Repository repository = releaseSettings.getRepository();
		SpringReleaseExtension.ReleaseTrain releaseTrain = releaseSettings.getReleaseTrain();

		// Register release management tasks
		project.getTasks().register("scheduleReleaseTrain", ScheduleReleaseTrainTask.class, (scheduleReleaseTrain) -> {
			scheduleReleaseTrain.doNotTrackState("API call to GitHub needs to check for new milestones every time");
			scheduleReleaseTrain.setGroup("Release");
			scheduleReleaseTrain.setDescription("Schedule the next release train as a series of milestones starting in January or July");

			scheduleReleaseTrain.setRepository(new RepositoryRef(repository.getOwner(), repository.getName()));
			scheduleReleaseTrain.setWeekOfMonth(releaseTrain.getWeekOfMonth());
			scheduleReleaseTrain.setDayOfWeek(releaseTrain.getDayOfWeek());
			scheduleReleaseTrain.setGitHubAccessToken((String) project.findProperty("gitHubAccessToken"));
			scheduleReleaseTrain.setVersion((String) project.findProperty("nextVersion"));
		});

		project.getTasks().register("triggerRelease", TriggerReleaseTask.class, (triggerRelease) -> {
			triggerRelease.setGroup("Release");
			triggerRelease.setDescription("Create a workflow_dispatch event to trigger a release on a given branch");

			triggerRelease.setRepository(new RepositoryRef(repository.getOwner(), repository.getName()));
			triggerRelease.setGitHubAccessToken((String) project.findProperty("gitHubAccessToken"));
			triggerRelease.setBranch((String) project.findProperty("branch"));
		});

		project.getTasks().register("getNextReleaseMilestone", GetNextReleaseMilestoneTask.class, (getNextReleaseMilestone) -> {
			getNextReleaseMilestone.doNotTrackState("API call to GitHub needs to check for new milestones every time");
			getNextReleaseMilestone.setGroup("Release");
			getNextReleaseMilestone.setDescription("Calculates the next release version based on the current version and outputs the version number");

			getNextReleaseMilestone.setRepository(new RepositoryRef(repository.getOwner(), repository.getName()));
			getNextReleaseMilestone.setGitHubAccessToken((String) project.findProperty("gitHubAccessToken"));
		});

		project.getTasks().register("getNextSnapshotVersion", GetNextSnapshotVersionTask.class, (getNextSnapshotVersion) -> {
			getNextSnapshotVersion.doNotTrackState("API call to GitHub needs to check for new milestones every time");
			getNextSnapshotVersion.setGroup("Release");
			getNextSnapshotVersion.setDescription("Calculates the next snapshot version based on the current version and outputs the version number");
		});

		project.getTasks().register("checkMilestoneHasNoOpenIssues", CheckMilestoneHasNoOpenIssues.class, (checkMilestoneHasNoOpenIssues) -> {
			checkMilestoneHasNoOpenIssues.doNotTrackState("API call to GitHub needs to check for open issues every time");
			checkMilestoneHasNoOpenIssues.setGroup("Release");
			checkMilestoneHasNoOpenIssues.setDescription("Checks if there are any open issues for the specified repository and milestone and outputs true or false");

			checkMilestoneHasNoOpenIssues.setRepository(new RepositoryRef(repository.getOwner(), repository.getName()));
			checkMilestoneHasNoOpenIssues.setGitHubAccessToken((String) project.findProperty("gitHubAccessToken"));
			checkMilestoneHasNoOpenIssues.setVersion((String) project.findProperty("nextVersion"));
		});

		project.getTasks().register("checkIsMilestoneDueToday", CheckIsMilestoneDueToday.class, (checkIsMilestoneDueToday) -> {
			checkIsMilestoneDueToday.doNotTrackState("API call to GitHub needs to check the milestone due date every time");
			checkIsMilestoneDueToday.setGroup("Release");
			checkIsMilestoneDueToday.setDescription("Checks if the given version is due today or past due and outputs true or false");

			checkIsMilestoneDueToday.setRepository(new RepositoryRef(repository.getOwner(), repository.getName()));
			checkIsMilestoneDueToday.setGitHubAccessToken((String) project.findProperty("gitHubAccessToken"));
			checkIsMilestoneDueToday.setVersion((String) project.findProperty("nextVersion"));
		});

		project.getTasks().register("createGitHubRelease", CreateGitHubReleaseTask.class, (createGitHubRelease) -> {
			createGitHubRelease.doNotTrackState("API call to GitHub needs to check for new issues and create a release every time");
			createGitHubRelease.setGroup("Release");
			createGitHubRelease.setDescription("Create a github release");
			createGitHubRelease.dependsOn("generateChangelog");

			createGitHubRelease.setRepository(new RepositoryRef(repository.getOwner(), repository.getName()));
			createGitHubRelease.setCreateRelease("true".equals(project.findProperty("createRelease")));
			createGitHubRelease.setVersion((String) project.findProperty("nextVersion"));
			if (project.hasProperty("branch")) {
				createGitHubRelease.setBranch((String) project.findProperty("branch"));
			}
			createGitHubRelease.setGitHubAccessToken((String) project.findProperty("gitHubAccessToken"));
			if (createGitHubRelease.isCreateRelease() && createGitHubRelease.getGitHubAccessToken() == null) {
				throw new MissingPropertyException("Please provide an access token with -PgitHubAccessToken=...");
			}
		});
	}
}
