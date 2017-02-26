package fm.pattern.jwt.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class PasswordEncodingServiceImpl implements PasswordEncodingService {

	private PasswordEncoder passwordEncoder;

	public String encode(String password) {
		return passwordEncoder.encode(password);
	}

	public boolean matches(String raw, String encoded) {
		return passwordEncoder.matches(raw, encoded);
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}
