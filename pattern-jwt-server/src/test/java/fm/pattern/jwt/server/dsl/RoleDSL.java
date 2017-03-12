package fm.pattern.jwt.server.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import fm.pattern.jwt.server.model.Role;
import fm.pattern.jwt.server.service.RoleService;
import fm.pattern.valex.Result;

public class RoleDSL extends AbstractDSL<RoleDSL, Role> {

	private String name = randomAlphabetic(10);
	private String description = null;
	
	public static RoleDSL role() {
		return new RoleDSL();
	}

	public RoleDSL withName(String name) {
		this.name = name;
		return this;
	}
	
	public RoleDSL withDescription(String description) {
		this.description = description;
		return this;
	}

	public Role build() {
		Role role = new Role(name);
		role.setDescription(description);
		return shouldPersist() ? persist(role) : role;
	}

	private Role persist(Role role) {
		Result<Role> result = load(RoleService.class).create(role);
		if (result.accepted()) {
			return result.getInstance();
		}
		throw new IllegalStateException("Unable to create role, errors:" + result.toString());
	}

}
