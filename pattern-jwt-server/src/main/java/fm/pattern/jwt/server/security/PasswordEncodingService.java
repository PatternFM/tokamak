package fm.pattern.jwt.server.security;

public interface PasswordEncodingService {

	String encode(String password);

	boolean matches(String raw, String encoded);

}
