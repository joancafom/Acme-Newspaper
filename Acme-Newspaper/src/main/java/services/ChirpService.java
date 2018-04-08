
package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ChirpRepository;
import security.LoginService;
import domain.Chirp;
import domain.User;

@Service
@Transactional
public class ChirpService {

	/* Managed Repository */
	@Autowired
	private ChirpRepository	chirpRepository;

	/* Services */
	@Autowired
	private UserService		userService;


	/* Level B Requirements */

	/* v1.0 - josembell */
	public Chirp create() {
		final Chirp chirp = new Chirp();
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

		chirp.setUser(user);
		chirp.setMoment(new Date());

		return chirp;

	}

	/* v1.0 - josembell */
	public Chirp save(final Chirp chirp) {
		Assert.notNull(chirp);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(chirp.getUser().equals(user));

		chirp.setMoment(new Date());
		final Chirp savedChirp = this.chirpRepository.save(chirp);

		user.getChirps().add(savedChirp);

		return savedChirp;
	}

}
