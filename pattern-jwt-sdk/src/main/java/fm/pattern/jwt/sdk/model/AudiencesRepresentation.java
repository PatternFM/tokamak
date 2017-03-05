package fm.pattern.jwt.sdk.model;

import java.util.ArrayList;
import java.util.List;

public class AudiencesRepresentation {

    private List<AudienceRepresentation> audiences = new ArrayList<AudienceRepresentation>();

    public AudiencesRepresentation() {

    }

    public AudiencesRepresentation(List<AudienceRepresentation> audiences) {
        this.audiences = audiences;
    }

    public List<AudienceRepresentation> getAuthorities() {
        return audiences;
    }

    public void setAuthorities(List<AudienceRepresentation> audiences) {
        this.audiences = audiences;
    }

}
