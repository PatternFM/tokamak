package fm.pattern.tokamak.sdk.model;

import java.util.ArrayList;
import java.util.List;

import fm.pattern.tokamak.sdk.commons.Representation;

public class AudiencesRepresentation extends Representation {

	private List<AudienceRepresentation> audiences = new ArrayList<AudienceRepresentation>();

	public AudiencesRepresentation() {

	}

	public AudiencesRepresentation(List<AudienceRepresentation> audiences) {
		this.audiences = audiences;
	}

	public List<AudienceRepresentation> getAudiences() {
		return audiences;
	}

	public void setAudiences(List<AudienceRepresentation> audiences) {
		this.audiences = audiences;
	}

}
