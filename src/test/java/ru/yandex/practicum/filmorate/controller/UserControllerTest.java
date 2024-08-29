package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;

    @BeforeEach
    void initUserController() {
        userController = new UserController();
    }

    @Test
    void userControllerCanReturnUsersList() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        userController.addUser(user);
        assertEquals(1, userController.getUsers().size());
    }

    @Test
    void userControllerCanAddNewUser() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = userController.addUser(user);
        assertNotNull(user1);
    }

    @Test
    void userControllerCantAddNewUserIfEmailDuplicate() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = User.builder().email("abv@mail.ru").name("vba").login("vba")
                .birthday(LocalDate.of(1988, 10, 2)).build();
        assertThrows(DuplicateException.class, () -> {
            userController.addUser(user);
            userController.addUser(user1);
        });
    }

    @Test
    void userControllerCantAddNewUserIfLoginContainWhitespace() {
        User user = User.builder().email("abv@mail.ru").login("a bv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        assertThrows(ConditionsNotMetException.class, () -> userController.addUser(user));
    }

    @Test
    void userControllerWriteLoginToNameIfNewUserWithoutName() {
        User user = User.builder().email("abv@mail.ru").login("abv")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = userController.addUser(user);
        assertEquals(user1.getName(), user1.getLogin());
    }

    @Test
    void userControllerCantUpdateUserIfUserWithNewInformationWithoutId() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        userController.addUser(user);
        User updatedUser = User.builder().email("abc@mail.ru").login("abc").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        assertThrows(ConditionsNotMetException.class, () -> userController.updateUser(updatedUser));
    }

    @Test
    void userControllerCantUpdateUserIfIdOfNewUserNotFound() {
        User user = User.builder().id(2).email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        assertThrows(NotFoundException.class, () -> userController.updateUser(user));
    }

    @Test
    void userControllerCanUpdateUser() {
        User user = User.builder().email("abv@mail.ru").login("abv").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        userController.addUser(user);
        User updatedUser = User.builder().id(1).email("abc@mail.ru").login("abc").name("Andy")
                .birthday(LocalDate.of(1995, 12, 4)).build();
        User user1 = userController.updateUser(updatedUser);
        assertEquals(updatedUser, user1);
    }
}