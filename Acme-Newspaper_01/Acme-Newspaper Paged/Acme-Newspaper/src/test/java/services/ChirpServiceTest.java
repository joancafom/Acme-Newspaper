
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import utilities.AbstractTest;
import domain.Chirp;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChirpServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private ChirpService	chirpService;

	//Fixtures ---------------------------------------

	@Autowired
	private UserService		userService;

	@PersistenceContext
	private EntityManager	entityManager;


	// -------------------------------------------------------------------------------
	// [UC-009]Create a Chirp.
	// 
	// Related Requirements:
	//   � REQ 15: A user may post a chirp. For every chirp, the system must store the
	//             moment, a title, and a description. The list of chirps are
	//             considered a part of the profile of a user.
	//   � REQ 16.1: An actor who is authenticated as a user must be able to post a
	//               chirp. Chirps may not be changed or deleted once they are posted.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverCreateChirp() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> moment of the new Chirp.
		// testingData[i][2] -> title of the new Chirp.
		// testingData[i][3] -> description of the new Chirp.
		// testingData[i][4] -> containsTaboo of the new Chirp.
		// testingData[i][5] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A User correctly creates a new Chirp.
				"user1", null, "niceTitle", "niceDescription", null, null
			}, {
				// 2 - (-) A User creates a Chirp with a null description.
				"user1", null, "niceTitle", null, null, ConstraintViolationException.class
			}, {
				// 3 - (-) An unauthenticated actor creates a correct Chirp.
				null, null, "niceTitle", "niceDescription", null, IllegalArgumentException.class
			}, {
				// 4 - (+) A User tries to create (unsuccessfully) a Chirp with a future moment.
				"user1", "futureDate", "niceTitle", "niceDescription", null, null
			}, {
				// 5 - (+) A User tries to create (unsuccessfully) a Chirp indicating the containsTaboo property.
				"user1", null, "niceTitle", "niceDescription", true, null
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Date moment = null;

			if (testingData[i][1] != null)
				moment = new LocalDate().plusYears(2).toDate();

			this.startTransaction();

			this.templateCreateChirp((String) testingData[i][0], moment, (String) testingData[i][2], (String) testingData[i][3], (Boolean) testingData[i][4], (Class<?>) testingData[i][5]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateCreateChirp(final String username, final Date moment, final String title, final String description, final Boolean containsTaboo, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. Create a Chirp

			final Chirp createdChirp = this.chirpService.create();

			if (moment != null)
				createdChirp.setMoment(moment);

			createdChirp.setTitle(title);
			createdChirp.setDescription(description);

			if (containsTaboo != null)
				createdChirp.setContainsTaboo(containsTaboo);

			final Chirp savedChirp = this.chirpService.save(createdChirp);

			// Flush
			this.chirpService.flush();

			if (moment != null)
				Assert.isTrue(!savedChirp.getMoment().equals(moment));

			if (containsTaboo != null)
				Assert.isTrue(savedChirp.getContainsTaboo() != containsTaboo);

			// 3. List my Chirps (display profile)

			final Collection<Chirp> myChirps = this.userService.findByUserAccount(LoginService.getPrincipal()).getChirps();

			Assert.isTrue(myChirps.contains(savedChirp));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	// -------------------------------------------------------------------------------
	// [UC-015] Administrator remove a Chirp.
	// 
	// Related Requirements:
	//   � REQ 17.5: An actor who is authenticated as an administrator must be able to
	//               remove a chirp that he or she thinks is inappropriate.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverRemoveChirp() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> chirp to remove.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) An Administrator correctly removes a chirp.
				"admin", "chirp12", null
			}, {
				// 2 - (-) An Administrator removes a non-existing chirp.
				"admin", "abcdefgh", IllegalArgumentException.class
			}, {
				// 3 - (-) An Administrator removes a null chirp.
				"admin", null, IllegalArgumentException.class
			}, {
				// 4 - (-) A not authenticated actor removes a chirp.
				null, "chirp12", IllegalArgumentException.class
			}, {
				// 5 - (-) A User removes a chirp.
				"user1", "chirp12", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Chirp chirp = null;

			if (i == 0 || i > 2)
				chirp = this.chirpService.findOne(super.getEntityId((String) testingData[i][1]));

			if (i == 1)
				chirp = new Chirp();

			this.startTransaction();

			this.templateRemoveChirp((String) testingData[i][0], chirp, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateRemoveChirp(final String username, final Chirp chirp, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List taboo Chirps
			final Collection<Chirp> oldTaboo = this.chirpService.findTabooedChirps();

			// 3. Remove a Chirp
			this.chirpService.delete(chirp);

			// Flush
			this.chirpService.flush();

			// 4. List taboo Chirps again and check
			final Collection<Chirp> newTaboo = this.chirpService.findTabooedChirps();

			Assert.isTrue(oldTaboo.contains(chirp));
			Assert.isTrue(!newTaboo.contains(chirp));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-012: Display Stream with Chirps
	 * 1. Log in to the system as a User
	 * 2. Display my Stream with Chirps
	 * 3. The user follows/unfollows another User (*)
	 * 4. Display my Stream with Chirps
	 * 
	 * 
	 * Involved REQs: 16.5
	 * 
	 * Test Cases (2; 2+ 2-):
	 * 
	 * + 1) A User logs in to the system and displays his/her list of streams. Follows a new User and retrieves it again.
	 * 
	 * + 2) A User logs in to the system and displays his/her list of streams. Unfollows a followee and retrieves it again.
	 * 
	 * - 3) An Administrator tries to retrieve the stream of chirps
	 * 
	 * - 4) An unauthenticated Actor tries to retrieve the stream of chirps
	 */

	@Test
	public void driverStreamChirp() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> username of the User to follow/unfollow
		// testingData[i][2] -> if we want to follow/unfollow (follow = true)
		// testingData[i][3] -> the expected Exception

		final Object testingData[][] = {
			{
				"user1", "user5", true, null
			}, {
				"user1", "user2", false, null
			}, {
				"admin", null, null, IllegalArgumentException.class
			}, {
				null, null, null, IllegalArgumentException.class
			}
		};

		User userToInteract = null;

		for (int i = 0; i < testingData.length; i++) {

			if ((String) testingData[i][1] != null)
				userToInteract = this.userService.findOne(this.getEntityId((String) testingData[i][1]));
			else
				userToInteract = null;

			this.startTransaction();

			this.templateStreamChirp((String) testingData[i][0], userToInteract, (Boolean) testingData[i][2], (Class<?>) testingData[i][3]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}

	protected void templateStreamChirp(final String actor, final User userToInteract, final Boolean follow, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system as a User
		this.authenticate(actor);

		try {

			// 2. Display my Stream with Chirps
			final Collection<Chirp> preChirps = new HashSet<Chirp>();
			preChirps.addAll(this.chirpService.getStream());

			// 3. The user follows/unfollows another User (*)

			if (follow)
				this.userService.follow(userToInteract);
			else
				this.userService.unfollow(userToInteract);

			this.userService.flush();

			// 4. Display my Stream with Chirps

			final Collection<Chirp> afterChirps = new HashSet<Chirp>(this.chirpService.getStream());

			Assert.isTrue(preChirps != afterChirps);

			if (follow)
				Assert.isTrue(afterChirps.containsAll(userToInteract.getChirps()));
			else
				Assert.isTrue(!afterChirps.contains(userToInteract.getChirps()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
