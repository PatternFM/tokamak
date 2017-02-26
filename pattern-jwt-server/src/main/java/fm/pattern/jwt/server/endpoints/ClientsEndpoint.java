package fm.pattern.jwt.server.endpoints;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.jwt.sdk.model.ClientRepresentation;
import fm.pattern.jwt.server.conversion.EgressConversionService;
import fm.pattern.jwt.server.conversion.IngressConversionService;
import fm.pattern.jwt.server.model.Client;
import fm.pattern.jwt.server.service.ClientService;

@RestController
public class ClientsEndpoint extends Endpoint {

	private final ClientService clientService;
	private final IngressConversionService ingress;
	private final EgressConversionService egress;

	@Autowired
	public ClientsEndpoint(ClientService clientService, IngressConversionService ingress, EgressConversionService egress) {
		this.clientService = clientService;
		this.ingress = ingress;
		this.egress = egress;
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/v1/clients", method = POST, consumes = "application/json", produces = "application/json")
	public ClientRepresentation create(@RequestBody ClientRepresentation representation) {
		Client client = ingress.convert(representation);
		return egress.convert(validate(clientService.create(client)));
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/v1/clients/{id}", method = PUT, consumes = "application/json", produces = "application/json")
	public ClientRepresentation update(@PathVariable String id, @RequestBody ClientRepresentation representation) {
		Client client = ingress.update(representation, validate(clientService.findById(id)));
		return egress.convert(validate(clientService.update(client)));
	}

}
