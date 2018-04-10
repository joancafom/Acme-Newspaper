
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
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
	 * [UC-002] - List and Create a Newspaper
	 * 1. Log In to the system
	 * 2. List Published Newspapers
	 * 3. Log In to the system as an user
	 * 4. Create a new Newspaper
	 * 5. List Unpublished Newspapers
	 * 
	 * REQ: 2, 4.2, 6.1
	 */
	@Test
	public void driverListAndCreateNewspaper() {
		final Object testingData[][] = {
			{
				/* + 1) Un usuario identificado crea un periodico */
				"user1", "Titulo", "Descripción", "http://clasedetecnologia.es/wp-content/uploads/2017/02/Test-Computer-Key-by-Stuart-Miles.jpg", null
			}, {
				/* - 2) Un usuario no identificado crea un periodico */
				null, "Titulo", "Descripción", "http://clasedetecnologia.es/wp-content/uploads/2017/02/Test-Computer-Key-by-Stuart-Miles.jpg", IllegalArgumentException.class
			}, {
				/* - 3) Un usuario identificado crea un periódico sin título */
				"user1", "", "Descripción", "http://clasedetecnologia.es/wp-content/uploads/2017/02/Test-Computer-Key-by-Stuart-Miles.jpg", ConstraintViolationException.class
			}, {
				/* - 4) Un usuario identificado crea un periódico sin descripción */
				"user1", "Titulo", "", "http://clasedetecnologia.es/wp-content/uploads/2017/02/Test-Computer-Key-by-Stuart-Miles.jpg", ConstraintViolationException.class
			}, {
				/* - 5) Un administrador intenta crear un periódico */
				"admin", "Título", "Descripción", "http://clasedetecnologia.es/wp-content/uploads/2017/02/Test-Computer-Key-by-Stuart-Miles.jpg", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();
			//System.out.println("Test" + i);
			this.templateListAndCreateNewspaper((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			//System.out.println("Test" + i + " ok");
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	protected void templateListAndCreateNewspaper(final String username, final String title, final String description, final String picture, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Listar los periódicos publicados */
		final Collection<Newspaper> publishedNewspapers = this.newspaperService.findAllPublished();
		final int numPublishedNewspapers = publishedNewspapers.size();

		/* 2. Loggerse como usuario */
		this.authenticate(username);

		try {
			final Collection<Newspaper> unpublishedNewspapers = this.newspaperService.findAllUnpublished();
			final int numUnpublishedNewspapers = unpublishedNewspapers.size();

			/* 3. Crear un nuevo periódico */
			final Newspaper newspaper = this.newspaperService.create();
			newspaper.setTitle(title);
			newspaper.setDescription(description);
			newspaper.setPicture(picture);

			final Newspaper savedNewspaper = this.newspaperService.save(newspaper);
			final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
			Assert.isTrue(user.getNewspapers().contains(savedNewspaper));

			final Collection<Newspaper> unpublishedNewspapers2 = this.newspaperService.findAllUnpublished();
			final int numUnpublishedNewspapers2 = unpublishedNewspapers2.size();
			final Collection<Newspaper> publishedNewspapers2 = this.newspaperService.findAllPublished();
			final int numPublishedNewspapers2 = publishedNewspapers2.size();

			Assert.isTrue(numUnpublishedNewspapers + 1 == numUnpublishedNewspapers2);
			Assert.isTrue(numPublishedNewspapers == numPublishedNewspapers2);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();
	}
}
