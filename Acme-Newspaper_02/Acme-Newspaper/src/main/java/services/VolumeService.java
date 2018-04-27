
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import repositories.VolumeRepository;
import domain.Volume;

@Service
@Transactional
public class VolumeService {

	/* Managed Repository */
	@Autowired
	private VolumeRepository	volumeRepository;


	//v1.0 - Implemented by JA
	public Volume findOne(final int volumeId) {
		return this.volumeRepository.findOne(volumeId);
	}

	//CRUD Methods

	// v1.0 - Implemented by JA
	public Collection<Volume> findAll() {
		return this.volumeRepository.findAll();
	}

	// v1.0 - Implemented by JA
	public Page<Volume> findAll(final int page, final int size) {
		return this.volumeRepository.findAllPaged(new PageRequest(page - 1, size));
	}

	//Other Business Methods

}
