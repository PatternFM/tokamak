package fm.pattern.tokamak.sdk.model;

import java.util.ArrayList;
import java.util.List;

import fm.pattern.tokamak.sdk.commons.Representation;

public class AuthoritiesRepresentation extends Representation {

    private List<AuthorityRepresentation> authorities = new ArrayList<AuthorityRepresentation>();

    public AuthoritiesRepresentation() {

    }

    public AuthoritiesRepresentation(List<AuthorityRepresentation> authorities) {
        this.authorities = authorities;
    }

    public List<AuthorityRepresentation> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityRepresentation> authorities) {
        this.authorities = authorities;
    }

}
