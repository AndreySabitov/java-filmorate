package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {
    UserStorage userStorage;

    @BeforeEach
    void initUserStorage() {
        userStorage = new InMemoryUserStorage();
    }

    @Test
    void userStorageCanReturnUsersList() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        userStorage.addUser(user);
        assertEquals(1, userStorage.getUsers().size());
    }

    @Test
    void userStorageCanAddNewUser() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = userStorage.addUser(user);
        assertNotNull(user1);
    }

    @Test
    void userStorageCantAddNewUserIfEmailDuplicate() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = User.builder().email("abv@mail.ru").name("vba").login("vba")
                .birthday(LocalDate.of(1988, 10, 2)).build();
        assertThrows(DuplicateException.class, () -> {
            userStorage.addUser(user);
            userStorage.addUser(user1);
        });
    }

    @Test
    void userStorageCantAddNewUserIfLoginContainWhitespace() {
        User user = User.builder().email("abv@mail.ru").login("a bv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        assertThrows(ValidationException.class, () -> userStorage.addUser(user));
    }

    @Test
    void userStorageWriteLoginToNameIfNewUserWithoutName() {
        User user = User.builder().email("abv@mail.ru").login("abv")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = userStorage.addUser(user);
        assertEquals(user1.getName(), user1.getLogin());
    }

    @Test
    void userStorageCantUpdateUserIfUserWithNewInformationWithoutId() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        userStorage.addUser(user);
        User updatedUser = User.builder().email("abc@mail.ru").login("abc").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        assertThrows(ValidationException.class, () -> userStorage.updateUser(updatedUser));
    }

    @Test
    void userStorageCantUpdateUserIfIdOfNewUserNotFound() {
        User user = User.builder().id(2).email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        assertThrows(NotFoundException.class, () -> userStorage.updateUser(user));
    }

    @Test
    void userStorageCanUpdateUser() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        userStorage.addUser(user);
        User updatedUser = User.builder().id(1).email("abc@mail.ru").login("abc").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = userStorage.updateUser(updatedUser);
        assertEquals(updatedUser, user1);
    }
}