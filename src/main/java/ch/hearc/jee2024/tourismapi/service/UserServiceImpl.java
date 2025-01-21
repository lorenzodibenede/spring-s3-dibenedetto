package ch.hearc.jee2024.tourismapi.service;

import ch.hearc.jee2024.tourismapi.utils.Role;
import ch.hearc.jee2024.tourismapi.entity.User;
import ch.hearc.jee2024.tourismapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String password, Role role) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("The name is required and cannot be null or empty.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("The password is required and cannot be null or empty.");
        }

        if (role == null) {
            role = Role.USER;
        }

        User user = new User(name, password, role);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllAdmins() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.ADMIN)
                .toList();
    }
}