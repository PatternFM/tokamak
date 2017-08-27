package fm.pattern.tokamak.console.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReactProxyController {

	@RequestMapping(value = "/login")
	public String login() {
		return "index.html";
	}
	
	@RequestMapping(value = "/logout")
	public String logout() {
		return "index.html";
	}
	
	@RequestMapping(value = "/apps")
	public String apps() {
		return "index.html";
	}

	@RequestMapping(value = "/scopes")
	public String scopes() {
		return "index.html";
	}

	@RequestMapping(value = "/authorities")
	public String authorities() {
		return "index.html";
	}

	@RequestMapping(value = "/audiences")
	public String audiences() {
		return "index.html";
	}

	@RequestMapping(value = "/accounts")
	public String accounts() {
		return "index.html";
	}

	@RequestMapping(value = "/roles")
	public String roles() {
		return "index.html";
	}
	
	@RequestMapping(value = "/policies")
	public String policies() {
		return "index.html";
	}
	
}
