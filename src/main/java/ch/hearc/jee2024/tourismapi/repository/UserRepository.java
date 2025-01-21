package ch.hearc.jee2024.tourismapi.repository;

import ch.hearc.jee2024.tourismapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}