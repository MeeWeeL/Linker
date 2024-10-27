package domain.repository;

import domain.model.Link;
import domain.model.auth.AuthState;
import kotlinx.coroutines.flow.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Repository {

    @NotNull
    Flow<? extends AuthState> getAuthState();

    @NotNull
    Boolean auth(@NotNull String uuid);

    void logout();

    @NotNull
    String convertLink(@NotNull String link);

    @Nullable
    String decodeLink(@NotNull String shortLink);

    @NotNull
    Flow<List<Link>> getAllLinks();

    void deleteLink(@NotNull String shortLink);

    void updateLinkLimitCount(@NotNull Link link, @NotNull Integer newCount);

    @NotNull
    Flow<String> getMessages();
}
