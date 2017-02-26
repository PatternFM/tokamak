package fm.pattern.jwt.server.endpoints;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.jwt.sdk.model.AccountRepresentation;
import fm.pattern.jwt.server.conversion.EgressConversionService;
import fm.pattern.jwt.server.conversion.IngressConversionService;
import fm.pattern.jwt.server.model.Account;
import fm.pattern.jwt.server.service.AccountService;

@RestController
public class AccountsEndpoint extends Endpoint {

	private final AccountService accountService;
	private final IngressConversionService ingress;
	private final EgressConversionService egress;

	@Autowired
	public AccountsEndpoint(AccountService accountService, IngressConversionService ingress, EgressConversionService egress) {
		this.accountService = accountService;
		this.ingress = ingress;
		this.egress = egress;
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/v1/accounts", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public AccountRepresentation create(@RequestBody AccountRepresentation representation) {
		Account account = ingress.convert(representation);
		return egress.convert(validate(accountService.create(account)));
	}

	@RequestMapping(value = "/v1/accounts/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public AccountRepresentation findById(@PathVariable String id) {
		return egress.convert(validate(accountService.findById(id)));
	}

}
