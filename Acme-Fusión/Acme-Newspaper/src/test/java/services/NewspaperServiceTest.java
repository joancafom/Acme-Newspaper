
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Article;
import domain.Newspaper;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class NewspaperServiceTest extends AbstractTest {

	//System Under Test

	@Autowired
	private NewspaperService	newspaperService;

	//Fixtures

	@Autowired
	private UserService			userService;

	@PersistenceContext
	private EntityManager		entityManager;


	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-003: Publish,Search and List Newspaper
	 * 1. Log in to the system as a User
	 * 2. List my Newspapers
	 * 3. Publish a Newspaper
	 * 4. (Log out)
	 * 5. Search for the published newspaper
	 * 
	 * 
	 * Involved REQs: 6.2, 4.5
	 * 
	 * Test Cases (5; 2+ 3-):
	 * 
	 * + 1) A User selects one of his/her Newspapers with all the articles in final mode and successfully
	 * publish it. Then he performs a search and the Newspaper is shown.
	 * 
	 * + 2) A User selects one of his/her Newspapers with all the articles in final mode and successfully
	 * publish it. Then he logs out and performs a search, and the Newspaper is shown.
	 * 
	 * - 3) A User tries to publish one of his/her Newspapers with but with some articles in draft mode. Then he looks
	 * up in the system for that newspaper using the searching tool (they must be final)
	 * 
	 * - 4) A User tries to publish one Newspaper created by another User, containing articles in final mode.
	 * Then he logs out and search for that newspaper using the searching tool (only the publisher can publish)
	 * 
	 * - 5) A User selects one of his/her Newspapers that is already published and tries to publish it.
	 * Then he performs a search for that newspaper. (a Newspaper cannot be published twice)
	 */

	@Test
	public void driverPublishSearchNewspaper() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> newspaper that's going to be publish.
		// testingData[i][2] -> username of the Actor that performs the search.
		// testingData[i][3] -> if we want to pre-make all articles final to ensure that it can be published.
		// testingData[i][4] -> the expected exception.

		final Object testingData[][] = {
			{
				"user3", "newspaper3", "user3", true, null
			}, {
				"user3", "newspaper3", null, true, null
			}, {
				"user3", "newspaper3", "user3", false, RuntimeException.class
			}, {
				"user1", "newspaper3", null, false, IllegalArgumentException.class
			}, {
				"user1", "newspaper1", "user1", false, IllegalArgumentException.class
			}
		};

		Newspaper newspaper;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][1] != null) {
				newspaper = this.newspaperService.findOne(this.getEntityId((String) testingData[i][1]));

				if ((Boolean) testingData[i][3]) {

					for (final Article a : newspaper.getArticles())
						a.setIsFinal(true);

					this.authenticate(this.userService.getPublisher(newspaper).getUserAccount().getUsername());
					newspaper = this.newspaperService.save(newspaper);
					this.unauthenticate();
				}

			} else
				newspaper = null;

			this.startTransaction();

			this.templatePublishSearchNewspaper((String) testingData[i][0], newspaper, (String) testingData[i][2], (Class<?>) testingData[i][4]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	//v1.0 - Implemented by JA
	protected void templatePublishSearchNewspaper(final String publisher, final Newspaper newspaper, final String searcher, final Class<?> expected) {

		Class<?> caught = null;

		//1. Log in to the system as a User
		this.authenticate(publisher);

		User currentUser = null;

		if (publisher.contains("user") && this.existsEntity(publisher))
			currentUser = this.userService.findOne(this.getEntityId(publisher));

		try {

			//2. List my Newspapers
			final Collection<Newspaper> allBeforePublish = new ArrayList<Newspaper>();

			if (currentUser != null)
				allBeforePublish.addAll(currentUser.getNewspapers());

			//3. Publish a Newspaper
			final Newspaper publishedNewspaper = this.newspaperService.publish(newspaper);

			this.newspaperService.flush();

			Assert.isTrue(publishedNewspaper.getPublicationDate() != null);
			Assert.isTrue(newspaper.getVersion() != publishedNewspaper.getVersion());
			Assert.isTrue(currentUser.getNewspapers().contains(publishedNewspaper));

			//4. (Log out)

			this.unauthenticate();
			this.authenticate(searcher);

			//5. Search for the published newspaper

			//The info is either contained in the title or the description
			String searchInfo;
			final Random rn = new Random();
			final Integer randomPick = rn.nextInt(2);

			//Pick one word
			if (randomPick.equals(0))
				searchInfo = publishedNewspaper.getTitle().split(" ")[0];
			else
				searchInfo = publishedNewspaper.getDescription().split(" ")[0];

			final Collection<Newspaper> searchResults = this.newspaperService.findPublishedByKeyword(searchInfo);

			Assert.isTrue(searchResults.contains(publishedNewspaper));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);

	}

	/*
	 * v1.0 - josembell
	 * [UC-007] - List and Remove a Newspaper
	 * 1. Log in to the system as Admin
	 * 2. List the newspapers
	 * 3. Select one and remove it
	 * 4. List the newspapers
	 * 
	 * REQ: 5.1, 7.2
	 */
	@Test
	public void driverListAndRemoveNewspaper() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador selecciona un periodico existente y lo elimina */
				"admin", "newspaper1", null
			}, {
				/* - 2) Un usuario no identificado elimina un periodico */
				null, "newspaper1", IllegalArgumentException.class
			}, {
				/* - 3) Un administrador selecciona un periodico null */
				"admin", null, IllegalArgumentException.class
			}, {
				/* - 4) Un usuario intenta eliminar un periódico */
				"user1", "newspaper1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			Newspaper newspaper = null;
			if (testingData[i][1] != null)
				newspaper = this.newspaperService.findOne(this.getEntityId((String) testingData[i][1]));

			this.startTransaction();

			System.out.println("test " + i);
			this.templateListAndRemoveNewspaper((String) testingData[i][0], newspaper, (Class<?>) testingData[i][2]);
			System.out.println("test " + i + " ok");

			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}
	/* v1.0 - josembell */
	protected void templateListAndRemoveNewspaper(final String user, final Newspaper newspaper, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Loggearse como admin */
		this.authenticate(user);

		try {
			/* 2. Listar todos los periodicos */
			final Collection<Newspaper> newspapers1 = this.newspaperService.findAll();
			final int numNewspapers1 = newspapers1.size();

			/* 3. Seleccionar uno -> entra por parámetros */
			/* 4. Eliminarlo */
			this.newspaperService.delete(newspaper);

			this.newspaperService.flush();

			final Collection<Newspaper> newspapers2 = this.newspaperService.findAll();
			final int numNewspapers2 = newspapers2.size();

			/* 5. Listar todos los periodicos */

			Assert.isTrue(numNewspapers2 + 1 == numNewspapers1);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}
}
