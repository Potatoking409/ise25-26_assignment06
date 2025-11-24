package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.User;
import org.jspecify.annotations.NonNull;
import de.seuhd.campuscoffee.domain.exceptions.NotFoundException;

import java.util.List;

/**
 * Port interface for user data operations.
 * This port is implemented by the data layer (adapter) and defines the contract
 * for persistence operations on user entities.
 * Follows the hexagonal architecture pattern where the domain defines the port
 * and the infrastructure layer provides the adapter implementation.
 */
public interface UserDataService {



    /**
     * Clears all user data from the data store.
     * This is typically used for testing or administrative purposes.
     * Warning: This operation is destructive and cannot be undone.
     */
    void clear();

    /**
     * Retrieves all user entities from the data store and returns them as domain objects.
     *
     * @return a list of all user entities; never null, but may be empty
     */
    @NonNull List<User> getAll();

    /**
     * Retrieves a single user entity by its unique identifier and returns it as a domain object.
     *
     * @param id the unique identifier of the user to retrieve; must not be null
     * @return the user with the specified ID; never null
     * @throws NotFoundException if no user exists with the given ID
     */
    @NonNull User getById(@NonNull Long id);

    /**
     * Retrieves a single user entity by its unique login name and returns it as a domain object.
     *
     * @param loginName the login name of the user to retrieve; must not be null
     * @return the user with the specified login name; never null
     * @throws NotFoundException if no user exists with the given login name
     */
    @NonNull User getByLoginName(@NonNull String loginName);

    /**
     * Creates a new user or updates an existing one.
     * If the user has an ID and exists in the data store, it will be updated.
     * If the user has no ID (null), a new user will be created.
     *
     * @param user the user to create or update; must not be null
     * @return the persisted user entity with updated timestamps and ID as a domain object; never null
     * @throws NotFoundException if attempting to update a user that does not exist
     */
    @NonNull User upsert(@NonNull User user);

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to delete; must not be null
     * @throws NotFoundException if no user exists with the given ID
     */
    void delete(@NonNull Long id);
}
