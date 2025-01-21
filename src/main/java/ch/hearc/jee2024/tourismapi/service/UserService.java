package ch.hearc.jee2024.tourismapi.service;

import ch.hearc.jee2024.tourismapi.utils.Role;
import ch.hearc.jee2024.tourismapi.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(String name, String password, Role role);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    List<User> getAllAdmins();
}
