package com.explosion204.wclookup.service;

import com.explosion204.wclookup.exception.EntityAlreadyExistsException;
import com.explosion204.wclookup.exception.EntityNotFoundException;
import com.explosion204.wclookup.model.entity.Review;
import com.explosion204.wclookup.model.entity.Toilet;
import com.explosion204.wclookup.model.entity.User;
import com.explosion204.wclookup.model.repository.ReviewRepository;
import com.explosion204.wclookup.model.repository.ToiletRepository;
import com.explosion204.wclookup.model.repository.UserRepository;
import com.explosion204.wclookup.service.dto.identifiable.ReviewDto;
import com.explosion204.wclookup.service.pagination.PageContext;
import com.explosion204.wclookup.service.pagination.PaginationModel;
import com.explosion204.wclookup.service.validation.annotation.ValidateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ToiletRepository toiletRepository;
    private final UserRepository userRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            ToiletRepository toiletRepository,
            UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.toiletRepository = toiletRepository;
        this.userRepository = userRepository;
    }

    public PaginationModel<ReviewDto> findAll(PageContext pageContext) {
        PageRequest pageRequest = pageContext.toPageRequest();
        Page<ReviewDto> page = reviewRepository.findAll(pageRequest)
                .map(ReviewDto::fromReview);
        return PaginationModel.fromPage(page);
    }

    public ReviewDto findById(long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Review.class));
        return ReviewDto.fromReview(review);
    }

    @ValidateDto
    public ReviewDto create(ReviewDto reviewDto) {
        Review review = reviewDto.toReview();

        Toilet toilet = toiletRepository.findById(reviewDto.getToiletId())
                .orElseThrow(() -> new EntityNotFoundException(Toilet.class));
        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(User.class));

        if (reviewRepository.findByUserAndToilet(user, toilet).isPresent()) {
            throw new EntityAlreadyExistsException(Review.class);
        }

        review.setToilet(toilet);
        review.setUser(user);
        Review savedReview = reviewRepository.save(review);

        return ReviewDto.fromReview(savedReview);
    }

    @ValidateDto
    public ReviewDto update(ReviewDto reviewDto) {
        Review review = reviewRepository.findById(reviewDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(Review.class));

        if (reviewDto.getText() != null) {
            review.setText(reviewDto.getText());
        }

        if (reviewDto.getRating() != null) {
            review.setRating(reviewDto.getRating());
        }

        Review updatedReview = reviewRepository.save(review);
        return ReviewDto.fromReview(updatedReview);
    }

    public void delete(long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Review.class));
        reviewRepository.delete(review);
    }
}
