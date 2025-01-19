package ch.hearc.jee2024.tourismapi.DTO;

public class RatingRequestDTO {
    private Long userId;
    private Integer ratingValue;

    public RatingRequestDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }
}
