package ch.hearc.jee2024.tourismapi.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RatingId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "location_id")
    private Long locationId;

    public RatingId() {
    }

    public RatingId(Long userId, Long locationId) {
        this.userId = userId;
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId ratingId = (RatingId) o;
        return Objects.equals(userId, ratingId.userId) && Objects.equals(locationId, ratingId.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, locationId);
    }
}