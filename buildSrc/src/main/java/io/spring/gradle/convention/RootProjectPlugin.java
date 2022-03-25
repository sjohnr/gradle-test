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

package io.spring.gradle.convention;

import java.io.File;
import java.util.Objects;

import io.spring.nohttp.gradle.NoHttpExtension;
import io.spring.nohttp.gradle.NoHttpPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.PluginManager;

import org.springframework.gradle.classpath.CheckProhibitedDependenciesLifecyclePlugin;
import org.springframework.gradle.maven.SpringNexusPlugin;

/**
 * @author Steve Riesenberg
 */
public class RootProjectPlugin implements Plugin<Project> {
	@Override
	public void apply(Project project) {
		// Apply default plugins
		PluginManager pluginManager = project.getPluginManager();
		pluginManager.apply(BasePlugin.class);
		pluginManager.apply(NoHttpPlugin.class);
		pluginManager.apply(SpringNexusPlugin.class);
		pluginManager.apply(CheckProhibitedDependenciesLifecyclePlugin.class);

		// Apply default repositories
		project.getRepositories().mavenCentral();

		// Configure nohttp plugin
		NoHttpExtension nohttp = project.getExtensions().getByType(NoHttpExtension.class);
		Objects.requireNonNull(nohttp, "NoHttpExtension not found");
		File allowlistFile = project.getRootProject().file("etc/nohttp/allowlist.lines");
		nohttp.setAllowlistFile(allowlistFile);
		nohttp.getSource().exclude("buildSrc/build/**");

		// Ensure release build automatically closes and releases staging repository
		Task finalizeDeployArtifacts = project.task("finalizeDeployArtifacts");
		if (Utils.isRelease(project) && project.hasProperty("ossrhUsername")) {
			Task closeAndReleaseOssrhStagingRepository = project.getTasks().findByName("closeAndReleaseOssrhStagingRepository");
			finalizeDeployArtifacts.dependsOn(closeAndReleaseOssrhStagingRepository);
		}
	}
}
