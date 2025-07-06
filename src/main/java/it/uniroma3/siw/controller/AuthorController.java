package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import jakarta.validation.Valid;

@Controller
public class AuthorController {
	@Autowired
	private AuthorService authorService;
	@Autowired
	private BookService bookService;
	
	@GetMapping("/authors")
	public String showAllAuthor(Model model) {
		model.addAttribute("authors", this.authorService.getAllAuthors());
		model.addAttribute("isAdmin", false);
        return "authors.html";
	}
	
	  @GetMapping("/author/{id}")
	  public String getAuthor(@PathVariable("id") Long id, Model model) {
	        model.addAttribute("author", this.authorService.getAuthorById(id));    
	        return "author.html";
	    }


	
	@GetMapping("/formNewAuthor")
	public String formNewAuthor(Model model) {
		model.addAttribute("author", new Author());
		return "formNewAuthor.html";
	}

	
	@PostMapping("/author")
    public String newAuthor(@Valid @ModelAttribute("author") Author author,BindingResult bindingResult,
                                   @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        if (bindingResult.hasErrors()) {
            return "formNewAuthor.html";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            Image img = new Image();
            img.setName(imageFile.getOriginalFilename());
            img.setType(imageFile.getContentType());
            img.setData(imageFile.getBytes());
            author.setImage(img);
        }

        authorService.saveAuthor(author);
        return "redirect:/author/"+author.getId();
    }
	
	
	@GetMapping("/updateAuthors")  //gestisci autori
	public String updateAuthors(Model model) {
		model.addAttribute("authors", this.authorService.getAllAuthors());
		model.addAttribute("isAdmin", true);
        return "authors.html";
	}
	
	@GetMapping("/author/{id}/update")  //modifica autori
	public String updateAuthor(@PathVariable("id") Long id, Model model) {
        model.addAttribute("author", this.authorService.getAuthorById(id)); 
        model.addAttribute("books", this.bookService.getAllBooks());
        return "formUpdateAuthor.html";
    }
	
	@PostMapping("/author/{id}/delete")
	public String deleteAuthor(@PathVariable Long id) {
	    authorService.deleteById(id);
	    return "redirect:/updateAuthors";
	}
	
	@PostMapping("/author/{id}/update")
	public String updateAuthor(@PathVariable("id") Long id,
	                           @Valid @ModelAttribute("author") Author author,
	                           BindingResult bindingResult,
	                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

	    if (bindingResult.hasErrors()) {
	        return "formUpdateAuthor.html";
	    }
	    author.setId(id);

	    if (imageFile != null && !imageFile.isEmpty()) {
	        Image img = new Image();
	        img.setName(imageFile.getOriginalFilename());
	        img.setType(imageFile.getContentType());
	        img.setData(imageFile.getBytes());
	        author.setImage(img);
	    } else {
	        Author existingAuthor = authorService.getAuthorById(id);
	        author.setImage(existingAuthor.getImage());
	    }
	    authorService.saveAuthor(author);

	    return "redirect:/author/" + id;
	}

	

}
