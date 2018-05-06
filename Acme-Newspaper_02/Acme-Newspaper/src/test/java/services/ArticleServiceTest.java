
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-006: Remove Article
	 * 1. Log in to the system as an Administrator
	 * 2. List the Newspapers (*)
	 * 3. Select a Newspaper
	 * 4. Select one of its articles and remove it
	 * 
	 * 
	 * Involved REQs: 5.1, 7.1
	 * 
	 * Test Cases (6; 2+ 4-):
	 * 
	 * + 1) An Admin logs in a select a final article from a Newspaper to remove and successfully performs the operation
	 * 
	 * - 2) An Admin logs in a select a draft article from a Newspaper and tries to remove it from the system (only final ones)
	 * 
	 * + 3) An Admin logs in a select a followUp article from a Newspaper to remove and successfully performs the operation
	 * 
	 * - 4) A User logs in a select an article from a Newspaper to remove (only administrators can do so)
	 * 
	 * - 5) An Admin logs in a tries to remove a null article (not null)
	 * 
	 * - 6) An unauthenticated Actor tries to remove an article from a Newspaper (only administrators can do so)
	 */

	@Test
	public void driverRemoveArticle() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> article to be removed.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				"admin", "article1", null
			}, {
				"admin", "article6", IllegalArgumentException.class
			}, {
				"admin", "article4", null
			}, {
				"user1", "article1", IllegalArgumentException.class
			}, {
				"admin", null, IllegalArgumentException.class
			}, {
				null, "article1", IllegalArgumentException.class
			}
		};

		Article article = null;

		for (int i = 0; i < testingData.length; i++) {

			if ((String) testingData[i][1] != null)
				article = this.articleService.findOne(this.getEntityId((String) testingData[i][1]));
			else
				article = null;

			this.startTransaction();

			this.templateRemoveArticle((String) testingData[i][0], article, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}

	protected void templateRemoveArticle(final String actor, final Article article, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system as an Administrator
		this.authenticate(actor);

		try {

			// 2. List the Newspapers (*)

			// 3. Select a Newspaper

			Newspaper articleNewspaper = null;
			if (article != null)
				articleNewspaper = article.getNewspaper();

			// 4. Select one of its articles and remove it (given in the parameters)

			this.articleService.delete(article);

			Assert.isTrue(!articleNewspaper.getArticles().contains(article));
			Assert.isNull(this.articleService.findOne(article.getId()));

			for (final Article followUp : article.getFollowUps())
				Assert.isNull(followUp.getMainArticle());

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-017: Write a Follow Up
	 * 1. Log in to the system as a User
	 * 2. List Published Newspapers (*)
	 * 3. Select one newspaper with an Article written by the User
	 * 4. Write a Follow Up about it
	 * 5. Display the Newspaper of the followUp
	 * 
	 * 
	 * Involved REQs: 14
	 * 
	 * Test Cases (5; 1+ 4-):
	 * 
	 * + 1) A User logs in to the system and successfully writes a follow up of an Article he/she has written previously and
	 * that is saved in another newspaper.
	 * 
	 * - 2) A User tries to write a follow up of an Article of his/her own that is final, but not published yet.
	 * 
	 * - 3) A User tries to write a follow up of an Article of his/her own that is in draft mode.
	 * 
	 * - 4) A User tries to write a follow up of an Article of another Actor that is published in another Newspaper.
	 * 
	 * - 5) A User tries to write a null follow up of an Article of his/her own that is final and published.
	 * 
	 * - 2) An Admin tries to write a follow up of an Article that is published.
	 */

	@Test
	public void driverWriteFollowUp() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> whether to use the provided FollowUp or to use the generic (true=generic)
		// testingData[i][2] -> the beanName of the followUp to save
		// testingData[i][3] -> the article we want to associate to the follow up
		// testingData[i][4] -> the newspaper where to publish the follow up article
		// testingData[i][5] -> the expected Exception

		final Object testingData[][] = {
			{
				"user1", false, null, "article1", "newspaper4", null
			}, {
				"user3", false, null, "article6", "newspaper4", IllegalArgumentException.class
			}, {
				"user3", false, null, "article7", "newspaper4", IllegalArgumentException.class
			}, {
				"user2", false, null, "article1", "newspaper4", IllegalArgumentException.class
			}, {
				"user1", true, null, "article1", "newspaper4", IllegalArgumentException.class
			}, {
				"admin", false, null, "article1", "newspaper4", IllegalArgumentException.class
			}
		};

		Article mainArticle = null;
		Article followUp = null;
		Newspaper newspaper = null;

		for (int i = 0; i < testingData.length; i++) {

			if ((String) testingData[i][3] != null)
				mainArticle = this.articleService.findOne(this.getEntityId((String) testingData[i][3]));
			else
				mainArticle = null;

			if ((String) testingData[i][2] != null)
				followUp = this.articleService.findOne(this.getEntityId((String) testingData[i][2]));
			else
				followUp = null;

			if ((String) testingData[i][4] != null)
				newspaper = this.newspaperService.findOne(this.getEntityId((String) testingData[i][4]));
			else
				newspaper = null;

			this.startTransaction();

			this.templateWriteFollowUp((String) testingData[i][0], mainArticle, (Boolean) testingData[i][1], followUp, newspaper, (Class<?>) testingData[i][5]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}

	protected void templateWriteFollowUp(final String actor, final Article mainArticle, final Boolean useProvided, final Article providedFollowUp, final Newspaper newspaper, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system as a User
		this.authenticate(actor);

		try {

			// 2. List Published Newspapers (*)

			// 3. Select one newspaper with an Article written by the User

			Newspaper mainNewspaper = null;

			if (mainArticle != null)
				mainNewspaper = mainArticle.getNewspaper();

			// 4. Write a Follow Up about it

			final Article followUpToSave;

			if (useProvided)
				followUpToSave = providedFollowUp;
			else {
				followUpToSave = this.articleService.create(mainArticle);
				followUpToSave.setTitle("Test Title");
				followUpToSave.setBody("Test Body");
				followUpToSave.setSummary("Test Summary");
				followUpToSave.setContainsTaboo(false);
				followUpToSave.setIsFinal(false);
			}

			if (followUpToSave != null)
				followUpToSave.setNewspaper(newspaper);

			final Article savedFollowUp = this.articleService.save(followUpToSave);

			this.articleService.flush();

			//5. Display the Newspaper of the followUp

			final Newspaper newspaperAfter = this.newspaperService.findOne(newspaper.getId());

			Assert.isTrue(newspaperAfter.getArticles().contains(savedFollowUp));
			Assert.isTrue(!mainNewspaper.getArticles().contains(savedFollowUp));
			Assert.isTrue(this.articleService.findOne(mainArticle.getId()).getFollowUps().contains(savedFollowUp));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-005 *EXTENDED*: List an Edit Articles
	 * 1. Log in to the system as a User
	 * 2. List the Unpublished Newspapers
	 * 3. Select an Unpublished newspaper where he/she's written a draft article
	 * 4. Retrieve the article and modify it
	 * 
	 * Involved REQs: 3, 6.3, 4.4
	 * 
	 * Test Cases (1; 1+ 12-):
	 * 
	 * + 1) A User logs in to the system, and successfully modifies an article in draft mode he/she has written (pictures empty).
	 * 
	 * - 2) A User logs in to the system and tries to modify an article in final mode he/she has written. (only draft mode is possible)
	 * 
	 * - 3) A User logs in to the system and tries to modify an article in draft mode another User has written. (only his/her own article)
	 * 
	 * - 4) An unauthenticated Actor tries to modify an article in draft mode another User has written. (only users can do so).
	 * 
	 * - 5) A User logs in to the system and tries to modify an article in draft mode he/she has written with invalid data. (null collection pictures)
	 * 
	 * - 6) A User logs in to the system and tries to modify an article in draft mode he/she has written with invalid data. (pictures not being URLs)
	 * 
	 * - 7) A User logs in to the system and tries to modify an article in draft mode he/she has written with invalid data. (blank title)
	 * 
	 * - 8) A User logs in to the system and tries to modify an article in draft mode he/she has written with invalid data. (XSSd title)
	 * 
	 * - 9) A User logs in to the system and tries to modify an article in draft mode he/she has written with invalid data. (blank body)
	 * 
	 * - 10) A User logs in to the system and tries to modify an article in draft mode he/she has written with invalid data. (XSSd body)
	 * 
	 * - 11) A User logs in to the system and tries to modify an article in draft mode he/she has written with invalid data. (blank summary)
	 * 
	 * - 12) A User logs in to the system and tries to modify an article in draft mode he/she has written with invalid data. (XSSd summary)
	 * 
	 * - 13) A User logs in to the system and tries to modify an article in draft invalid data. (change isFinal property true->false)
	 */

	@Test
	public void driverListEditArticle() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> the article we want to change
		// testingData[i][2] -> String with the properties we want to change
		// testingData[i][3] -> the new title
		// testingData[i][4] -> the new summary
		// testingData[i][5] -> the new body
		// testingData[i][6] -> the new pictures
		// testingData[i][7] -> the new isFinal
		// testingData[i][8] -> the expected Exception

		final Object testingData[][] = {
			{
				"user3", "article6", "pictures", null, null, null, "", null, null
			}, {
				"user1", "article1", "title", "test", null, null, "", null, IllegalArgumentException.class
			}, {
				"user1", "article6", "pictures", null, null, null, "", null, IllegalArgumentException.class
			}, {
				null, "article6", "pictures", null, null, null, "", null, IllegalArgumentException.class
			}, {
				"user3", "article6", "pictures", null, null, null, null, null, IllegalArgumentException.class
			}, {
				"user3", "article6", "pictures", null, null, null, null, "raca, notURl", IllegalArgumentException.class
			}, {
				"user3", "article6", "title", "", null, null, null, null, ConstraintViolationException.class
			}, {
				"user3", "article6", "title", "<script>alert('Hacked');</script>", null, null, null, null, ConstraintViolationException.class
			}, {
				"user3", "article6", "body", null, null, "", null, null, ConstraintViolationException.class
			}, {
				"user3", "article6", "body", null, null, "<script>alert('Hacked');</script>", null, null, ConstraintViolationException.class
			}, {
				"user3", "article6", "summary", null, "", null, null, null, ConstraintViolationException.class
			}, {
				"user3", "article6", "summary", null, "<script>alert('Hacked');</script>", null, null, null, ConstraintViolationException.class
			}, {
				"user2", "article5", "isFinal", null, null, null, null, false, IllegalArgumentException.class
			}
		};

		Article articleToModify = null;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][1] != null)
				articleToModify = this.articleService.findOne(this.getEntityId((String) testingData[i][1]));
			else
				articleToModify = null;

			final String changes = (String) testingData[i][2];

			if (articleToModify != null) {

				if (changes.contains("title"))
					articleToModify.setTitle((String) testingData[i][3]);

				if (changes.contains("summary"))
					articleToModify.setSummary((String) testingData[i][4]);

				if (changes.contains("body"))
					articleToModify.setBody((String) testingData[i][5]);

				if (changes.contains("pictures")) {
					final String urlString = (String) testingData[i][6];
					Collection<String> urls = new ArrayList<String>();
					if (urlString != null) {
						if (!urlString.equals(""))
							urls.addAll(Arrays.asList(urlString.split(",")));
					} else
						urls = null;
					articleToModify.setPictures(urls);
				}

				if (changes.contains("isFinal"))
					articleToModify.setIsFinal((Boolean) testingData[i][7]);

			}

			this.startTransaction();

			this.templateListEditArticle((String) testingData[i][0], articleToModify, (String) testingData[i][2], (Class<?>) testingData[i][8]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}
	}
	protected void templateListEditArticle(final String username, final Article modifiedArticle, final String changes, final Class<?> expected) {

		Class<?> caught = null;

		/*
		 * 1. Log in to the system as a User
		 * 2. List the Unpublished Newspapers
		 * 3. Select an Unpublished newspaper where he/she's written a draft article
		 * 4. Retrieve the article and modify it
		 */

		// 1. Log in to the system as a User
		this.authenticate(username);

		try {

			// 2. List Unpublished Newspapers
			final List<Newspaper> unpublishedBefore = new ArrayList<Newspaper>(this.newspaperService.findAllUnpublished());

			// 3. Select an Unpublished newspaper where he/she's written a draft article (provided)

			// 4. Retrieve the article and modify it
			final Article savedArticle = this.articleService.save(modifiedArticle);
			this.articleService.flush();

			final List<Newspaper> unpublishedAfter = new ArrayList<Newspaper>(this.newspaperService.findAllUnpublished());

			Assert.isTrue(unpublishedBefore.containsAll(unpublishedAfter));
			Assert.isTrue(unpublishedBefore.size() == unpublishedAfter.size());

			for (int i = 0; i < unpublishedAfter.size(); i++)
				Assert.isTrue(unpublishedBefore.get(i).equals(unpublishedAfter.get(i)));

			if (changes.contains("title"))
				Assert.isTrue(savedArticle.getTitle().contains("test"));

			if (changes.contains("summary"))
				Assert.isTrue(savedArticle.getSummary().contains("test"));

			if (changes.contains("body"))
				Assert.isTrue(savedArticle.getBody().contains("test"));

			if (changes.contains("pictures")) {
				Boolean hasChanged = false;
				for (final String url : savedArticle.getPictures())
					if (url.contains("test")) {
						hasChanged = true;
						break;
					}

				Assert.isTrue(hasChanged || savedArticle.getPictures().isEmpty());
			}

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
