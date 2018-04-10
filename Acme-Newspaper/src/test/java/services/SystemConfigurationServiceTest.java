
package services;

import java.util.Collection;

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
import domain.Chirp;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SystemConfigurationServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	//Fixtures ---------------------------------------

	@Autowired
	private ArticleService				articleService;

	@Autowired
	private ChirpService				chirpService;

	@Autowired
	private NewspaperService			newspaperService;

	@PersistenceContext
	private EntityManager				entityManager;


	// -------------------------------------------------------------------------------
	// [UC-013]List taboo and remove a taboo word.
	// 
	// Related Requirements:
	//   · REQ 17.1: An actor who is authenticated as an administrator must be able to
	//               manage a list of taboo words.
	//   · REQ 17.2: An actor who is authenticated as an administrator must be able to
	//               list the articles that contain taboo words.
	//   · REQ 17.3: An actor who is authenticated as an administrator must be able to
	//               list the newspapers that contain taboo words.
	//   · REQ 17.4: An actor who is authenticated as an administrator must be able to
	//               list the chirps that contain taboo words.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverListAndRemoveTaboo() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> taboo article.
		// testingData[i][2] -> taboo newspaper.
		// testingData[i][3] -> taboo chirp.
		// testingData[i][4] -> taboo word to remove from the list.
		// testingData[i][5] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A Administrator removes an existing taboo word from the list.
				"admin", "article1", "newspaper1", "chirp1", "sex", null
			}, {
				// 2 - (-) A Administrator removes a non-existing taboo word from the list.
				"admin", "article1", "newspaper1", "chirp1", "rainbow", IllegalArgumentException.class
			}, {
				// 3 - (-) A User removes a taboo word from the list.
				"user1", "article1", "newspaper1", "chirp1", "sex", IllegalArgumentException.class
			}, {
				// 4 - (-) A not authenticated actor removes a taboo word from the list.
				null, "article1", "newspaper1", "chirp1", "sex", IllegalArgumentException.class
			}, {
				// 5 - (-) An Administrator removes a null taboo word from the list.
				"admin", "article1", "newspaper1", "chirp1", null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			final String tabooWord = (String) testingData[i][4];

			final Article article = this.articleService.findOne(super.getEntityId((String) testingData[i][1]));
			final Newspaper newspaper = this.newspaperService.findOne(super.getEntityId((String) testingData[i][2]));
			final Chirp chirp = this.chirpService.findOne(super.getEntityId((String) testingData[i][3]));

			if (tabooWord != null) {
				article.setTitle(tabooWord);
				newspaper.setTitle(tabooWord);
				chirp.setTitle(tabooWord);
			}

			this.startTransaction();

			this.templateListAndRemoveTaboo((String) testingData[i][0], article, newspaper, chirp, tabooWord, (Class<?>) testingData[i][5]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateListAndRemoveTaboo(final String username, final Article article, final Newspaper newspaper, final Chirp chirp, final String tabooWord, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 3. Remove the taboo word

			this.systemConfigurationService.deleteTabooWord(tabooWord);

			// Flush
			this.systemConfigurationService.flush();

			// 2. List taboo Articles/Newspapers/Chirps

			final Collection<Article> tabooArticles = this.articleService.findTabooedArticles();
			final Collection<Newspaper> tabooNewspapers = this.newspaperService.getTabooed();
			final Collection<Chirp> tabooChirps = this.chirpService.findTabooedChirps();

			Assert.isTrue(!tabooArticles.contains(article));
			Assert.isTrue(!tabooNewspapers.contains(newspaper));
			Assert.isTrue(!tabooChirps.contains(chirp));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
