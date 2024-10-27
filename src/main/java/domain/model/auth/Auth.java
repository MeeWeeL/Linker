package domain.model.auth;

import domain.model.User;
import org.jetbrains.annotations.NotNull;

public class Auth implements AuthState {
    private final @NotNull User user;

    public Auth(@NotNull User user) {
        this.user = user;
    }

    public @NotNull User getUser() {
        return user;
    }
}
