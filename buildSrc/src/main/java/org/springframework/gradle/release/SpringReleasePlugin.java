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
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @author Steve Riesenberg
 */
public class SpringReleasePlugin implements Plugin<Project> {
	@Override
	public void apply(Project project) {
		SpringReleaseExtension releaseSettings = project.getExtensions().create("springRelease", SpringReleaseExtension.class);
		SpringReleaseExtension.Repository repository = releaseSettings.getRepository();
		SpringReleaseExtension.ReleaseTrain releaseTrain = releaseSettings.getReleaseTrain();

		project.getTasks().register("scheduleReleaseTrain", ScheduleReleaseTrainTask.class, (scheduleReleaseTrain) -> {
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
	}
}
