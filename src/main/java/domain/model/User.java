package domain.model;

import org.jetbrains.annotations.NotNull;

public class User {
    private final @NotNull String uuid;

    public User(@NotNull String uuid) {
        this.uuid = uuid;
    }

    public @NotNull String getUuid() {
        return uuid;
    }
}
