package ch.hearc.jee2024.tourismapi.service;

import ch.hearc.jee2024.tourismapi.entity.Location;
import ch.hearc.jee2024.tourismapi.entity.User;
import ch.hearc.jee2024.tourismapi.repository.LocationRepository;
import ch.hearc.jee2024.tourismapi.repository.UserRepository;
import ch.hearc.jee2024.tourismapi.utils.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public LocationServiceImpl(LocationRepository locationRepository, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    public Location submitLocation(String name, String description, Double latitude, Double longitude, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

        Location location = new Location(name, description, latitude, longitude, user);
        if (user.getRole() == Role.ADMIN) {
            location.setValidatedBy(user);
        }

        return locationRepository.save(location);
    }

    public Optional<Location> getLocationById(Long locationId) {
        return locationRepository.findById(locationId);
    }

    public List<Location> getAllLocations(Integer page, Integer limit) {
        return paginateLocations(locationRepository.findAll(), page, limit);
    }

    public List<Location> getValidatedLocations(Integer page, Integer limit) {
        return paginateLocations(locationRepository.findAll()
                .stream()
                .filter(location -> location.getValidatedById() != null)
                .toList(), page, limit);
    }

    private List<Location> paginateLocations(List<Location> locations, Integer page, Integer limit) {
        if (page != null && limit != null) {
            return locations.stream()
                    .skip((long) (page - 1) * limit)
                    .limit(limit)
                    .toList();
        }
        return locations;
    }

    public boolean validateLocation(Long locationId, User admin) {
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return false;
        }

        Location location = locationOptional.get();
        location.setValidatedBy(admin);
        locationRepository.save(location);
        return true;
    }

    public boolean rejectLocation(Long locationId) {
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return false;
        }

        locationRepository.delete(locationOptional.get());
        return true;
    }
}