
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import utilities.AbstractTest;
import domain.CreditCard;
import domain.Customer;
import domain.Volume;
import domain.VolumeSubscription;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class VolumeSubscriptionServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private VolumeSubscriptionService	volumeSubscriptionService;

	//Fixtures ---------------------------------------

	@Autowired
	private VolumeService				volumeService;

	@Autowired
	private CustomerService				customerService;

	@PersistenceContext
	private EntityManager				entityManager;


	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-030: List(*) and Subscribe Volumes
	 * 1. Log in to the System as a Customer
	 * 2. List the Volumes in the system
	 * 3. Select a Volume which the Customer is not already subscribed to.
	 * 4. Subscribe to the Volume by providing a valid CreditCard
	 * 5. Display one of its newspapers, which she or he has just subscribed to.
	 * 
	 * 
	 * Involved REQs: 9.1
	 * 
	 * Test Cases (5; 2+ 3-):
	 * 
	 * + 1) A Customer logs in to the system, list all the Volumes and subscribes to a Volume he/she is not already by providing a valid creditCard. Then he/she
	 * displays one the newspapers of the recently subscribed Volume
	 * 
	 * + 2) A Customer logs in to the system, list all the Volumes and subscribes to a Volume he/she is not already by providing a valid creditCard. (Volume with empty newspapers)
	 * 
	 * - 3) A Customer logs in to the system, list all the Volumes and tries to subscribe to a Volume he/she is already subscribed to by providing a valid creditCard. (cannot subscribe twice)
	 * 
	 * - 4) A Customer logs in to the system, list all the Volumes and subscribes to a Volume he/she is not already by providing an invalid creditCard (expired)
	 * 
	 * - 5) A Customer logs in to the system, list all the Volumes and tries to subscribe another Customer to a Volume he/she is not already by providing an valid creditCard (you can not subscribe another customer other than yourself)
	 */

	@Test
	public void driverSubscribeVolumes() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the holderName of the creditCard.
		// testingData[i][2] -> the brandName of the creditCard.
		// testingData[i][3] -> the number of the creditCard.
		// testingData[i][4] -> the CVV of the creditCard.
		// testingData[i][5] -> the month of the creditCard.
		// testingData[i][6] -> the year of the creditCard.
		// testingData[i][7] -> the customer for the subscription.
		// testingData[i][8] -> the volume to subscribe to.
		// testingData[i][9] -> the expected exception.

		final LocalDate futureDate = new LocalDate();

		final Object testingData[][] = {
			{
				"customer3", "Cristina Cifuentes", "MasterCard URJC 404", "4997894241220386", 123, 12, futureDate.getYear() + 1, "customer3", "volume1", null
			}, {
				"customer3", "Cristina Cifuentes", "MasterCard URJC 404", "4997894241220386", 123, 12, futureDate.getYear() + 1, "customer3", "volume5", null
			}, {
				"customer1", "Cristina Cifuentes", "MasterCard URJC 404", "4997894241220386", 123, 12, futureDate.getYear() + 1, "customer1", "volume1", IllegalArgumentException.class
			}, {
				"customer3", "Cristina Cifuentes", "MasterCard URJC 404", "4997894241220386", 123, 12, futureDate.getYear() - 1, "customer3", "volume1", IllegalArgumentException.class
			}, {
				"customer3", "Cristina Cifuentes", "MasterCard URJC 404", "4997894241220386", 123, 12, futureDate.getYear() + 1, "customer1", "volume1", IllegalArgumentException.class
			}
		};

		final CreditCard creditCard = new CreditCard();
		Volume volume = null;
		Customer subscriber = null;

		for (int i = 0; i < testingData.length; i++) {

			creditCard.setHolderName((String) testingData[i][1]);
			creditCard.setBrandName((String) testingData[i][2]);
			creditCard.setNumber((String) testingData[i][3]);
			creditCard.setCVV((Integer) testingData[i][4]);
			creditCard.setMonth((Integer) testingData[i][5]);
			creditCard.setYear((Integer) testingData[i][6]);

			if (testingData[i][8] != null)
				volume = this.volumeService.findOne(this.getEntityId((String) testingData[i][8]));

			if (testingData[i][7] != null)
				subscriber = this.customerService.findOne(this.getEntityId((String) testingData[i][7]));

			this.startTransaction();

			this.templateSubscribeVolumes((String) testingData[i][0], volume, creditCard, subscriber, (Class<?>) testingData[i][9]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}
	//v1.0 - Implemented by JA
	protected void templateSubscribeVolumes(final String performer, final Volume volumeToSubscribe, final CreditCard creditCard, final Customer subscriber, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the System as a Customer
		this.authenticate(performer);

		try {

			//2. List the Volumes in the system
			final Collection<Volume> volumesBefore = this.volumeService.findAll();

			//3. Select a Volume which the Customer is not already subscribed to. (provided by params)

			//4. Subscribe to the Volume by providing a valid CreditCard
			final VolumeSubscription subscription = this.volumeSubscriptionService.create(volumeToSubscribe);
			subscription.setCreditCard(creditCard);
			subscription.setSubscriber(subscriber);

			this.volumeSubscriptionService.save(subscription);
			this.volumeSubscriptionService.flush();

			//5. Display one of its newspapers, which she or he has just subscribed to.

			final Customer currentCostumer = this.customerService.findByUserAccount(LoginService.getPrincipal());

			if (LoginService.getPrincipal().equals(subscriber.getUserAccount()))
				//In order to display a newspaper, she or he has to be subscribed... So we check that
				Assert.isTrue(this.volumeSubscriptionService.hasVolumeSubscriptionVolume(currentCostumer, volumeToSubscribe));

			final Collection<Volume> volumesAfter = this.volumeService.findAll();

			Assert.isTrue(volumesBefore.containsAll(volumesAfter));
			Assert.isTrue(volumesAfter.containsAll(volumesBefore));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}
}
