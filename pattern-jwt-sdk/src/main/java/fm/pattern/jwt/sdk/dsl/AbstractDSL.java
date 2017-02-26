package fm.pattern.jwt.sdk.dsl;

import fm.pattern.jwt.sdk.model.AccessTokenRepresentation;

@SuppressWarnings("unchecked")
public abstract class AbstractDSL<T, C> {

    private boolean persistent;
    private AccessTokenRepresentation token;

    public T thatIs() {
        return (T) this;
    }

    public T thatAre() {
        return (T) this;
    }

    public T and() {
        return (T) this;
    }

    public T persistent() {
        this.persistent = true;
        return (T) this;
    }

    public T withToken(AccessTokenRepresentation token) {
        this.token = token;
        return (T) this;
    }

    public T persistent(AccessTokenRepresentation token) {
        this.persistent = true;
        this.token = token;
        return (T) this;
    }

    public AccessTokenRepresentation getToken() {
        return token;
    }

    public boolean shouldPersist() {
        return persistent;
    }

    public abstract C build();

}
