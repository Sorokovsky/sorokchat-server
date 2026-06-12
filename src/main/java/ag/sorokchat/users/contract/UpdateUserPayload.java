package ag.sorokchat.users.contract;

public record UpdateUserPayload(String login, String password, String displayName) {
}
