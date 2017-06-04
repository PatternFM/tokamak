package fm.pattern.tokamak.sdk.model;

import java.util.Date;

import fm.pattern.tokamak.sdk.commons.Representation;

public class EntityRepresentation extends Representation {

    private String id;
    private Date created;
    private Date updated;

    public EntityRepresentation() {

    }

    public EntityRepresentation(String id) {
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

}
