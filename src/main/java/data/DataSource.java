package data;

import domain.model.Link;
import domain.model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DataSource {
    @NotNull
    User createUser();

    @Nullable
    User getUser(String uuid);

    @NotNull
    Link createLink(String link, String uuid);

    @Nullable
    Link getLink(String shortLink);

    @NotNull
    List<Link> getAllLinks(@NotNull String uuid);

    void deleteLink(String shortLink);

    void updateLinkLimitCount(Link link, Integer newCount);
}
