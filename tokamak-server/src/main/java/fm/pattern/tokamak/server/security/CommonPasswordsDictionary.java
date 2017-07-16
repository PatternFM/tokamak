package fm.pattern.tokamak.server.security;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public final class CommonPasswordsDictionary {

	private static List<String> passwords;

	static {
		try {
			passwords = Files.readAllLines(Paths.get(CommonPasswordsDictionary.class.getClassLoader().getResource("common-passwords.txt").toURI()), Charset.defaultCharset());
		}
		catch (Exception e) {
			passwords = new ArrayList<String>();
		}
	}

	private CommonPasswordsDictionary() {

	}

	public static List<String> list() {
		return passwords;
	}

	public static boolean contains(String password) {
		return StringUtils.isBlank(password) ? false : !passwords.parallelStream().filter(pw -> pw.equalsIgnoreCase(password)).collect(Collectors.toList()).isEmpty();
	}

}
