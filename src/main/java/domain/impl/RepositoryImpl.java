package domain.impl;

import data.DataSource;
import data.DataSourceImpl;
import domain.model.Link;
import domain.model.User;
import domain.model.auth.Auth;
import domain.model.auth.AuthState;
import domain.model.auth.NoAuth;
import domain.repository.Repository;
import kotlinx.coroutines.flow.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RepositoryImpl implements Repository {
    private static final int LIFE_TIME_SECONDS = 35;

    private final @NotNull AuthStateManager authManager = new AuthStateManager();
    private final @NotNull DataSource localData = new DataSourceImpl();
    private final @NotNull LinkListService linkListService = new LinkListService();
    private final @NotNull MessageManager messageManager = new MessageManager();

    @Override
    public @NotNull Flow<? extends AuthState> getAuthState() {
        checkTimes();
        return authManager.getAuthState();
    }

    @Override
    public @NotNull Boolean auth(@NotNull String uuid) {
        checkTimes();
        User user = localData.getUser(uuid);
        if (user != null) {
            authManager.updateAuthState(new Auth(user));
            updateLinks();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void logout() {
        checkTimes();
        authManager.updateAuthState(new NoAuth());
        updateLinks();
    }

    @Override
    public @NotNull String convertLink(@NotNull String link) {
        checkTimes();
        User user;
        AuthState state = authManager.getActualState();
        if (state instanceof NoAuth) {
            user = localData.createUser();
        } else {
            try {
                user = ((Auth) state).getUser();
            } catch (Exception e) {
                user = localData.createUser();
            }
        }
        authManager.updateAuthState(new Auth(user));
        String newLink = localData.createLink(link, user.getUuid()).getShortLink();
        updateLinks();
        return newLink;
    }

    @Override
    public @Nullable String decodeLink(@NotNull String shortLink) {
        checkTimes();
        try {
            Link link = Objects.requireNonNull(localData.getLink(shortLink));
            int newCount = link.getCurrentLimitCount() - 1;
            if (newCount < 1) {
                localData.deleteLink(link.getShortLink());
                messageManager.sendMessage("Ссылка " + link.getShortLink() + " удалена");
                return null;
            } else {
                localData.updateLinkLimitCount(link, link.getCurrentLimitCount() - 1);
            }
            return link.getLink();
        } catch (Exception e) {
            return null;
        } finally {
            updateLinks();
        }
    }

    @Override
    public @NotNull Flow<List<Link>> getAllLinks() {
        checkTimes();
        return linkListService.getLinks();
    }

    @Override
    public void deleteLink(@NotNull String shortLink) {
        checkTimes();
        localData.deleteLink(shortLink);
        messageManager.sendMessage("Ссылка " + shortLink + " удалена");
        updateLinks();
    }

    @Override
    public void updateLinkLimitCount(@NotNull Link link, @NotNull Integer newCount) {
        checkTimes();
        if (newCount < 1) {
            localData.deleteLink(link.getShortLink());
            messageManager.sendMessage("Ссылка " + link.getShortLink() + " удалена");
        } else {
            localData.updateLinkLimitCount(link, newCount);
        }
        updateLinks();
    }

    @NotNull
    @Override
    public Flow<String> getMessages() {
        checkTimes();
        return messageManager.getMessages();
    }

    private void updateLinks() {
        checkTimes();
        AuthState authState = authManager.getActualState();
        if (authState instanceof Auth) {
            linkListService.updateLinkList(localData.getAllLinks(((Auth) authState).getUser().getUuid()));
        } else {
            linkListService.updateLinkList(Collections.emptyList());
        }
    }

    private void checkTimes() {
        AuthState authState = authManager.getActualState();
        if (authState instanceof Auth) {
            for (Link link : localData.getAllLinks(((Auth) authState).getUser().getUuid())) {
                long millis = link.getCreateDate().getTimeInMillis();
                Calendar currentDate = Calendar.getInstance();
                currentDate.add(Calendar.SECOND, -LIFE_TIME_SECONDS);
                if (millis < currentDate.getTimeInMillis()) {
                    localData.deleteLink(link.getShortLink());
                    messageManager.sendMessage("Ссылка " + link.getShortLink() + " удалена");
                }
            }
        }
    }
}
