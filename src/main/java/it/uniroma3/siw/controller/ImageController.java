package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.ImageService;

@RestController
@RequestMapping("/image")
public class ImageController {

	@Autowired
	private AuthorService authorService;

	@Autowired
	private BookService bookService;
	
	@Autowired
	private ImageService imageService;

	/**
	 * Restituisce l'immagine associata a un autore/libro
	 * @param id ID del libro/autore
	 * @return ResponseEntity con byte[] dell'immagine e il MIME type
	 */
	@GetMapping("book/{id}")
	public ResponseEntity<byte[]> getRandomImageByBookId(@PathVariable Long id) {
		Book book = bookService.getBookById(id);

		List<Image> images = book.getImages();

		if (images != null && !images.isEmpty()) {
			Image randomImage = images.get((int) (Math.random() * images.size()));

			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(randomImage.getType()))
					.body(randomImage.getData());
		}

		return ResponseEntity.notFound().build();
	}
	@GetMapping("author/{id}")
	public ResponseEntity<byte[]> getImageByAuthorId(@PathVariable Long id) {
		Author author = authorService.getAuthorById(id);

		if (author.getImage() != null) {
			byte[] imageData = author.getImage().getData();
			String contentType = author.getImage().getType();
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.body(imageData);
		}

		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable Long id) {
        Image image = imageService.getImageById(id);

        if (image != null && image.getData() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getType()))
                    .body(image.getData());
        }

        return ResponseEntity.notFound().build();
    }
}
