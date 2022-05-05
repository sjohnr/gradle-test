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

package org.springframework.gradle;

import groovy.lang.MissingPropertyException;
import org.gradle.api.Project;

/**
 * @author Steve Riesenberg
 */
public class ProjectUtils {
	private ProjectUtils() {
	}

	public static String getProjectName(Project project) {
		String projectName = project.getRootProject().getName();
		if (projectName.endsWith("-build")) {
			projectName = projectName.substring(0, projectName.length() - "-build".length());
		}
		return projectName;
	}

	public static boolean isSnapshot(Project project) {
		String projectVersion = projectVersion(project);
		return projectVersion.matches("^.*([.-]BUILD)?-SNAPSHOT$");
	}

	public static boolean isMilestone(Project project) {
		String projectVersion = projectVersion(project);
		return projectVersion.matches("^.*[.-]M\\d+$") || projectVersion.matches("^.*[.-]RC\\d+$");
	}

	public static boolean isRelease(Project project) {
		return !(isSnapshot(project) || isMilestone(project));
	}

	private static String projectVersion(Project project) {
		return String.valueOf(project.getVersion());
	}

	public static String findProperty(Project project, String propertyName) {
		String propertyValue = null;
		if (project.hasProperty(propertyName)) {
			propertyValue = (String) project.findProperty(propertyName);
		}
		if (propertyValue == null) {
			throw new MissingPropertyException(propertyName + " is a required property. Provide a value with -P" + propertyName + "=...");
		}
		return propertyValue;
	}

	public static boolean findProperty(Project project, String propertyName, boolean defaultValue) {
		String propertyValue = String.valueOf(defaultValue);
		if (project.hasProperty(propertyName)) {
			propertyValue = (String) project.findProperty(propertyName);
		}
		return Boolean.parseBoolean(propertyValue);
	}
}
