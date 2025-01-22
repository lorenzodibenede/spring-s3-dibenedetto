package ch.hearc.jee2024.tourismapi.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Location {

    public interface SummaryView {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private Double averageRating = 0.0;

    @ManyToOne
    @JoinColumn(name = "submitted_by")
    private User submittedBy;

    @ManyToOne
    @JoinColumn(name = "validated_by")
    private User validatedBy;

    public Location() {
    }

    public Location(String name, String description, Double latitude, Double longitude, User submittedBy) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.submittedBy = submittedBy;
    }

    @JsonView(SummaryView.class)
    public Long getId() {
        return id;
    }

    @JsonView(SummaryView.class)
    public String getName() {
        return name;
    }

    @JsonView(SummaryView.class)
    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {

        this.averageRating = averageRating;
    }

    public String getDescription() {
        return description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public User getSubmittedBy() {
        return submittedBy;
    }

    public User getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(User validatedBy) {
        this.validatedBy = validatedBy;
    }
}