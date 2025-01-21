package ch.hearc.jee2024.tourismapi.repository;

import ch.hearc.jee2024.tourismapi.entity.Location;
import ch.hearc.jee2024.tourismapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}