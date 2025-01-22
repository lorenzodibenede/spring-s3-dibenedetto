package ch.hearc.jee2024.tourismapi.controller;

import ch.hearc.jee2024.tourismapi.DTO.RatingRequestDTO;
import ch.hearc.jee2024.tourismapi.entity.Location;
import ch.hearc.jee2024.tourismapi.entity.Rating;
import ch.hearc.jee2024.tourismapi.entity.User;
import ch.hearc.jee2024.tourismapi.service.LocationService;
import ch.hearc.jee2024.tourismapi.service.RatingService;
import ch.hearc.jee2024.tourismapi.service.UserService;
import ch.hearc.jee2024.tourismapi.utils.Role;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
class LocationController {
    private final LocationService locationService;
    private final RatingService ratingService;
    private final UserService userService;

    public LocationController(LocationService locationService, RatingService ratingService, UserService userService) {
        this.locationService = locationService;
        this.ratingService = ratingService;
        this.userService = userService;
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
    @JsonView(Location.SummaryView.class)
    public ResponseEntity<List<Location>> getLocations(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Long userId) {
        List<Location> locations;

        if (userId != null) {
            Optional<User> userOptional = userService.getUserById(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            User user = userOptional.get();
            if (user.getRole() == Role.ADMIN) {
                locations = locationService.getAllLocations(page, limit);
            } else if (user.getRole() == Role.USER) {
                locations = locationService.getValidatedLocations(page, limit);
            } else {
                return ResponseEntity.status(403).build();
            }
        } else {
            locations = locationService.getValidatedLocations(page, limit);
        }

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
    @JsonView(Rating.WithoutUserView.class)
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

    @GetMapping("/{locationId}/ratings")
    @JsonView(Rating.WithUserIdView.class)
    public ResponseEntity<List<Rating>> getRatingsByLocation(
            @PathVariable Long locationId) {
        try {
            List<Rating> ratings = ratingService.getRatingsByLocation(locationId);
            return ResponseEntity.ok(ratings);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{locationId}/validate")
    public ResponseEntity<Void> validateLocation(
            @PathVariable Long locationId,
            @RequestParam Long adminId) {
        Optional<User> adminOptional = userService.getUserById(adminId);
        if (adminOptional.isEmpty() || adminOptional.get().getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        User admin = adminOptional.get();
        boolean success = locationService.validateLocation(locationId, admin);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{locationId}/reject")
    public ResponseEntity<Void> rejectLocation(
            @PathVariable Long locationId,
            @RequestParam Long adminId) {
        Optional<User> adminOptional = userService.getUserById(adminId);
        if (adminOptional.isEmpty() || adminOptional.get().getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        boolean success = locationService.rejectLocation(locationId);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
