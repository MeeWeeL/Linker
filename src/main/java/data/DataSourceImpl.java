package data;

import domain.model.Link;
import domain.model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DataSourceImpl implements DataSource {

    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<String, Link> links = new HashMap<>();
    private final ShortLinkGenerator shortLinkGenerator = new ShortLinkGenerator();

    @NotNull
    @Override
    public User createUser() {
        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (users.containsKey(uuid));
        User newUser = new User(uuid);
        users.put(uuid, newUser);
        return newUser;
    }

    @Nullable
    @Override
    public User getUser(String uuid) {
        return users.get(uuid);
    }

    @NotNull
    @Override
    public Link createLink(String link, String uuid) {
        String newShortLink;
        do {
            newShortLink = shortLinkGenerator.generateShortLink();
        } while (links.containsKey(newShortLink));
        Link newLink = new Link(uuid, link, newShortLink);
        links.put(newShortLink, newLink);
        return newLink;
    }

    @Nullable
    @Override
    public Link getLink(String shortLink) {
        return links.get(shortLink);
    }

    @NotNull
    @Override
    public List<Link> getAllLinks(@NotNull String uuid) {
        return links.values().stream().filter(link -> link.getUserUuid().equals(uuid)).collect(Collectors.toList());
    }

    @Override
    public void deleteLink(String shortLink) {
        links.remove(shortLink);
    }

    @Override
    public void updateLinkLimitCount(Link link, Integer newCount) {
        links.replace(
            link.getShortLink(),
            new Link(
                /* userUuid */ link.getUserUuid(),
                /* link */ link.getLink(),
                /* shortLink */ link.getShortLink(),
                /* currentLimitCount */ newCount
            )
        );
    }
}
