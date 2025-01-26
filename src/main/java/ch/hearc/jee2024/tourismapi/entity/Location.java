package ch.hearc.jee2024.tourismapi.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @OneToMany(mappedBy = "location", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

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

    public void setId(Long id) {
        this.id = id;
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

    @JsonView(SummaryView.class)
    public Long getSubmittedById(){
        Optional<User> userId = Optional.ofNullable(this.submittedBy);
        return userId.map(User::getId).orElse(null);
    }

    public void setSubmittedBy(User submittedBy) {
        this.submittedBy = submittedBy;
    }

    @JsonView(SummaryView.class)
    public Long getValidatedById(){
        Optional<User> userId = Optional.ofNullable(this.validatedBy);
        return userId.map(User::getId).orElse(null);
    }

    public void setValidatedBy(User validatedBy) {
        this.validatedBy = validatedBy;
    }

    @JsonView(SummaryView.class)
    public String getHref() {
        return "api/locations/" + this.id;
    }
}