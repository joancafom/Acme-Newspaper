
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ChirpRepository;
import security.LoginService;
import domain.Administrator;
import domain.Chirp;
import domain.User;

@Service
@Transactional
public class ChirpService {

	/* Managed Repository */
	@Autowired
	private ChirpRepository				chirpRepository;

	/* Services */
	@Autowired
	private UserService					userService;

	@Autowired
	private AdministratorService		adminService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	/* Level B Requirements */

	//CRUD Methods ----------------

	/* v1.0 - josembell */
	public Chirp create() {
		final Chirp chirp = new Chirp();
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

		chirp.setUser(user);
		chirp.setMoment(new Date());

		return chirp;

	}

	//v1.0 - Implemented by JA
	public Chirp findOne(final int chirpId) {

		return this.chirpRepository.findOne(chirpId);
	}

	/* v1.0 - josembell */
	// v2.0 - Modified by JA (taboo)
	public Chirp save(final Chirp chirp) {
		Assert.notNull(chirp);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(chirp.getUser().equals(user));

		final Date moment = new Date(System.currentTimeMillis() - 1000L);
		chirp.setMoment(moment);

		//Taboo check
		//Check for taboo words
		final Boolean containsTabooVeredict = this.systemConfigurationService.containsTaboo(chirp.getTitle() + " " + chirp.getDescription());
		chirp.setContainsTaboo(containsTabooVeredict);

		final Chirp savedChirp = this.chirpRepository.save(chirp);

		user.getChirps().add(savedChirp);

		return savedChirp;
	}
	
	// v1.0 - Implemented by Alicia
	public Chirp saveTaboo(final Chirp chirp) {
		Assert.notNull(chirp);

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		//Taboo check
		//Check for taboo words
		final Boolean containsTabooVeredict = this.systemConfigurationService.containsTaboo(chirp.getTitle() + " " + chirp.getDescription());
		chirp.setContainsTaboo(containsTabooVeredict);

		return this.chirpRepository.save(chirp);
	}

	//v1.0 - Implemented by JA
	// v2.0 - Updated by Alicia
	public void delete(final Chirp chirpToDelete) {

		Assert.notNull(chirpToDelete);
		Assert.isTrue(this.chirpRepository.exists(chirpToDelete.getId()));

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		this.chirpRepository.delete(chirpToDelete);
	}

	//Other Business Methods ---------

	// v1.0 - Implemented by Alicia
	public void flush() {
		this.chirpRepository.flush();
	}

	/* v1.0 - josembell */
	public Collection<Chirp> getStream() {
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		return this.chirpRepository.getStream(user.getId());
	}

	/* v1.0 - josembell */
	public Collection<Chirp> findTabooedChirps() {
		return this.chirpRepository.findTabooedChirps();
	}
	
	// v1.0 - Implemented by Alicia
	public Collection<Chirp> findNotTabooedChirps() {
		return this.chirpRepository.findNotTabooedChirps();
	}

}
