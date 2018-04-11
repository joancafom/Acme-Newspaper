
package services;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;

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
import domain.Newspaper;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private AdministratorService	administratorService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager			entityManager;

	@Autowired
	private UserService				userService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private articleService 			articleService;


	/*
	 * v1.0 - josembell
	 * [UC-008] - Display Dashboard
	 * 
	 * REQ: 7.3
	 */
	@Test
	public void driverAverageNewspapersPerUser() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateAverageNewspapersPerUser((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	private void templateAverageNewspapersPerUser(final String username, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getAvgNewspapersPerUser();

			/* Computar */
			Double computeResult = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.userService.findAll().size());
			for (final User u : this.userService.findAll())
				num = num + u.getNewspapers().size();

			computeResult = num / denom;
			Assert.isTrue(queryResult.equals(computeResult));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - josembell
	 * [UC-008] - Display Dashboard
	 * 
	 * REQ: 7.3
	 */
	@Test
	public void driverStandardDeviationNewspapersPerUser() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateStandardDeviationNewspapersPerUser((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	private void templateStandardDeviationNewspapersPerUser(final String username, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getStdNewspapersPerUser();

			/* Computar */
			Double media = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.userService.findAll().size());
			for (final User u : this.userService.findAll())
				num = num + u.getNewspapers().size();

			media = num / denom;

			Double computeResult = 0.0;
			Double num1 = 0.0;
			for (final User u : this.userService.findAll())
				num1 = num1 + Math.pow(u.getNewspapers().size() - media, 2);

			computeResult = Math.sqrt(num1 / denom);

			final DecimalFormat fmt = new DecimalFormat(".##");

			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - josembell
	 * [UC-008] - Display Dashboard
	 * 
	 * REQ: 7.3
	 */
	@Test
	public void driverAverageArticlesPerWriter() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateAverageArticlesPerWriter((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	private void templateAverageArticlesPerWriter(final String username, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getAvgArticlesPerWriter();

			/* Computar */
			Double computeResult = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.userService.findAll().size());
			for (final User u : this.userService.findAll())
				num = num + u.getArticles().size();

			computeResult = num / denom;

			final DecimalFormat fmt = new DecimalFormat(".##");

			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - josembell
	 * [UC-008] - Display Dashboard
	 * 
	 * REQ: 7.3
	 */
	@Test
	public void driverStandardDeviationArticlesPerWriter() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateStandardDeviationArticlesPerWriter((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	private void templateStandardDeviationArticlesPerWriter(final String username, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getStdArticlesPerWriter();

			/* Computar */
			Double media = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.userService.findAll().size());
			for (final User u : this.userService.findAll())
				num = num + u.getArticles().size();

			media = num / denom;

			Double computeResult = 0.0;
			Double num1 = 0.0;
			for (final User u : this.userService.findAll())
				num1 = num1 + Math.pow(u.getArticles().size() - media, 2);

			computeResult = Math.sqrt(num1 / denom);

			final DecimalFormat fmt = new DecimalFormat(".##");

			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - josembell
	 * [UC-008] - Display Dashboard
	 * 
	 * REQ: 7.3
	 */
	@Test
	public void driverAverageArticlesPerNewspaper() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateAverageArticlesPerNewspaper((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	private void templateAverageArticlesPerNewspaper(final String username, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getAvgArticlesPerNewspaper();

			/* Computar */
			Double computeResult = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.newspaperService.findAll().size());
			for (final Newspaper n : this.newspaperService.findAll())
				num = num + n.getArticles().size();

			computeResult = num / denom;

			final DecimalFormat fmt = new DecimalFormat(".##");

			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - josembell
	 * [UC-008] - Display Dashboard
	 * 
	 * REQ: 7.3
	 */
	@Test
	public void driverStandardDeviationArticlesPerNewspaper() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateStandardDeviationArticlesPerNewspaper((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	private void templateStandardDeviationArticlesPerNewspaper(final String username, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getStdArticlesPerNewspaper();

			/* Computar */
			Double media = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.newspaperService.findAll().size());
			for (final Newspaper n : this.newspaperService.findAll())
				num = num + n.getArticles().size();

			media = num / denom;

			Double computeResult = 0.0;
			Double num1 = 0.0;
			for (final Newspaper n : this.newspaperService.findAll())
				num1 = num1 + Math.pow(n.getArticles().size() - media, 2);

			computeResult = Math.sqrt(num1 / denom);

			final DecimalFormat fmt = new DecimalFormat(".##");

			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - josembell
	 * [UC-008] - Display Dashboard
	 * 
	 * REQ: 7.3
	 */
	@Test
	public void driverNewspapersWith10PercentMoreArticlesAverage() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateNewspapersWith10PercentMoreArticlesAverage((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	protected void templateNewspapersWith10PercentMoreArticlesAverage(final String username, final Class<?> expected) {
		Class<?> caught = null;
		this.authenticate(username);

		try {
			/* Computar */
			Double media = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.newspaperService.findAll().size());
			for (final Newspaper n : this.newspaperService.findAll())
				num = num + n.getArticles().size();

			media = num / denom;
			final Double mediaPlus10Percent = media + media * 0.1;
			final Collection<Newspaper> res = new HashSet<Newspaper>();
			for (final Newspaper n : this.newspaperService.findAll())
				if (n.getArticles().size() > mediaPlus10Percent)
					res.add(n);

			final Collection<Newspaper> queryResult = this.administratorService.getNewspapers10MoreArticlesThanAverage();
			for (final Newspaper n : queryResult)
				Assert.isTrue(res.contains(n));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * v1.0 - josembell
	 * [UC-008] - Display Dashboard
	 * 
	 * REQ: 7.3
	 */
	@Test
	public void driverNewspapersWith10PercentFewerArticlesAverage() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateNewspapersWith10PercentFewerArticlesAverage((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	protected void templateNewspapersWith10PercentFewerArticlesAverage(final String username, final Class<?> expected) {
		Class<?> caught = null;
		this.authenticate(username);

		try {
			final Collection<Newspaper> queryResult = this.administratorService.getNewspapers10FewerArticlesThanAverage();

			/* Computar */
			Double media = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.newspaperService.findAll().size());
			for (final Newspaper n : this.newspaperService.findAll())
				num = num + n.getArticles().size();

			media = num / denom;
			final Double mediaPlus10Percent = media - media * 0.1;
			final Collection<Newspaper> res = new HashSet<Newspaper>();
			for (final Newspaper n : this.newspaperService.findAll())
				if (n.getArticles().size() < mediaPlus10Percent)
					res.add(n);

			for (final Newspaper n : queryResult)
				Assert.isTrue(res.contains(n));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * v1.0 - josembell
	 * [UC-008] - Display Dashboard
	 * 
	 * REQ: 7.3
	 */
	@Test
	public void driverRatioUsersWithNewspaper() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateRatioUsersWithNewspaper((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	protected void templateRatioUsersWithNewspaper(final String username, final Class<?> expected) {
		Class<?> caught = null;
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getRatioUsersHaveCreatedANewspaper();

			/* Computar */
			Double computeResult = 0.0;
			for (final User u : this.userService.findAll())
				if (u.getNewspapers().size() != 0)
					computeResult++;

			computeResult = computeResult / this.userService.findAll().size();
			final DecimalFormat fmt = new DecimalFormat(".##");
			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * v1.0 - josembell
	 * [UC-008] - Display Dashboard
	 * 
	 * REQ: 7.3
	 */
	@Test
	public void driverRatioUsersWithArticles() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateRatioUsersWithArticles((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	protected void templateRatioUsersWithArticles(final String username, final Class<?> expected) {
		Class<?> caught = null;
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getRatioUsersHaveCreatedANewspaper();

			/* Computar */
			Double computeResult = 0.0;
			for (final User u : this.userService.findAll())
				if (u.getArticles().size() != 0)
					computeResult++;

			computeResult = computeResult / this.userService.findAll().size();
			final DecimalFormat fmt = new DecimalFormat(".##");
			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

		/*
	 * v1.0 - josembell
	 * [UC-016] - Display Dashboard
	 * 
	 * REQ: 17.6
	 */
	@Test
	public void driverAverageFollowUpsPerArticle() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateAverageFollowUpsPerArticle((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	private void templateAverageFollowUpsPerArticle(final String username, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getAvgFollowUpsPerArticle();

			/* Computar */
			Double computeResult = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.articleService.findAll().size());
			for (final Article a : this.articleService.findAll())
				num = num + a.getFollowUps().size();

			computeResult = num / denom;

			final DecimalFormat fmt = new DecimalFormat(".##");

			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - josembell
	 * [UC-016] - Display Dashboard
	 * 
	 * REQ: 17.6
	 */
	@Test
	public void driverAverageFollowUpsOneWeekAfterPerArticle() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateAverageFollowUpsOneWeekAfterPerArticle((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	private void templateAverageFollowUpsOneWeekAfterPerArticle(final String username, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getAvgFollowUpsPerArticleOneWeek();

			/* Computar */
			Double computeResult = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.articleService.findAll().size());
			for (final Article a : this.articleService.findAll())
				for (final Article followUp : a.getFollowUps()) {
					final LocalDateTime date1 = new LocalDateTime(followUp.getPublicationDate());
					final LocalDateTime date2 = new LocalDateTime(a.getPublicationDate());

					if (date2.plusDays(7).isBefore(date1))
						num++;
				}

			computeResult = num / denom;

			final DecimalFormat fmt = new DecimalFormat(".##");

			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - josembell
	 * [UC-016] - Display Dashboard
	 * 
	 * REQ: 17.6
	 */
	@Test
	public void driverAverageFollowUpTwoWeeksAfterPerArticle() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateAverageFollowUpTwoWeeksAfterPerArticle((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	private void templateAverageFollowUpTwoWeeksAfterPerArticle(final String username, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getAvgFollowUpsPerArticleTwoWeeks();

			/* Computar */
			Double computeResult = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.articleService.findAll().size());
			for (final Article a : this.articleService.findAll())
				for (final Article followUp : a.getFollowUps()) {
					final LocalDateTime date1 = new LocalDateTime(followUp.getPublicationDate());
					final LocalDateTime date2 = new LocalDateTime(a.getPublicationDate());

					if (date2.plusDays(14).isBefore(date1))
						num++;
				}

			computeResult = num / denom;

			final DecimalFormat fmt = new DecimalFormat(".##");

			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - josembell
	 * [UC-016] - Display Dashboard
	 * 
	 * REQ: 17.6
	 */
	@Test
	public void driverAverageChirpsPerUser() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateAverageChirpsPerUser((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	private void templateAverageChirpsPerUser(final String username, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getAvgChirpsPerUser();

			/* Computar */
			Double computeResult = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.userService.findAll().size());
			for (final User u : this.userService.findAll())
				num = num + u.getChirps().size();

			computeResult = num / denom;

			final DecimalFormat fmt = new DecimalFormat(".##");

			Assert.isTrue(fmt.format(computeResult).equals(fmt.format(queryResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - josembell
	 * [UC-016] - Display Dashboard
	 * 
	 * REQ: 17.6
	 */
	@Test
	public void driverStandardDeviationChirpsPerUser() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateStandardDeviationChirpsPerUser((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	private void templateStandardDeviationChirpsPerUser(final String username, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getStdChirpsPerUser();

			/* Computar */
			Double media = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.userService.findAll().size());
			for (final User u : this.userService.findAll())
				num = num + u.getChirps().size();

			media = num / denom;

			Double computeResult = 0.0;
			Double num1 = 0.0;
			for (final User u : this.userService.findAll())
				num1 = num1 + Math.pow(u.getChirps().size() - media, 2);

			computeResult = Math.sqrt(num1 / denom);

			final DecimalFormat fmt = new DecimalFormat(".##");

			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - josembell
	 * [UC-016] - Display Dashboard
	 * 
	 * REQ: 17.6
	 */
	@Test
	public void driverRatioUsersWith75PercentChirps() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador ejecuta la query */
				"admin", null
			}, {
				/* - 2) Un usuario no identificado ejecuta la query */
				null, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario ejecuta la query */
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			this.templateRatioUsersWith75PercentChirps((String) testingData[i][0], (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	protected void templateRatioUsersWith75PercentChirps(final String username, final Class<?> expected) {
		Class<?> caught = null;
		this.authenticate(username);

		try {
			final Double queryResult = this.administratorService.getRatioUsersAbove75AvgChirps();

			/* Computar */
			Double media = 0.0;
			Double num = 0.0;
			final Double denom = new Double(this.userService.findAll().size());
			for (final User u : this.userService.findAll())
				num = num + u.getChirps().size();

			media = num / denom;
			final Double mediaPlus75Percent = media + media * 0.75;

			/* Computar */
			Double computeResult = 0.0;

			for (final User u : this.userService.findAll())
				if (u.getChirps().size() > mediaPlus75Percent)
					computeResult++;

			computeResult = computeResult / this.userService.findAll().size();
			final DecimalFormat fmt = new DecimalFormat(".##");
			Assert.isTrue(fmt.format(queryResult).equals(fmt.format(computeResult)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

}
