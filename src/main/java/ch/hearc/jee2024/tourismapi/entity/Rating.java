package ch.hearc.jee2024.tourismapi.entity;

import ch.hearc.jee2024.tourismapi.utils.RatingId;
import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {
    @EmbeddedId
    private RatingId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("locationId")
    @JoinColumn(name = "location_id")
    private Location location;

    private Integer rating;

    public Rating() {
    }

    public Rating(User user, Location location, Integer rating) {
        this.id = new RatingId(user.getId(), location.getId());
        this.user = user;
        this.location = location;
        setRating(rating);
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}