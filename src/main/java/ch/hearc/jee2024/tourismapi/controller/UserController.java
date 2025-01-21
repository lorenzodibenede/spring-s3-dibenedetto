package ch.hearc.jee2024.tourismapi.controller;

import ch.hearc.jee2024.tourismapi.entity.User;
import ch.hearc.jee2024.tourismapi.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user.getName(), user.getPassword(), user.getRole());
            return ResponseEntity.ok(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @JsonView(User.WithoutPasswordView.class)
    public ResponseEntity<User> getUser(
            @PathVariable Long id) {
        return userService.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @JsonView(User.WithoutPasswordView.class)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admins")
    @JsonView(User.WithoutRoleView.class)
    public ResponseEntity<List<User>> getAllAdmins() {
        List<User> admins = userService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }
}
