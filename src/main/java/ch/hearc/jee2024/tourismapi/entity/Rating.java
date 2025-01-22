package ch.hearc.jee2024.tourismapi.entity;

import ch.hearc.jee2024.tourismapi.utils.RatingId;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {

    public interface WithoutUserView {}
    public interface WithUserIdView extends WithoutUserView {}

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

    @JsonView(WithoutUserView.class)
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

    @JsonView(WithUserIdView.class)
    public Long getUserId() {
        return user.getId();
    }

    public void setUser(User user) {
        this.user = user;
    }
}