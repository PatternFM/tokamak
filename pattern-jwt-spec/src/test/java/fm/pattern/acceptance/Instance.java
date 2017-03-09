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

package fm.pattern.acceptance;

public class Instance {

	private String name;
	private String path;
	private String ping;

	private String start = "start.sh";
	private String stop = "stop.sh";

	public Instance(String name, String path, String ping) {
		this.name = name;
		this.path = path;
		this.ping = ping;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPing() {
		return ping;
	}

	public void setPing(String ping) {
		this.ping = ping;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public boolean running() {
		return InstanceManagementService.isRunning(this);
	}

	public void start() {
		if (!running()) {
			InstanceManagementService.start(this);
		}
	}

	public void stop() {
		if (running()) {
			InstanceManagementService.stop(this);
		}
	}

}
