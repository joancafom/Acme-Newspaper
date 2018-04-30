
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
import domain.Newspaper;

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

	@Autowired
	private NewspaperService		newspaperService;


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

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-024: List Not Advertised Newspapers, Advertise and List Advertised Newspapers
	 * 1. Log in to the System as an Agent
	 * 2. List her/his not Advertised Newspapers
	 * 3. Select a Newspaper from the list and associate to it one valid Advertisement of her/his own
	 * 4. List her/his Advertised Newspapers, including the new one
	 * 
	 * 
	 * Involved REQs: 4.2, 4.3, 4.4
	 * 
	 * Test Cases (5; 1+ 4-):
	 * 
	 * + 1) An agent list her/his not Advertised Newspapers, selects one and successfully advertises it by providing an advertisement of hers/his.
	 * Then, he lists her/his Advertised Newspapers
	 * 
	 * - 2) An agent list her/his not Advertised Newspapers, selects one and tries to advertise it by providing an advertisement of hers/his that's already been used.
	 * (the same advertisement cannot be used twice for the same Newspaper)
	 * 
	 * - 3) An agent list her/his not Advertised Newspapers, selects one and tries to advertise it by providing an advertisement of another agent.
	 * (only advertisements created by the agent can be used to advertise)
	 * 
	 * - 4) An agent list her/his not Advertised Newspapers, selects one and tries to advertise it by providing a null advertisement.
	 * (null advertisement)
	 * 
	 * - 5) An agent list her/his not Advertised Newspapers, provides a null newspaper and tries advertises it by providing an advertisement of hers/his.
	 * (null newspaper)
	 */
	@Test
	public void driverListAdvertiseAndListNot() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the beanName of the chosen newspaper to advertise.
		// testingData[i][2] -> the beanName of the chosen advertisement to use.
		// testingData[i][3] -> the expected exception.

		final Object testingData[][] = {
			{
				"agent1", "newspaper4", "advertisement1", null
			}, {

				"agent1", "newspaper1", "advertisement1", IllegalArgumentException.class
			}, {

				"agent1", "newspaper4", "advertisement5", IllegalArgumentException.class
			}, {

				"agent1", "newspaper4", null, IllegalArgumentException.class
			}, {

				"agent1", null, "advertisement1", IllegalArgumentException.class
			}
		};

		Newspaper newspaper = null;
		Advertisement ad = null;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][1] != null)
				newspaper = this.newspaperService.findOne(this.getEntityId((String) testingData[i][1]));
			else
				newspaper = null;

			if (testingData[i][2] != null)
				ad = this.advertisementService.findOne(this.getEntityId((String) testingData[i][2]));
			else
				ad = null;

			this.startTransaction();

			this.templateListAdvertiseAndListNot((String) testingData[i][0], newspaper, ad, (Class<?>) testingData[i][3]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	//v1.0 - Implemented by JA
	protected void templateListAdvertiseAndListNot(final String performer, final Newspaper newspaperToAdvertise, final Advertisement ad, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the System as an Agent
		this.authenticate(performer);

		final Agent agent = this.agentService.findOne(this.getEntityId(performer));

		try {

			// 2. List her/his not Advertised Newspapers
			final Collection<Newspaper> notAdvertisedNewspapersPrevious = this.newspaperService.getNotAdvertised(agent);

			final Collection<Newspaper> advertisedNewspapersPrevious = this.newspaperService.getAdvertised(agent);

			// 3. Select a Newspaper from the list and associate to it one valid Advertisement of her/his own (provided ad by parameters)

			this.advertisementService.advertise(newspaperToAdvertise, ad);

			this.newspaperService.flush();
			this.advertisementService.flush();

			//4. List her/his Advertised Newspapers, including the new one
			final Collection<Newspaper> advertisedNewspapersAfter = this.newspaperService.getAdvertised(agent);

			final Collection<Newspaper> notAdvertisedNewspapersAfter = this.newspaperService.getNotAdvertised(agent);

			Assert.isTrue(advertisedNewspapersAfter.contains(newspaperToAdvertise));
			Assert.isTrue(!notAdvertisedNewspapersAfter.contains(newspaperToAdvertise));
			Assert.isTrue(notAdvertisedNewspapersAfter.size() == notAdvertisedNewspapersPrevious.size() - 1);
			Assert.isTrue(advertisedNewspapersAfter.size() == advertisedNewspapersPrevious.size() + 1);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-027: Remove Advertisements
	 * 1. Log in to the System as an Administrator
	 * 2. List Advertisements
	 * 3. Select an Advertisement and remove it
	 * 4. List Advertisements, that now does not contain the removed one
	 * 
	 * 
	 * Involved REQs: 5.2
	 * 
	 * Test Cases (5; 3+ 2-):
	 * 
	 * + 1) An Admin logs in to the system, lists the Advertisements in the system and successfully provides one from the list to remove. Then, he lists the
	 * Advertisements again and sees that it was removed correctly. (advert with no Newspapers)
	 * 
	 * - 2) An Agent logs in to the system and tries to remove an Advertisement from another rival Agent (only Admins can remove them)
	 * 
	 * - 3) An Admin logs in to the system, lists the Advertisements in the system and tries to remove a null Advertisement
	 * 
	 * + 4) An Admin logs in to the system, lists the Advertisements in the system and successfully provides one from the list to remove. Then, he lists the
	 * Advertisements again and sees that it was removed correctly. (advert with one Newspapers)
	 * 
	 * + 5) An Admin logs in to the system, lists the Advertisements in the system and successfully provides one from the list to remove. Then, he lists the
	 * Advertisements again and sees that it was removed correctly. (advert with multiple Newspapers)
	 */
	@Test
	public void driverRemoveNewspaper() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the beanName of the chosen advertisement to remove.
		// testingData[i][2] -> whether we want to remove all its newspapers before the test or not.
		// testingData[i][3] -> the expected exception.

		final Object testingData[][] = {
			{
				"admin", "advertisement5", true, null
			}, {

				"agent1", "advertisement5", false, IllegalArgumentException.class
			}, {

				"admin", null, false, IllegalArgumentException.class
			}, {

				"admin", "advertisement4", false, null
			}, {

				"admin", "advertisement2", false, null
			}
		};

		Advertisement ad = null;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][1] != null)
				ad = this.advertisementService.findOne(this.getEntityId((String) testingData[i][1]));
			else
				ad = null;

			if ((Boolean) testingData[i][2]) {
				this.authenticate(ad.getAgent().getUserAccount().getUsername());

				for (final Newspaper n : ad.getNewspapers()) {
					n.getAdvertisements().remove(n);
					this.newspaperService.save(n);
				}

				this.unauthenticate();
			}

			this.startTransaction();
			//System.out.println("test " + i);
			this.templateRemoveNewspaper((String) testingData[i][0], ad, (Class<?>) testingData[i][3]);
			//System.out.println("test " + i + " ok");
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	//v1.0 - Implemented by JA
	protected void templateRemoveNewspaper(final String performer, final Advertisement ad, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the System as an Administrator
		this.authenticate(performer);

		try {

			// 2. List Advertisements
			final Collection<Advertisement> advetisementsBefore = this.advertisementService.findAll();

			// 3. Select an Advertisement and remove it

			this.advertisementService.delete(ad);

			this.newspaperService.flush();
			this.advertisementService.flush();

			//4. List Advertisements, that now does not contain the removed one
			final Collection<Advertisement> advetisementsAfter = this.advertisementService.findAll();

			Assert.isTrue(!advetisementsAfter.contains(ad));
			Assert.isTrue(advetisementsAfter.size() == advetisementsBefore.size() - 1);

			for (final Newspaper n : ad.getNewspapers())
				Assert.isTrue(!n.getAdvertisements().contains(ad));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}
}
