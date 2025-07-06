package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Book {
	 @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;


	@NotBlank
     private String title;

     @NotNull
     @Min(1000)
     @Max(2025)
     private Integer year;
     
     @ManyToMany
     private List<Author> authors;
     
     @OneToMany(mappedBy = "book" , cascade =CascadeType.ALL )
     private List<Review> reviews;
     
     @OneToMany(cascade = CascadeType.ALL)
     @JoinColumn(name = "book_id")
     private List<Image> images;
     
     
     

     public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Author> getAuthors() {
		return authors;
	}
	
	public void addAuthor(Author author) {
		this.authors.add(author);
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	
	public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

	@Override
	public int hashCode() {
		return Objects.hash(authors, id, title, images, year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(authors, other.authors) && Objects.equals(id, other.id)
				&& Objects.equals(title, other.title)&& Objects.equals(year, other.year);
	}
	

}
