package fm.pattern.tokamak.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class StaticApplicationContextHolder {

	private static ApplicationContext CONTEXT;

	@Autowired
	public void setApplicationContext(ApplicationContext context) {
		CONTEXT = context;
	}

	public static <T> T get(Class<T> type) {
		return CONTEXT.getBean(type);
	}

}
