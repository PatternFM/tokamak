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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yaml.snakeyaml.Yaml;

@SuppressWarnings("unchecked")
public class InstanceConfiguration {

	private static final Log log = LogFactory.getLog(InstanceConfiguration.class);
	private static final String FILENAME = "acceptance.yml";

	private static Map<String, Map<String, String>> config;

	static {
		load(FILENAME);
	}

	public static void load(String filename) {
		InputStream inputStream = InstanceConfiguration.class.getClassLoader().getResourceAsStream(filename);

		try {
			if (inputStream != null) {
				config = (Map<String, Map<String, String>>) new Yaml().load(inputStream);
			}
		}
		catch (Exception e) {
			log.error("Unable to load " + filename + " file:", e);
		}
	}

	public static List<Instance> instances() {
		List<Instance> instances = new ArrayList<>();

		for (Map.Entry<String, Map<String, String>> entry : config.entrySet()) {
			String service = entry.getKey();

			Map<String, String> map = entry.getValue();
			String path = map.get("path");
			String ping = map.get("ping");

			Instance instance = new Instance(service, path, ping);

			if (map.containsKey("start")) {
				instance.setStart(map.get("start"));
			}
			if (map.containsKey("stop")) {
				instance.setStop(map.get("stop"));
			}
			
			instances.add(instance);
		}

		return instances;
	}

}
