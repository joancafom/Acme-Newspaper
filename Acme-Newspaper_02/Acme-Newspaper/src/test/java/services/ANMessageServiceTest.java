
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import utilities.AbstractTest;
import domain.ANMessage;
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ANMessageServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private ANMessageService	anMessageService;

	//Fixtures ---------------------------------------

	@Autowired
	private ActorService		actorService;

	@PersistenceContext
	private EntityManager		entityManager;


	// -------------------------------------------------------------------------------
	// [UC-035] Delete a Message
	// 
	// Related Requirements:
	//   · REQ 13.1: An actor who is authenticated must be able to exchange messages
	//               with other actors and manage them, which includes deleting and
	//               moving them from one folder to another folder.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverDeleteMessage() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> message to delete.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A User successfully deletes a Message.
				"user1", "anMessage1", null
			}, {
				// 2 - (-) A Customer deletes a Message that doesn't belong to her, but she sent.
				"customer1", "anMessage4copy", IllegalArgumentException.class
			}, {
				// 3 - (-) An Administrator deletes a null Message.
				"admin", null, IllegalArgumentException.class
			}, {
				// 4 - (-) An Agent deletes an non-existing Message.
				"agent1", "", IllegalArgumentException.class
			}, {
				// 5 - (-) An unauthenticated actor deletes a Message.
				null, "anMessage1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			final String bean = (String) testingData[i][1];
			ANMessage anMessage = null;

			if (bean != null && bean.contains("anMessage"))
				anMessage = this.anMessageService.findOneTest(super.getEntityId(bean));
			else if (bean == "")
				anMessage = new ANMessage();

			this.startTransaction();

			this.templateDeleteMessage((String) testingData[i][0], anMessage, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateDeleteMessage(final String username, final ANMessage anMessage, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. Send the Message to the Trash Box (first delete)

			this.anMessageService.delete(anMessage);

			// Flush
			this.anMessageService.flush();

			Assert.isTrue(anMessage.getFolder().getName().equals("Trash Box"));

			// 3. Permanently remove the Message (second delete)

			final ANMessage anMessage2 = this.anMessageService.findOneTest(anMessage.getId());

			this.anMessageService.delete(anMessage2);

			// Flush
			this.anMessageService.flush();

			final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(!actor.getReceivedMessages().contains(anMessage));
			Assert.isTrue(!actor.getSentMessages().contains(anMessage));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}
}
