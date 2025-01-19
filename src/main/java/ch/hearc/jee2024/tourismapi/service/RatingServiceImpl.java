package ch.hearc.jee2024.tourismapi.service;

import ch.hearc.jee2024.tourismapi.entity.Location;
import ch.hearc.jee2024.tourismapi.entity.Rating;
import ch.hearc.jee2024.tourismapi.entity.User;
import ch.hearc.jee2024.tourismapi.repository.LocationRepository;
import ch.hearc.jee2024.tourismapi.repository.RatingRepository;
import ch.hearc.jee2024.tourismapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public RatingServiceImpl(RatingRepository ratingRepository, LocationRepository locationRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    public Rating addOrUpdateRating(Long locationId, Long userId, Integer ratingValue) {
        if (ratingValue < 0 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NoSuchElementException("Location not found"));

        Rating rating = new Rating(user, location, ratingValue);
        ratingRepository.save(rating);

        List<Rating> allRatings = ratingRepository.findByLocation(location);
        double average = allRatings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
        location.setAverageRating(average);
        locationRepository.save(location);

        return rating;
    }

    public List<Rating> getRatingsByLocation(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NoSuchElementException("Location not found"));
        return ratingRepository.findByLocation(location);
    }

    public List<Rating> getRatingsByUser(User user) {
        return ratingRepository.findByUser(user);
    }
}