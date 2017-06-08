package fm.pattern.tokamak.server.conversion;

import org.springframework.stereotype.Service;

import fm.pattern.tokamak.sdk.model.RoleRepresentation;
import fm.pattern.tokamak.server.model.Role;

@Service
public class RoleConversionService {

	public Role convert(RoleRepresentation representation) {
		Role role = new Role(representation.getName());
		role.setDescription(representation.getDescription());
		return role;
	}

	public RoleRepresentation convert(Role role) {
		RoleRepresentation representation = new RoleRepresentation(role.getId());
		representation.setCreated(role.getCreated());
		representation.setUpdated(role.getUpdated());
		representation.setName(role.getName());
		representation.setDescription(role.getDescription());
		return representation;
	}

	public Role convert(RoleRepresentation representation, Role role) {
		role.setName(representation.getName());
		role.setDescription(representation.getDescription());
		return role;
	}

}
