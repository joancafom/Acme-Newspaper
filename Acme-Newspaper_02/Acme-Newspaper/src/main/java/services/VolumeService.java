
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.VolumeRepository;
import security.LoginService;
import domain.Newspaper;
import domain.User;
import domain.Volume;

@Service
@Transactional
public class VolumeService {

	/* Managed Repository */
	@Autowired
	private VolumeRepository	volumeRepository;

	@Autowired
	private UserService			userService;

	@Autowired
	private Validator			validator;


	//CRUD Methods

	/* v1.0 - josembell */
	public Volume create() {
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		final Volume volume = new Volume();
		volume.setNewspapers(new HashSet<Newspaper>());

		return volume;
	}

	// v1.0 - Implemented by JA
	public Collection<Volume> findAll() {
		return this.volumeRepository.findAll();
	}

	// v1.0 - Implemented by JA
	public Page<Volume> findAll(final int page, final int size) {
		return this.volumeRepository.findAllPaged(new PageRequest(page - 1, size));
	}

	//v1.0 - Implemented by JA
	public Volume findOne(final int volumeId) {
		return this.volumeRepository.findOne(volumeId);
	}

	/* v1.0 - josembell */
	public Volume save(final Volume volume) {
		Assert.notNull(volume);
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		final LocalDate now = LocalDate.now();
		Assert.isTrue(volume.getYear() <= now.getYear());

		final Volume saved = this.volumeRepository.save(volume);
		user.getVolumes().add(saved);

		return saved;
	}

	// v1.0 - Implemented by Alicia
	public void flush() {
		this.volumeRepository.flush();
	}

	/* v1.0 - josembell */
	public void addNewspaper(final Volume volume, final Newspaper newspaper) {
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		Assert.notNull(volume);
		Assert.notNull(newspaper);
		Assert.isTrue(user.getNewspapers().contains(newspaper));
		Assert.isTrue(user.getVolumes().contains(volume));
		Assert.isTrue(!volume.getNewspapers().contains(newspaper));
		Assert.isTrue(!newspaper.getVolumes().contains(volume));

		newspaper.getVolumes().add(volume);
		volume.getNewspapers().add(newspaper);
	}

	/* v1.0 - josembell */
	// v2.0 - Updated by Alicia
	public void removeNewspaper(final Volume volume, final Newspaper newspaper) {
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		Assert.notNull(volume);
		Assert.notNull(newspaper);
		Assert.isTrue(user.getNewspapers().contains(newspaper));
		Assert.isTrue(user.getVolumes().contains(volume));
		Assert.isTrue(volume.getNewspapers().contains(newspaper));
		Assert.isTrue(newspaper.getVolumes().contains(volume));

		newspaper.getVolumes().remove(volume);
		volume.getNewspapers().remove(newspaper);

	}

	public Volume reconstruct(final Volume prunedVolume, final BindingResult binding) {
		Assert.notNull(prunedVolume);
		//final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());

		prunedVolume.setNewspapers(new HashSet<Newspaper>());

		this.validator.validate(prunedVolume, binding);
		return prunedVolume;
	}

	//Other Business Methods

}
