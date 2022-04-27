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

import com.github.api.GitHubMilestoneApi;
import com.github.api.RepositoryRef;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

/**
 * @author Steve Riesenberg
 */
public class CheckMilestoneHasNoOpenIssues extends DefaultTask {
	@Input
	private RepositoryRef repository = new RepositoryRef();

	@Input
	private String gitHubAccessToken;

	@Input
	private String version;

	@TaskAction
	public void checkMilestoneHasNoOpenIssues() {
		GitHubMilestoneApi gitHubMilestoneApi = new GitHubMilestoneApi(this.gitHubAccessToken);
		long milestoneNumber = gitHubMilestoneApi.findMilestoneNumberByTitle(this.repository, this.version);
		boolean hasOpenIssues = gitHubMilestoneApi.isOpenIssuesForMilestoneNumber(this.repository, milestoneNumber);
		System.out.println(!hasOpenIssues);
	}

	public RepositoryRef getRepository() {
		return repository;
	}

	public void setRepository(RepositoryRef repository) {
		this.repository = repository;
	}

	public String getGitHubAccessToken() {
		return gitHubAccessToken;
	}

	public void setGitHubAccessToken(String gitHubAccessToken) {
		this.gitHubAccessToken = gitHubAccessToken;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
