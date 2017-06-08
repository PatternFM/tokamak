package fm.pattern.tokamak.server.conversion;

import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.model.AudienceRepresentation;
import fm.pattern.tokamak.server.model.Audience;

@Service
public class AudienceConversionService {

	public Audience convert(AudienceRepresentation representation) {
		Audience audience = new Audience(representation.getName());
		audience.setDescription(representation.getDescription());
		return audience;
	}

	public AudienceRepresentation convert(Audience audience) {
		AudienceRepresentation representation = new AudienceRepresentation(audience.getId());
		representation.setCreated(audience.getCreated());
		representation.setUpdated(audience.getUpdated());
		representation.setName(audience.getName());
		representation.setDescription(audience.getDescription());
		return representation;
	}

	public Audience convert(AudienceRepresentation representation, Audience audience) {
		audience.setName(representation.getName());
		audience.setDescription(representation.getDescription());
		return audience;
	}

}
