package ch.hearc.jee2024.tourismapi.entity;

import ch.hearc.jee2024.tourismapi.utils.Role;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    public interface WithoutRoleView {}
    public interface WithoutPasswordView extends WithoutRoleView {}
    public interface Summary extends WithoutPasswordView {}
    public interface AdminSummary extends WithoutRoleView {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
    }

    public User(String name, String password, Role role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    @JsonView(WithoutRoleView.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(WithoutRoleView.class)
    public String getName() {
        return name;
    }

    @JsonView(WithoutPasswordView.class)
    public Role getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    @JsonView({Summary.class, AdminSummary.class})
    public String getHref() {
        return "api/users/" + id;
    }
}

