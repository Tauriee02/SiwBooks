package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.validation.Valid;

@Controller
public class BookController {
	@Autowired
	private BookService bookService;
	@Autowired
	private AuthorService authorService;

	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("/books")
	public String showAllBook(Model model) {
		model.addAttribute("books", this.bookService.getAllBooks());
		model.addAttribute("isAdmin", false);
		return "books.html";
	}

	@GetMapping("/book/{id}")
	public String getBook(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
		boolean isAdmin= credentialsService.getCredentials(userDetails.getUsername()).getRole().equals("ADMIN_ROLE");
		boolean bool = false;
        if (userDetails != null) {
            User user = credentialsService.getCredentials(userDetails.getUsername()).getUser();
            bool = reviewService.existsByBookIdAndUserId(id, user.getId());
        }
        model.addAttribute("userHasReviewed", bool);
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("book", this.bookService.getBookById(id));    
		return "book.html";
	}

	@GetMapping("/book/year/{year}")
	public String getBookYear(@PathVariable("year") Integer year, Model model) {
		model.addAttribute("books", this.bookService.findAllByYear(year));    
		return "books.html";
	}



	@GetMapping("/formNewBook")
	public String formNewBook(Model model) {
		model.addAttribute("authors", authorService.getAllAuthors());
		model.addAttribute("book", new Book());
		return "formNewBook.html";
	}


	@PostMapping("/book")
	public String newBook(@Valid @ModelAttribute("book") Book book,BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {         
			model.addAttribute("messaggioErroreTitolo", "Campo obbligatorio");
			return "formNewBook.html";
		} else {
			if(book.getId()!=null) {
				Book temp=bookService.getBookById(book.getId());
				List<Author> authors= temp.getAuthors();
				for(Author a: authors) {
					a.addBook(temp);
				}
				book.setImages(temp.getImages());
			}
			this.bookService.saveBook(book);
			model.addAttribute("book", book);
			return "redirect:/book/"+book.getId();
		}
	}


	@PostMapping("book/addAuthor")
	public String addAuthor(@Valid @ModelAttribute("book") Book book,BindingResult bindingResult, @RequestParam(required = false) String nuovoAutore,Model model) {
		if(bindingResult.hasErrors()) {        
			model.addAttribute("messaggioErroreTitolo", "Campo obbligatorio");
			return "formUpdateBook.html";
		} else {
			if (nuovoAutore != null && !nuovoAutore.isBlank()) {
				Author autore = new Author();
				autore.setName(nuovoAutore);
				book.addAuthor(autore);
				authorService.saveAuthor(autore);
			}
			this.bookService.saveBook(book);
			model.addAttribute("book", book);
			return "redirect:/book/"+book.getId();
		}
	}


	@GetMapping("/updateBooks")  //gestisci libri
	public String updateBooks(Model model) {
		model.addAttribute("books", this.bookService.getAllBooks());
		model.addAttribute("isAdmin", true);
		return "books.html";
	}

	@GetMapping("/book/{id}/update")  //modifica libri
	public String updateBook(@PathVariable("id") Long id, Model model) {
		model.addAttribute("book", this.bookService.getBookById(id)); 
		model.addAttribute("authors", this.authorService.getAllAuthors());
		return "formUpdateBook.html";
	}

	@PostMapping("/book/{id}/delete")
	public String deleteBook(@PathVariable Long id) {
		bookService.deleteById(id);
		return "redirect:/updateBooks";
	}
	
	@GetMapping("/book/{id}/images")
    public String manageImages(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "image";
    }
	
	@PostMapping("/book/{id}/addImage")
    public String uploadImages(@PathVariable Long id,
            @RequestParam("imageFiles") List<MultipartFile> imageFiles) {
        bookService.addImagesToBook(id, imageFiles);
        return "redirect:/book/" + id + "/images";
    }

    @PostMapping("/book/{id}/removeImage")
    public String removeImage(@PathVariable Long id,
            @RequestParam("imageId") Long imageId) {
        
        bookService.removeImageFromBook(id, imageId);
        return "redirect:/book/" + id + "/images";
    }
}
