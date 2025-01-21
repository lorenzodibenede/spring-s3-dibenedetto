package ch.hearc.jee2024.tourismapi.service;

import ch.hearc.jee2024.tourismapi.entity.Rating;
import ch.hearc.jee2024.tourismapi.entity.User;

import java.util.List;

public interface RatingService {
    Rating addOrUpdateRating(Long locationId, Long userId, Integer ratingValue);

    List<Rating> getRatingsByUser(User user);

    List<Rating> getRatingsByLocation(Long locationId);
}
