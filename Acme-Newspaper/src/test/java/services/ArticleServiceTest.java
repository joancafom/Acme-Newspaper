
package services;

import java.util.Collection;
import java.util.HashSet;

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

import utilities.AbstractTest;
import domain.Article;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ArticleServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private ArticleService		articleService;

	//Fixtures ---------------------------------------

	@Autowired
	private NewspaperService	newspaperService;

	@PersistenceContext
	private EntityManager		entityManager;


	// -------------------------------------------------------------------------------
	// [UC-004]Create and Search for an Article.
	// 
	// Related Requirements:
	//   · REQ 3: For each article, the system must store a title, the moment when it
	//            is published, a summary, a piece of text (the body), and some
	//            optional pictures. An article is published when the corresponding
	//            newspaper is published.
	//   · REQ 4.4: An actor who is not authenticated must be able to search for a
	//              published article using a single key word that must appear
	//              somewhere in its title, summary, or body.
	//   · REQ 6.3: An actor who is authenticated as a user must be able to write an
	//              article and attach it to any newspaper that has not been published,
	//              yet. Note that articles may be saved in draft mode, which allows
	//              to modify them later, or final mode, which freezes them forever.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverCreateAndSearchArticle() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> newspaper where the article is going to be created.
		// testingData[i][2] -> title of the new article.
		// testingData[i][3] -> summary of the new article.
		// testingData[i][4] -> body of the new article.
		// testingData[i][5] -> pictures of the new article.
		// testingData[i][6] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A User correctly creates a new Article.
				"user1", "newspaper3", "niceTitle", "niceSummary", "niceBody", "http://www.images.com/picture1.png, http://www.images.com/picture2.png", null
			}, {
				// 2 - (-) A User creates an Article for a null Newspaper.
				"user1", null, "niceTitle", "niceSummary", "niceBody", "http://www.images.com/picture1.png, http://www.images.com/picture2.png", IllegalArgumentException.class
			}, {
				// 3 - (-) A User creates an Article for an already published Newspaper.
				"user1", "newspaper1", "niceTitle", "niceSummary", "niceBody", "http://www.images.com/picture1.png, http://www.images.com/picture2.png", IllegalArgumentException.class
			}, {
				// 4 - (-) An unauthenticated actor creates a correct Article.
				null, "newspaper3", "niceTitle", "niceSummary", "niceBody", "http://www.images.com/picture1.png, http://www.images.com/picture2.png", IllegalArgumentException.class
			}, {
				// 5 - (-) An User creates an Article with a null title.
				"user1", "newspaper3", null, "niceSummary", "niceBody", "http://www.images.com/picture1.png, http://www.images.com/picture2.png", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Newspaper newspaper = null;

			if ((String) testingData[i][1] != null) {
				newspaper = this.newspaperService.findOne(super.getEntityId((String) testingData[i][1]));

				for (final Article a : newspaper.getArticles())
					a.setIsFinal(true);
			}

			final Collection<String> pictures = new HashSet<String>();
			final String noSpaces = ((String) testingData[i][5]).replaceAll("\\s", "");
			final String[] parts = noSpaces.split(",");

			for (int j = 0; j < parts.length; j++)
				pictures.add(parts[j]);

			this.startTransaction();

			this.templateCreateAndSearchArticle((String) testingData[i][0], newspaper, (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], pictures, (Class<?>) testingData[i][6]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateCreateAndSearchArticle(final String username, final Newspaper newspaper, final String title, final String summary, final String body, final Collection<String> pictures, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List the unpublished Newspapers
			this.newspaperService.findAllUnpublished();

			// 3. Write an Article for one of those Newspapers

			final Article createdArticle = this.articleService.create(newspaper);

			createdArticle.setTitle(title);
			createdArticle.setSummary(summary);
			createdArticle.setBody(body);
			createdArticle.setPictures(pictures);
			createdArticle.setIsFinal(true);

			final Article savedArticle = this.articleService.save(createdArticle);

			// Flush
			this.articleService.flush();

			// 4. Publish the associated Newspaper
			super.unauthenticate();

			super.authenticate("user3");
			this.newspaperService.publish(newspaper);

			// Flush
			this.newspaperService.flush();

			// 5. Search for the created Article

			final Collection<Article> searchResults = this.articleService.findPublishedByKeyword(title);
			Assert.isTrue(searchResults.contains(savedArticle));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
