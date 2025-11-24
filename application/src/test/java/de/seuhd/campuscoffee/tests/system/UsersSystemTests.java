package de.seuhd.campuscoffee.tests.system;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.tests.TestFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

import static de.seuhd.campuscoffee.tests.SystemTestUtils.Requests.userRequests;
import static org.assertj.core.api.Assertions.assertThat;

public class UsersSystemTests extends AbstractSysTest {


    @Test
    void createUser() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        User createdUser = userDtoMapper.toDomain(userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate))).getFirst());

        assertEqualsIgnoringIdAndTimestamps(createdUser, userToCreate);
    }

    @Test
    void getAllCreatedUsers() {
        List<User> createdPosList = TestFixtures.createUsers(userService);

        List<User> retrievedPos = userRequests.retrieveAll()
                .stream()
                .map(userDtoMapper::toDomain)
                .toList();

        assertEqualsIgnoringTimestamps(retrievedPos, createdPosList);
    }

    @Test
    void getUserById() {
        List<User> createdUserList = TestFixtures.createUsers(userService);
        User createdUser = createdUserList.getFirst();

        User retrievedUser = userDtoMapper.toDomain(
                userRequests.retrieveById(createdUser.id())
        );

        assertEqualsIgnoringTimestamps(retrievedUser, createdUser);
    }

    @Test
    void filterUserByLoginName() {
        List<User> createdUserList = TestFixtures.createUsers(userService);
        User createdUser = createdUserList.getFirst();
        String userName = createdUser.loginName();
        User filteredUser = userDtoMapper.toDomain(userRequests.retrieveByFilter("loginName", userName));

        assertEqualsIgnoringTimestamps(filteredUser, createdUser);
    }

    @Test
    void updateUser() {
        List<User> createdUserList = TestFixtures.createUsers(userService);
        User userToUpdate = createdUserList.getFirst();

        // update fields using toBuilder() pattern (records are immutable)
        userToUpdate = userToUpdate.toBuilder()
                .loginName(userToUpdate.loginName() + "Updated")
                .emailAddress("updated@example.com")
                .build();

        User updatedPos = userDtoMapper.toDomain(userRequests.update(List.of(userDtoMapper.fromDomain(userToUpdate))).getFirst());

        assertEqualsIgnoringTimestamps(updatedPos, userToUpdate);

        // verify changes persist
        User retrievedPos = userDtoMapper.toDomain(userRequests.retrieveById(userToUpdate.id()));

        assertEqualsIgnoringTimestamps(retrievedPos, userToUpdate);
    }

    @Test
    void deleteUser() {
        List<User> createdUserList = TestFixtures.createUsers(userService);
        User userToDelete = createdUserList.getFirst();
        Objects.requireNonNull(userToDelete.id());

        List<Integer> statusCodes = userRequests.deleteAndReturnStatusCodes(List.of(userToDelete.id(), userToDelete.id()));

        // first deletion should return 204 No Content, second should return 404 Not Found
        assertThat(statusCodes)
                .containsExactly(HttpStatus.NO_CONTENT.value(), HttpStatus.NOT_FOUND.value());

        List<Long> remainingPosIds = userRequests.retrieveAll()
                .stream()
                .map(UserDto::id)
                .toList();
        assertThat(remainingPosIds)
                .doesNotContain(userToDelete.id());
    }
}