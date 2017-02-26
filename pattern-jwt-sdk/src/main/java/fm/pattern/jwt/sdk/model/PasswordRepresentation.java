package fm.pattern.jwt.sdk.model;

import fm.pattern.commons.rest.Representation;

public class PasswordRepresentation extends Representation {

	private String currentPassword;
	private String newPassword;

	public PasswordRepresentation() {

	}

	public PasswordRepresentation(String currentPassword, String newPassword) {
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
