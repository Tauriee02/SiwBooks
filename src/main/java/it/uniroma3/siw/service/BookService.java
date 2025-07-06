package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.BookRepository;
import jakarta.transaction.Transactional;

@Service
public class BookService {
	@Autowired
	private BookRepository repository;
	
	@Autowired
	private ImageService imageService;

	public Book getBookById(Long id) {
		return repository.findById(id).get();
	}

	public Iterable<Book> getAllBooks(){
		return repository.findAll();
	}

	public Book saveBook(Book book) {
		return repository.save(book);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}
	
	public Iterable<Author> getAllAuthors(Long id){
		Book book= getBookById(id);
		return book.getAuthors();
	}
	
	public List<Book> findAllByYear(Integer year) {
		return repository.findAllByYear(year);
	}

	//public void addAuthorToBook(Long bookId, Long authorId) {
		//repository.addAuthorToBook(bookId, authorId);
	//}
	
	@Transactional
    public void addImagesToBook(Long bookId, List<MultipartFile> files) {
        Book book = getBookById(bookId);
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    Image img = new Image();
                    img.setName(file.getOriginalFilename());
                    img.setType(file.getContentType());
                    img.setData(file.getBytes());
                    book.getImages().add(img);
                } catch (IOException e) {
                    System.out.println("errore");
                }
            }
        }
        repository.save(book);
    }

    @Transactional
    public void removeImageFromBook(Long bookId, Long imageId) {
        Book book = getBookById(bookId);
        Image imageToRemove = null;

        for (Image img : book.getImages()) {
            if (img.getId().equals(imageId)) {
                imageToRemove = img;
                break;
            }
        }

        if (imageToRemove != null) {
            book.getImages().remove(imageToRemove);
            imageService.deleteById(imageId);
        }

        repository.save(book);
    }

}
