package it.uniroma3.siw.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;

@Controller
public class ReviewController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/book/{id}/review")
    public String showReviewForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);

        Review review = new Review();
        review.setBook(book);

        model.addAttribute("review", review);
        model.addAttribute("book", book);
        return "formNewReview.html";
    }
    
    @GetMapping("/review/{id}/update")
    public String showReviewUpdateForm(@PathVariable("id") Long id, Model model) {

        Review review = reviewService.getReviewById(id);
       
        model.addAttribute("review", review);
        return "formUpdateReview.html";
    }



    @PostMapping("/book/{bookId}/newReview")
    public String saveReview(@PathVariable("bookId") Long bookId, @ModelAttribute Review review) {
        Book book = bookService.getBookById(bookId);
        review.setBook(book);

        // Ottieni lo username dell'utente loggato
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); 

        Credentials credentials = credentialsService.getCredentials(username); 

        review.setUser(credentials.getUser());

        reviewService.saveReview(review);
        return "redirect:/book/" + bookId;
    }
    
    
    @PostMapping("/review/{id}/delete")
    public String deleteReview(@PathVariable("id") Long reviewId, Model model) {
        Review review = reviewService.getReviewById(reviewId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Credentials credentials = credentialsService.getCredentials(username);
        boolean isAdmin = credentials.getRole().equals(Credentials.ADMIN_ROLE);

        if (isAdmin || credentials.getUser().equals(review.getUser())) {
            Long bookId = review.getBook().getId(); 
            reviewService.deleteById(reviewId);
            return "redirect:/book/" + bookId;
        }

        // se non autorizzato, torni alla homepage o pagina errore
        return "redirect:/accessDenied";
    }

    
}