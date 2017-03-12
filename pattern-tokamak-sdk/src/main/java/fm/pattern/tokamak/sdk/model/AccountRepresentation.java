package fm.pattern.tokamak.sdk.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import fm.pattern.tokamak.sdk.commons.Representation;

public class AccountRepresentation extends Representation {

	private String id;
	private Date created;
	private Date updated;

	private String username;
	private String password;
	private boolean locked;

	private Set<RoleRepresentation> roles = new HashSet<RoleRepresentation>();

	public AccountRepresentation() {

	}

	public AccountRepresentation(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public Set<RoleRepresentation> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleRepresentation> roles) {
		this.roles = roles;
	}

}
