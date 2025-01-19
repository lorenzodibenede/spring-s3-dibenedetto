package ch.hearc.jee2024.tourismapi.service;

import ch.hearc.jee2024.tourismapi.utils.Role;
import ch.hearc.jee2024.tourismapi.entity.User;

import java.util.Optional;

public interface UserService {
    public User createUser(String name, String password, Role role);

    public Optional<User> getUserById(Long id);
}
