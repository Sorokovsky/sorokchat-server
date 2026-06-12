package ag.sorokchat.users.model;

public enum Role {
    USER("USER"),
    PRO("PRO"),
    ADMIN("ADMIN");

    private String value;

    Role(String value) {
        this.value = value;
    }
}
