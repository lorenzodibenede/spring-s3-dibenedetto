package ch.hearc.jee2024.tourismapi.DTO;

public class LocationDTO {
    private final Long id;
    private final String name;
    private final Double averageRating;

    public LocationDTO(Long id, String name, Double averageRating) {
        this.id = id;
        this.name = name;
        this.averageRating = averageRating;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAverageRating() {
        return averageRating;
    }
}