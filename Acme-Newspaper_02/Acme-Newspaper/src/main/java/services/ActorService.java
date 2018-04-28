
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.UserAccount;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	// Managed repository ------------------

	@Autowired
	private ActorRepository	actorRepository;


	// Constructors ------------------------

	public ActorService() {
		super();
	}

	// Simple CRUD methods -----------------

	public Collection<Actor> findAll() {
		Collection<Actor> actors;

		Assert.notNull(this.actorRepository);
		actors = this.actorRepository.findAll();
		Assert.notNull(actors);

		return actors;
	}

	public Actor findOne(final int actorId) {
		// REVISAR !!!
		// Debe tener algún assert?
		Actor actor;

		actor = this.actorRepository.findOne(actorId);

		return actor;
	}

	// Other business methods --------------

	public Actor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		final Actor actor = this.actorRepository.findByUserAccountId(userAccount.getId());

		return actor;
	}

}
