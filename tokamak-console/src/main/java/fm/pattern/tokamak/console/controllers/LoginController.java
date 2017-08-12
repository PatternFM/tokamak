package fm.pattern.tokamak.console.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import fm.pattern.minimal.JSON;
import fm.pattern.tokamak.sdk.TokensClient;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AccessTokenRepresentation;

@RestController
public class LoginController extends WebController {

	private TokensClient tokensClient = new TokensClient("http://localhost:9600");
	private String clientId = "test-client";
	private String clientSecret = "bm46bksylwt2imkfbhgb";

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/login", method = POST, consumes = APPLICATION_JSON_VALUE)
	public AccessTokenRepresentation login(@RequestBody Credentials credentials, HttpSession session) {
		Result<AccessTokenRepresentation> result = tokensClient.getAccessToken(clientId, clientSecret, credentials.getUsername(), credentials.getPassword());
		session.setAttribute("token", JSON.stringify(verify(result).getInstance()));
		return result.getInstance();
	}

	@RequestMapping(value = "/logout", method = GET)
	public ModelAndView logout(HttpSession session) {
		session.setAttribute("token", null);
		session.invalidate();
		return new ModelAndView("redirect:/login");
	}

}
