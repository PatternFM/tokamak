package fm.pattern.tokamak.sdk.model;

public class PasswordPolicyRepresentation extends EntityRepresentation {

	private String name;
	private String description;
	private Integer minLength;
	private boolean requireUppercaseCharacter;
	private boolean requireLowercaseCharacter;
	private boolean requireNumericCharacter;
	private boolean requireSpecialCharacter;
	private boolean rejectCommonPasswords;

	public PasswordPolicyRepresentation() {

	}

	public PasswordPolicyRepresentation(String id) {
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

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public boolean isRequireUppercaseCharacter() {
		return requireUppercaseCharacter;
	}

	public void setRequireUppercaseCharacter(boolean requireUppercaseCharacter) {
		this.requireUppercaseCharacter = requireUppercaseCharacter;
	}

	public boolean isRequireLowercaseCharacter() {
		return requireLowercaseCharacter;
	}

	public void setRequireLowercaseCharacter(boolean requireLowercaseCharacter) {
		this.requireLowercaseCharacter = requireLowercaseCharacter;
	}

	public boolean isRequireNumericCharacter() {
		return requireNumericCharacter;
	}

	public void setRequireNumericCharacter(boolean requireNumericCharacter) {
		this.requireNumericCharacter = requireNumericCharacter;
	}

	public boolean isRequireSpecialCharacter() {
		return requireSpecialCharacter;
	}

	public void setRequireSpecialCharacter(boolean requireSpecialCharacter) {
		this.requireSpecialCharacter = requireSpecialCharacter;
	}

	public boolean isRejectCommonPasswords() {
		return rejectCommonPasswords;
	}

	public void setRejectCommonPasswords(boolean rejectCommonPasswords) {
		this.rejectCommonPasswords = rejectCommonPasswords;
	}

}
