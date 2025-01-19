package ch.hearc.jee2024.tourismapi.service;

import ch.hearc.jee2024.tourismapi.utils.Role;
import ch.hearc.jee2024.tourismapi.entity.User;
import ch.hearc.jee2024.tourismapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String password, Role role) {
        if (role == null) {
            role = Role.USER;
        }
        User user = new User(name, password, role);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}