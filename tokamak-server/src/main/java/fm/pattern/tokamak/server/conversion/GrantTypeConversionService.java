package fm.pattern.tokamak.server.conversion;

import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.model.GrantTypeRepresentation;
import fm.pattern.tokamak.server.model.GrantType;

@Service
public class GrantTypeConversionService {

	public GrantTypeRepresentation convert(GrantType grantType) {
		GrantTypeRepresentation representation = new GrantTypeRepresentation(grantType.getId());
		representation.setCreated(grantType.getCreated());
		representation.setUpdated(grantType.getUpdated());
		representation.setName(grantType.getName());
		representation.setDescription(grantType.getDescription());
		return representation;
	}

	public GrantType convert(GrantTypeRepresentation representation) {
		GrantType grantType = new GrantType(representation.getName());
		grantType.setDescription(representation.getDescription());
		return grantType;
	}

	public GrantType convert(GrantTypeRepresentation representation, GrantType grantType) {
		grantType.setName(representation.getName());
		grantType.setDescription(representation.getDescription());
		return grantType;
	}

}
