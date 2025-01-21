package ch.hearc.jee2024.tourismapi.service;

import ch.hearc.jee2024.tourismapi.entity.Location;
import ch.hearc.jee2024.tourismapi.entity.User;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    Location submitLocation(String name, String description, Double latitude, Double longitude, Long userId);

    Optional<Location> getLocationById(Long id);

    List<Location> getAllLocations(Integer page, Integer limit);

    List<Location> getValidatedLocations(Integer page, Integer limit);

    boolean validateLocation(Long locationId, User admin);

    boolean rejectLocation(Long locationId);
}
