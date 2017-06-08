package fm.pattern.tokamak.server.conversion;

import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.model.AuthorityRepresentation;
import fm.pattern.tokamak.server.model.Authority;

@Service
public class AuthorityConversionService {

	public Authority convert(AuthorityRepresentation representation) {
		Authority authority = new Authority(representation.getName());
		authority.setDescription(representation.getDescription());
		return authority;
	}

	public AuthorityRepresentation convert(Authority authority) {
		AuthorityRepresentation representation = new AuthorityRepresentation(authority.getId());
		representation.setCreated(authority.getCreated());
		representation.setUpdated(authority.getUpdated());
		representation.setName(authority.getName());
		representation.setDescription(authority.getDescription());
		return representation;
	}

	public Authority convert(AuthorityRepresentation representation, Authority authority) {
		authority.setName(representation.getName());
		authority.setDescription(representation.getDescription());
		return authority;
	}

}
