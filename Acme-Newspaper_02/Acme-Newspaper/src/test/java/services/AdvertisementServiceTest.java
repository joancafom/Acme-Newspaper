
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

import security.LoginService;
import utilities.AbstractTest;
import domain.Advertisement;
import domain.Agent;
import domain.CreditCard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdvertisementServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private AdvertisementService	advertisementService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager			entityManager;

	@Autowired
	private AgentService			agentService;


	/*
	 * v1.0 - josembell
	 * [UC-023] - List and Create Advertisements
	 * REQ: 2, 4.2
	 */
	@Test
	public void driverListAndCreateAdvertisements() {
		final Object testingData[][] = {
			{
				/* + 1) Un agent crea un advertisement de forma correcta */
				"agent1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 10, 2020, null
			}, {
				/* - 2) Un usuario no identificado crea un advertisement */
				null, "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 10, 2020, IllegalArgumentException.class
			}, {
				/* - 3) Un user crea un advertisement */
				"user1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 10, 2020, IllegalArgumentException.class
			}, {
				/* - 4) Un customer crea un advertisement */
				"customer1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 10, 2020, IllegalArgumentException.class
			}, {
				/* - 5) Un administrator crea un advertisement */
				"admin", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 10, 2020, IllegalArgumentException.class
			}, {
				/* - 6) Un agent crea un advertisement sin titulo */
				"agent1", null, "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 7) Un agent crea un advertisement sin banner URL */
				"agent1", "Test", null, "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 8) Un agent crea un advertisement con banner URL erróneo */
				"agent1", "Test", "fail", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 9) Un agent crea un advertisement sin target URL */
				"agent1", "Test", "http://www.test.com/test.jpg", null, "Angela", "BBVA", "4959375404376849", 654, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 10) Un agent crea un advertisement con target URL erróneo */
				"agent1", "Test", "http://www.test.com/test.jpg", "fail", "Angela", "BBVA", "4959375404376849", 654, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 11) Un agent crea un advertisement sin holder name */
				"agent1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", null, "BBVA", "4959375404376849", 654, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 12) Un agent crea un advertisement sin brand name */
				"agent1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", null, "4959375404376849", 654, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 13) Un agent crea un advertisement sin CCNumber */
				"agent1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", null, 654, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 14) Un agent crea un advertisement con CCNumber erróneo */
				"agent1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "fail", 654, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 15) Un agent crea un advertisement con CVV menor a 100 */
				"agent1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 99, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 16) Un agent crea un advertisement con CVV mayor a 999 */
				"agent1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 1000, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 17) Un agent crea un advertisement con month menor de 1 */
				"agent1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 0, 2020, ConstraintViolationException.class
			}, {
				/* - 18) Un agent crea un advertisement con month mayor a 12 */
				"agent1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 13, 2020, ConstraintViolationException.class
			}, {
				/* - 19) Un agent crea un advertisement con year menor a 2018 */
				"agent1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 10, 2017, IllegalArgumentException.class
			}, {
				/* - 20) Un agent crea un advertisement con fecha no valida */
				"agent1", "Test", "http://www.test.com/test.jpg", "http://www.test.com", "Angela", "BBVA", "4959375404376849", 654, 1, 2018, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			//System.out.println("test " + i);
			this.templateListAndCreateAdvertisements((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(int) testingData[i][7], (int) testingData[i][8], (int) testingData[i][9], (Class<?>) testingData[i][10]);
			//System.out.println("test " + i + " ok");

			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	protected void templateListAndCreateAdvertisements(final String username, final String title, final String bannerURL, final String targetURL, final String holderName, final String brandName, final String number, final int CVV, final int month,
		final int year, final Class<?> expected) {

		Class<?> caught = null;

		/* 1. Log in to the system */
		super.authenticate(username);

		try {

			/* 2. List my Advertisements */
			Collection<Advertisement> advertisements1 = new HashSet<Advertisement>();
			int numAdvertisements1 = 0;
			Agent agent = null;
			if (username != null) {
				agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
				if (agent != null) {
					advertisements1 = agent.getAdvertisements();
					numAdvertisements1 = advertisements1.size();
				}
			}

			/* 3. Create a Advertisement */
			final Advertisement advertisement = this.advertisementService.create();
			advertisement.setBannerURL(bannerURL);
			advertisement.setTargetURL(targetURL);
			advertisement.setTitle(title);
			final CreditCard creditCard = new CreditCard();
			creditCard.setHolderName(holderName);
			creditCard.setBrandName(brandName);
			creditCard.setNumber(number);
			creditCard.setCVV(CVV);
			creditCard.setMonth(month);
			creditCard.setYear(year);
			advertisement.setCreditCard(creditCard);

			/* -> Save */
			final Advertisement adSaved = this.advertisementService.save(advertisement);
			this.advertisementService.flush();

			/* 4. Check that there's a new advertisement */
			final Collection<Advertisement> advertisements2 = agent.getAdvertisements();
			final int numAdvertisements2 = advertisements2.size();

			Assert.isTrue(advertisements2.contains(adSaved));
			Assert.isTrue(numAdvertisements1 + 1 == numAdvertisements2);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
