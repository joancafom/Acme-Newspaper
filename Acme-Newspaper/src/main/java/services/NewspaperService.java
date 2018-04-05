
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.NewspaperRepository;
import domain.Newspaper;

@Service
@Transactional
public class NewspaperService {

	/* Managed Repository */
	@Autowired
	private NewspaperRepository	newspaperRepository;


	/* v1.0 - josembell */
	public Collection<Newspaper> findAll() {
		return this.newspaperRepository.findAll();
	}

	/* v1.0 - josembell */
	public Collection<Newspaper> findAllPublished() {
		return this.newspaperRepository.findAllPublished();
	}

	/* v1.0 - josembell */
	public Newspaper findOne(final int newspaperId) {
		return this.newspaperRepository.findOne(newspaperId);
	}
}
