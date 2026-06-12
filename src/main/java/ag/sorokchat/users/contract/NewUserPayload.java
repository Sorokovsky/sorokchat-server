package ag.sorokchat.users.contract;

public record NewUserPayload(
        String login,
        String password,
        String displayName
) {
}
