package ch.hearc.jee2024.tourismapi.controller;

import ch.hearc.jee2024.tourismapi.DTO.LocationDTO;
import ch.hearc.jee2024.tourismapi.DTO.RatingRequestDTO;
import ch.hearc.jee2024.tourismapi.entity.Location;
import ch.hearc.jee2024.tourismapi.entity.Rating;
import ch.hearc.jee2024.tourismapi.service.LocationService;
import ch.hearc.jee2024.tourismapi.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/locations")
class LocationController {
    private final LocationService locationService;
    private final RatingService ratingService;

    public LocationController(LocationService locationService, RatingService ratingService) {
        this.locationService = locationService;
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<Location> submitLocation(
            @RequestParam Long userId,
            @RequestBody Location location) {
        Location submitted = locationService.submitLocation(
                location.getName(),
                location.getDescription(),
                location.getLatitude(),
                location.getLongitude(),
                userId
        );
        return ResponseEntity.ok(submitted);
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getLocations(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit) {
        List<LocationDTO> locations = locationService.getLocations(page, limit);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(
            @PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{locationId}/rate")
    public ResponseEntity<Rating> rateLocation(
            @PathVariable Long locationId,
            @RequestBody RatingRequestDTO ratingRequest) {
        try {
            Rating rating = ratingService.addOrUpdateRating(
                    locationId,
                    ratingRequest.getUserId(),
                    ratingRequest.getRatingValue()
            );
            return ResponseEntity.ok(rating);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{location_id}/ratings")
    public ResponseEntity<List<Rating>> getRatingsByLocation(
            @PathVariable Long location_id) {
        try {
            List<Rating> ratings = ratingService.getRatingsByLocation(location_id);
            return ResponseEntity.ok(ratings);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
