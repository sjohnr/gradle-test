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

package org.springframework.gradle.checkstyle;

import java.io.File;
import java.util.Objects;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.quality.CheckstyleExtension;
import org.gradle.api.plugins.quality.CheckstylePlugin;

/**
 * Adds and configures Checkstyle plugin.
 *
 * @author Vedran Pavic
 * @author Steve Riesenberg
 */
public class SpringJavaCheckstylePlugin implements Plugin<Project> {
	private static final String CHECKSTYLE_DIR = "etc/checkstyle";

	@Override
	public void apply(Project project) {
		project.getPlugins().withType(JavaPlugin.class).all((javaPlugin) -> {
			File checkstyleDir = project.getRootProject().file(CHECKSTYLE_DIR);
			if (checkstyleDir.exists() && checkstyleDir.isDirectory()) {
				project.getPluginManager().apply(CheckstylePlugin.class);
				// NOTE: See gradle.properties#springJavaformatVersion and build.gradle for actual version number
				project.getDependencies().add("checkstyle", "io.spring.javaformat:spring-javaformat-checkstyle:0.0.31");
				project.getDependencies().add("checkstyle", "io.spring.nohttp:nohttp-checkstyle:0.0.10");

				CheckstyleExtension checkstyle = project.getExtensions().findByType(CheckstyleExtension.class);
				Objects.requireNonNull(checkstyle, "CheckstyleExtension not found");
				checkstyle.getConfigDirectory().set(checkstyleDir);
				// NOTE: See build.gradle for actual version number
				checkstyle.setToolVersion("8.34");
			}
		});
	}
}
