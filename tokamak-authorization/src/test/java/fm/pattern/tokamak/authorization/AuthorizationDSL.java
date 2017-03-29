package fm.pattern.tokamak.authorization;

import java.lang.annotation.Annotation;

@SuppressWarnings("all")
public class AuthorizationDSL extends AbstractDSL<AuthorizationDSL, Authorize> {

    private String scopes = "";
    private String authorities = "";
    private String roles = "";

    public static AuthorizationDSL authorize() {
        return new AuthorizationDSL();
    }

    public AuthorizationDSL withScopes(String scopes) {
        this.scopes = scopes;
        return this;
    }

    public AuthorizationDSL withAuthorities(String authorities) {
        this.authorities = authorities;
        return this;
    }

    public AuthorizationDSL withRoles(String roles) {
        this.roles = roles;
        return this;
    }

    public Authorize build() {
        return new AuthorizeAnnotation(scopes, authorities, roles);
    }

    public class AuthorizeAnnotation implements Authorize {

        private String scopes;
        private String authorities;
        private String roles;

        public AuthorizeAnnotation(String scopes, String authorities, String roles) {
            this.scopes = scopes;
            this.authorities = authorities;
            this.roles = roles;
        }

        public String authorities() {
            return authorities;
        }

        public String scopes() {
            return scopes;
        }

        public String roles() {
            return roles;
        }

        public Class<? extends Annotation> annotationType() {
            return AuthorizeAnnotation.class;
        }

    }

}
