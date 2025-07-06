package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Image;

import it.uniroma3.siw.repository.ImageRepository;

@Service
public class ImageService {
	@Autowired
	private ImageRepository repository;
	
	public Image getImageById(Long id) {
		return repository.findById(id).get();
	}

	public Iterable<Image> getAllImages(){
		return repository.findAll();
	}

	public Image saveImage(Image image) {
		return repository.save(image);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
