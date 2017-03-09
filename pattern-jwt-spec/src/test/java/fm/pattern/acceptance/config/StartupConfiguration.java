/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fm.pattern.acceptance.config;

public class StartupConfiguration {

	private Integer pollingInterval = 1000;
	private Integer retryCount = 60;

	public StartupConfiguration() {

	}

	public StartupConfiguration(Integer pollingInterval, Integer retryCount) {
		if (pollingInterval != null) {
			this.pollingInterval = pollingInterval;
		}
		if (retryCount != null) {
			this.retryCount = retryCount;
		}
	}

	public Integer getPollingInterval() {
		return pollingInterval;
	}

	public void setPollingInterval(Integer pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

}
