package fm.pattern.jwt.server.endpoints;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fm.pattern.jwt.sdk.model.GrantTypeRepresentation;
import fm.pattern.jwt.sdk.model.GrantTypesRepresentation;
import fm.pattern.jwt.server.conversion.EgressConversionService;
import fm.pattern.jwt.server.model.GrantType;
import fm.pattern.jwt.server.service.GrantTypeService;

@RestController
public class GrantTypesEndpoint extends Endpoint {

	private final GrantTypeService grantService;
	private final EgressConversionService egress;

	@Autowired
	public GrantTypesEndpoint(GrantTypeService grantService, EgressConversionService egress) {
		this.grantService = grantService;
		this.egress = egress;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/v1/grants/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public GrantTypeRepresentation findById(@PathVariable String id) {
		GrantType grant = validate(grantService.findById(id));
		return egress.convert(grant);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/v1/grants", method = GET, produces = APPLICATION_JSON_VALUE)
	public GrantTypesRepresentation list() {
		List<GrantType> grants = validate(grantService.list());
		return new GrantTypesRepresentation(grants.stream().map(grant -> egress.convert(grant)).collect(Collectors.toList()));
	}

}
