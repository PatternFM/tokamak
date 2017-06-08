package fm.pattern.tokamak.server.conversion;

import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.model.ScopeRepresentation;
import fm.pattern.tokamak.server.model.Scope;

@Service
public class ScopeConversionService {

	public Scope convert(ScopeRepresentation representation) {
		Scope audience = new Scope(representation.getName());
		audience.setDescription(representation.getDescription());
		return audience;
	}

	public ScopeRepresentation convert(Scope audience) {
		ScopeRepresentation representation = new ScopeRepresentation(audience.getId());
		representation.setCreated(audience.getCreated());
		representation.setUpdated(audience.getUpdated());
		representation.setName(audience.getName());
		representation.setDescription(audience.getDescription());
		return representation;
	}

	public Scope convert(ScopeRepresentation representation, Scope audience) {
		audience.setName(representation.getName());
		audience.setDescription(representation.getDescription());
		return audience;
	}

}
