package fm.pattern.acceptance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public final class InstanceManagementService {

	private static final Log log = LogFactory.getLog(InstanceManagementService.class);

	private InstanceManagementService() {

	}

	public static void start(Instance instance) {
		String userDirectory = getRootDirectory(System.getProperty("user.dir"));
		String startScript = userDirectory + instance.getPath() + instance.getStart();
		log.info("using start script: " + startScript);
		execute(startScript);
	}

	public static void stop(Instance instance) {
		String userDirectory = getRootDirectory(System.getProperty("user.dir"));
		String startScript = userDirectory + instance.getPath() + instance.getStop();
		log.info("using stop script: " + startScript);
		execute(startScript);
	}

	public static boolean isRunning(Instance instance) {
		HttpResponse<String> response;

		try {
			response = Unirest.get(instance.getPing()).asString();
			return response.getStatus() == 200;
		}
		catch (UnirestException e) {
			return false;
		}

	}

	private static void execute(String command) {
		try {

			Map<String, String> map = new HashMap<>();
			map.putAll(System.getenv());

			String path = map.get("PATH");
			String newPath = path += ":/usr/local/bin";
			map.put("PATH", newPath);

			EnvironmentVariable.setEnv(map);

			ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
			builder.environment();
			builder.redirectErrorStream(true);

			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;

			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				log.info(line);
				sb.append(line);
			}

			process.waitFor();
			int exitValue = process.exitValue();
			process.destroy();

			if (exitValue != 0 && exitValue != 7) {
				throw new IllegalStateException("The script finished with exit code: " + exitValue + sb.toString());
			}
		}
		catch (Exception e) {
			throw new IllegalStateException("Cannot start tests due to build failure: ", e);
		}
	}

	private static String getRootDirectory(String currentDirectory) {
		String userRootDirectory1 = currentDirectory.substring(0, currentDirectory.lastIndexOf("/"));
		String userRootDirectory2 = userRootDirectory1.substring(0, userRootDirectory1.lastIndexOf("/"));
		return userRootDirectory2 + "/";
	}

}
