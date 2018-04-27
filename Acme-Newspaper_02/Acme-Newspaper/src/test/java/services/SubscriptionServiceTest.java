
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
import domain.CreditCard;
import domain.Newspaper;
import domain.Subscription;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SubscriptionServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private SubscriptionService	subscriptionService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager		entityManager;

	@Autowired
	private NewspaperService	newspaperService;


	/*
	 * v1.0 - josembell
	 * [UC-019] - List and Suscribe to a Newspaper
	 * 
	 * REQ: 20, 22
	 */
	@Test
	public void driverListSuscribeNewspaper() {
		final Object testingData[][] = {
			{
				/* + 1) Un customer se suscribe introduciendo información valida a un periodico privado */
				"customer2", "newspaper2", "Jose Bellido", "BBVA", "373185782581613", 859, 12, 2019, null
			}, {
				/* - 2) Un usuario no identificado se suscribe a un periodico */
				null, "newspaper2", "Jose Bellido", "BBVA", "373185782581613", 859, 12, 2019, IllegalArgumentException.class
			}, {
				/* - 3) Un user se suscribe a un periodico */
				"user2", "newspaper2", "Jose Bellido", "BBVA", "373185782581613", 859, 12, 2019, IllegalArgumentException.class
			}, {
				/* - 4) Un admin se suscribe a un periodico */
				"admin", "newspaper2", "Jose Bellido", "BBVA", "373185782581613", 859, 12, 2019, IllegalArgumentException.class
			}, {
				/* - 5) Un customer se suscribe a un periodico null */
				"customer2", null, "Jose Bellido", "BBVA", "373185782581613", 859, 12, 2019, IllegalArgumentException.class
			}, {
				/* - 6) Un customer se suscribe a un periodico publico */
				"customer2", "newspaper1", "Jose Bellido", "BBVA", "373185782581613", 859, 12, 2019, IllegalArgumentException.class
			}, {
				/* - 7) Un customer se suscribe a un periodico ya suscrito */
				"customer1", "newspaper2", "Jose Bellido", "BBVA", "373185782581613", 859, 12, 2019, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			Newspaper newspaper = null;
			if (testingData[i][1] != null)
				newspaper = this.newspaperService.findOne(this.getEntityId((String) testingData[i][1]));

			this.startTransaction();
			//System.out.println("test " + i);
			this.templateListSuscribeNewspaper((String) testingData[i][0], newspaper, (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6], (int) testingData[i][7],
				(Class<?>) testingData[i][8]);
			//System.out.println("test " + i + " ok");
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	protected void templateListSuscribeNewspaper(final String username, final Newspaper newspaper, final String holderName, final String brandName, final String number, final int CVV, final int month, final int year, final Class<?> expected) {
		Class<?> caught = null;
		/* 1. Loggearse como Customer */

		this.authenticate(username);

		try {
			/* 2. Listar todos los periodicos */
			final Collection<Newspaper> newspapers = this.newspaperService.findAllPublished();
			Assert.isTrue(newspapers.contains(newspaper));

			/* 3. Seleccionar uno -> entra por parametros */
			/* 4. Suscribirse */
			final Subscription subscription = this.subscriptionService.create(newspaper);
			subscription.setCreditCard(new CreditCard());

			subscription.getCreditCard().setHolderName(holderName);
			subscription.getCreditCard().setBrandName(brandName);
			subscription.getCreditCard().setNumber(number);
			subscription.getCreditCard().setCVV(CVV);
			subscription.getCreditCard().setMonth(month);
			subscription.getCreditCard().setYear(year);

			this.subscriptionService.save(subscription);
			this.subscriptionService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

}
