package fm.pattern.tokamak.sdk.model;

public class RoleRepresentation extends EntityRepresentation {

	private String name;
	private String description;

	public RoleRepresentation() {

	}

	public RoleRepresentation(String id) {
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
