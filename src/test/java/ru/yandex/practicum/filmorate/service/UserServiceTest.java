package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.friendship.FriendshipStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserServiceTest {
    private final UserService userService;
    private final FriendshipStorage friendshipStorage;

    @Test
    void userStorageCanReturnUsersList() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        userService.addUser(user);
        assertEquals(1, userService.getUsers().size());
    }

    @Test
    void userStorageCanAddNewUser() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = userService.addUser(user);
        assertNotNull(user1);
    }

    @Test
    void userStorageCantAddNewUserIfLoginContainWhitespace() {
        User user = User.builder().email("abv@mail.ru").login("a bv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        assertThrows(ValidationException.class, () -> userService.addUser(user));
    }

    @Test
    void userStorageWriteLoginToNameIfNewUserWithoutName() {
        User user = User.builder().email("abv@mail.ru").login("abv")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = userService.addUser(user);
        assertEquals(user1.getName(), user1.getLogin());
    }

    @Test
    void userStorageCantUpdateUserIfUserWithNewInformationWithoutId() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        userService.addUser(user);
        User updatedUser = User.builder().email("abc@mail.ru").login("abc").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        assertThrows(ValidationException.class, () -> userService.updateUser(updatedUser));
    }

    @Test
    void userStorageCantUpdateUserIfIdOfNewUserNotFound() {
        User user = User.builder().id(2).email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        assertThrows(NotFoundException.class, () -> userService.updateUser(user));
    }

    @Test
    void userStorageCanUpdateUser() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        userService.addUser(user);
        User updatedUser = User.builder().id(1).email("abc@mail.ru").login("abc").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = userService.updateUser(updatedUser);
        assertEquals(updatedUser, user1);
    }

    @Test
    void testCanFindUserById() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = userService.addUser(user);
        User user2 = userService.getUserById(user1.getId());
        assertEquals(user2.getName(), user1.getName());
    }

    @Test
    void testCanAddFriend() {
        userService.addUser(User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build());
        userService.addUser(User.builder().email("abvg@mail.ru").login("abvg").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build());
        userService.addFriend(1, 2);
        assertEquals(2, friendshipStorage.getFriendshipOfUser(1).stream().findFirst().get());
    }

    @Test
    void testCantAddNotExistFriend() {
        userService.addUser(User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build());
        assertThrows(NotFoundException.class, () -> userService.addFriend(1, 2));
    }

    @Test
    void testUnknownUserCantAddFriend() {
        userService.addUser(User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build());
        assertThrows(NotFoundException.class, () -> userService.addFriend(2, 1));
    }

    @Test
    void testCanDeleteFriend() {
        userService.addUser(User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build());
        userService.addUser(User.builder().email("abvg@mail.ru").login("abvg").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build());
        userService.addFriend(1, 2);
        userService.deleteFriend(1, 2);
        assertTrue(friendshipStorage.getFriendshipOfUser(1).isEmpty());
    }

    @Test
    void testCanGetUserFriends() {
        userService.addUser(User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build());
        userService.addUser(User.builder().email("abvg@mail.ru").login("abvg").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build());
        userService.addFriend(1, 2);
        List<User> friends = userService.getUserFriends(1);
        assertEquals(2, friends.getFirst().getId());
    }

    @Test
    void testCanGetMutualFriends() {
        userService.addUser(User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build());
        userService.addUser(User.builder().email("abvg@mail.ru").login("abvg").name("roy")
                .birthday(LocalDate.of(1994, 12, 4)).build());
        userService.addUser(User.builder().email("abvgd@mail.ru").login("abvgd").name("roys")
                .birthday(LocalDate.of(1993, 12, 4)).build());
        userService.addFriend(1, 3);
        userService.addFriend(2, 3);
        List<User> mutualFriends = userService.getMutualFriends(1, 2);
        assertEquals(3, mutualFriends.getFirst().getId());
    }
}