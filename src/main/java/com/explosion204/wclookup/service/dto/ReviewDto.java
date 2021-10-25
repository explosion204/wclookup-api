package com.explosion204.wclookup.service.dto;

import com.explosion204.wclookup.model.entity.Review;
import com.explosion204.wclookup.service.validation.constraint.RestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@RestDto
public class ReviewDto extends IdentifiableDto {
    private long userId;
    private long toiletId;

    @DecimalMin("0.0")
    @DecimalMax("180.0")
    private Double rating;

    @Size(min = 1, max = 140)
    private String text;

    public Review toReview() {
        Review review = new Review();

        review.setId(id);
        review.setText(text);
        review.setRating(rating);

        return review;
    }

    public static ReviewDto fromReview(Review review) {
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.id = review.getId();
        reviewDto.userId = review.getUser().getId();
        reviewDto.toiletId = review.getToilet().getId();
        reviewDto.rating = review.getRating();
        reviewDto.text = review.getText();

        return reviewDto;
    }
}
