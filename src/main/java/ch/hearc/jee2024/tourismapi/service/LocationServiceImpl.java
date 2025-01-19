package ch.hearc.jee2024.tourismapi.service;

import ch.hearc.jee2024.tourismapi.DTO.LocationDTO;
import ch.hearc.jee2024.tourismapi.entity.Location;
import ch.hearc.jee2024.tourismapi.entity.User;
import ch.hearc.jee2024.tourismapi.repository.LocationRepository;
import ch.hearc.jee2024.tourismapi.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public LocationServiceImpl(LocationRepository locationRepository, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    public Location submitLocation(String name, String description, Double latitude, Double longitude, Long userId) {
        User submittedBy = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

        Location location = new Location(name, description, latitude, longitude, submittedBy);
        return locationRepository.save(location);
    }

    public Optional<Location> getLocationById(Long locationId) {
        return locationRepository.findById(locationId);
    }

    public List<Location> getLocationsValidatedBy(User admin) {
        return locationRepository.findByValidatedBy(admin);
    }

    public List<Location> getLocationsSubmittedBy(User user) {
        return locationRepository.findBySubmittedBy(user);
    }

    public List<LocationDTO> getLocations(Integer page, Integer limit) {
        List<Location> locations = locationRepository.findAll();

        Stream<Location> locationStream = locations.stream();
        if (page != null && limit != null) {
            locationStream = locationStream.skip((long) (page - 1) * limit).limit(limit);
        }

        return locationStream
                .map(location -> new LocationDTO(
                        location.getId(),
                        location.getName(),
                        location.getAverageRating()))
                .toList();
    }
}