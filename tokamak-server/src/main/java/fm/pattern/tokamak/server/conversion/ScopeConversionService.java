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

	public ScopeRepresentation convert(Scope scope) {
		ScopeRepresentation representation = new ScopeRepresentation(scope.getId());
		representation.setCreated(scope.getCreated());
		representation.setUpdated(scope.getUpdated());
		representation.setName(scope.getName());
		representation.setDescription(scope.getDescription());
		return representation;
	}

	public Scope convert(ScopeRepresentation representation, Scope scope) {
		scope.setName(representation.getName());
		scope.setDescription(representation.getDescription());
		return scope;
	}

}
