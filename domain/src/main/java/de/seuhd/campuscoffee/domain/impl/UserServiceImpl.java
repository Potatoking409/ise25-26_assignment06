package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.exceptions.DuplicationException;
import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.ports.UserDataService;
import de.seuhd.campuscoffee.domain.ports.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDataService userDataService;
    public void clear() {
        log.warn("Clearing all User data");
        userDataService.clear();
    }

    @Override
    public @NonNull List<User> getAll() {
        log.debug("Retrieving all POS");
        return userDataService.getAll();
    }

    @Override
    public @NonNull User getById(@NonNull Long id) {
        log.debug("Retrieving User with ID: {}", id);
        return userDataService.getById(id);
    }

    @Override
    public @NonNull User getByLoginName(@NonNull String loginName) {
        log.debug("Retrieving User with name: {}", loginName);
        return userDataService.getByLoginName(loginName);
    }

    @Override
    public @NonNull User upsert(@NonNull User user) {
        if (user.id() == null) {
            // create a new User
            log.info("Creating new User: {}", user.loginName());
        } else {
            // update an existing User
            log.info("Updating User with ID: {}", user.id());
            // User ID must be set
            Objects.requireNonNull(user.id());
            // User must exist in the database before the update
            userDataService.getById(user.id());
        }
        return performUpsert(user);
    }

    @Override
    public void delete(@NonNull Long id) {
        log.info("Trying to delete User with ID: {}", id);
        userDataService.delete(id);
        log.info("Deleted User with ID: {}", id);
    }

    private @NonNull User performUpsert(@NonNull User user) {
        try {
            User upsertedUser = userDataService.upsert(user);
            log.info("Successfully upserted User with ID: {}", upsertedUser.id());
            return upsertedUser;
        } catch (DuplicationException e) {
            log.error("Error upserting User '{}': {}", user.loginName(), e.getMessage());
            throw e;
        }
    }
}
