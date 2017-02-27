package fm.pattern.jwt.sdk.model;

import java.util.Date;

import fm.pattern.commons.rest.Representation;

public class GrantTypeRepresentation extends Representation {

    private String id;
    private Date created;
    private Date updated;

    private String name;
    private String description;

    public GrantTypeRepresentation() {

    }

    public GrantTypeRepresentation(String id) {
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
