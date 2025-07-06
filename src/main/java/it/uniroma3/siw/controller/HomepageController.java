package it.uniroma3.siw.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;

@Controller
public class HomepageController {

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private BookService bookService;


	@GetMapping("/homepage")
	public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		List<Book> books = StreamSupport.stream(bookService.getAllBooks().spliterator(), false)
				.collect(Collectors.toList());
		User user=null;
		if(userDetails!=null) {
			user=credentialsService.getCredentials(userDetails.getUsername()).getUser();
		}
		model.addAttribute("user", user);
		model.addAttribute("books", books);
		return "homepage";
	}


	@GetMapping("/profile")
	public String homeProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		User user=null;
		if(userDetails!=null) {
			user=credentialsService.getCredentials(userDetails.getUsername()).getUser();
		}
		model.addAttribute("user", user);
		return "profile";
	}

	@GetMapping("/bookIndex")
	public String homeBook(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			boolean isAdmin = Credentials.ADMIN_ROLE.equals(credentials.getRole());
			model.addAttribute("isAdmin", isAdmin);
		} else {
			
			model.addAttribute("isAdmin", false);
		}
		return "bookIndex";
	}

	@GetMapping("/authorIndex")
	public String homeAuthor(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			model.addAttribute("isAdmin", Credentials.ADMIN_ROLE.equals(credentials.getRole()));
		} else {
			model.addAttribute("isAdmin", false);
		}
		return "authorIndex";
	}

}
