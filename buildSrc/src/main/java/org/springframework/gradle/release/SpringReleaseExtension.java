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

import org.gradle.api.Action;
import org.gradle.api.tasks.Nested;

/**
 * @author Steve Riesenberg
 */
public class SpringReleaseExtension {
	@Nested
	private Repository repository = new Repository();

	@Nested
	private ReleaseTrain releaseTrain = new ReleaseTrain();

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public void repository(Action<Repository> action) {
		action.execute(this.repository);
	}

	public ReleaseTrain getReleaseTrain() {
		return releaseTrain;
	}

	public void setReleaseTrain(ReleaseTrain releaseTrain) {
		this.releaseTrain = releaseTrain;
	}

	public void releaseTrain(Action<ReleaseTrain> action) {
		action.execute(this.releaseTrain);
	}

	public static class Repository {
		private String owner;
		private String name;

		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public static class ReleaseTrain {
		private Integer weekOfMonth = 1;
		private Integer dayOfWeek = 1;

		public Integer getWeekOfMonth() {
			return weekOfMonth;
		}

		public void setWeekOfMonth(int weekOfMonth) {
			this.weekOfMonth = weekOfMonth;
		}

		public Integer getDayOfWeek() {
			return dayOfWeek;
		}

		public void setDayOfWeek(int dayOfWeek) {
			this.dayOfWeek = dayOfWeek;
		}
	}
}
