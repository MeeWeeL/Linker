package domain.model;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class Link {
    private static final int START_LIMIT_COUNT = 3;
    private final @NotNull String userUuid;
    private final @NotNull String link;
    private final @NotNull String shortLink;
    private final @NotNull Calendar createDate;
    private final @NotNull Integer currentLimitCount;

    public Link(
        @NotNull String userUuid,
        @NotNull String link,
        @NotNull String shortLink
    ) {
        this.userUuid = userUuid;
        this.link = link;
        this.shortLink = shortLink;
        currentLimitCount = START_LIMIT_COUNT;
        createDate = Calendar.getInstance();
    }

    public Link(
        @NotNull String userUuid,
        @NotNull String link,
        @NotNull String shortLink,
        @NotNull Integer currentLimitCount
    ) {
        this.userUuid = userUuid;
        this.link = link;
        this.shortLink = shortLink;
        this.currentLimitCount = currentLimitCount;
        createDate = Calendar.getInstance();
    }

    public @NotNull String getUserUuid() {
        return userUuid;
    }

    public @NotNull String getLink() {
        return link;
    }

    public @NotNull String getShortLink() {
        return shortLink;
    }

    public @NotNull Calendar getCreateDate() {
        return createDate;
    }

    public @NotNull Integer getCurrentLimitCount() {
        return currentLimitCount;
    }
}
