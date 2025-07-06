package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {
	@Autowired
	private ReviewRepository repository;
	
	public Review saveReview(Review review) {
		return repository.save(review);
	}
	
	public Review getReviewById(Long id) {
		return repository.findById(id).get();
	}

	public Iterable<Review> getAllReviews(){
		return repository.findAll();
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}
	
	public boolean existsByBookIdAndUserId(Long bookId, Long userId) {
        return repository.existsByBookIdAndUserId(bookId, userId);
    }
	
}
