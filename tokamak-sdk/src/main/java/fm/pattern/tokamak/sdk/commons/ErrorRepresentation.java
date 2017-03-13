package fm.pattern.tokamak.sdk.commons;

public class ErrorRepresentation extends Representation {

    private String code;
    private String message;

    public ErrorRepresentation() {

    }

    public ErrorRepresentation(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
