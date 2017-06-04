package fm.pattern.tokamak.sdk.model;

import java.util.HashSet;
import java.util.Set;

public class AccountRepresentation extends EntityRepresentation {

    private String username;
    private String password;
    private boolean locked;

    private Set<RoleRepresentation> roles = new HashSet<RoleRepresentation>();

    public AccountRepresentation() {

    }

    public AccountRepresentation(String id) {
        super(id);
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
