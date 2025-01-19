package ch.hearc.jee2024.tourismapi.service;

import ch.hearc.jee2024.tourismapi.DTO.LocationDTO;
import ch.hearc.jee2024.tourismapi.entity.Location;
import ch.hearc.jee2024.tourismapi.entity.User;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    public Location submitLocation(String name, String description, Double latitude, Double longitude, Long userId);

    public List<Location> getLocationsValidatedBy(User admin);

    public List<Location> getLocationsSubmittedBy(User user);

    public List<LocationDTO> getLocations(Integer page, Integer limit);

    public Optional<Location> getLocationById(Long id);
}
