
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Agent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AgentServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private AgentService	agentService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;


	//Acme-Newspaper v2.0 -------------------------------------------------------------------------------------------

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-022: Register as an Agent
	 * 1. Register to the system as an Agent by providing correct info
	 * 2. Log in to the System as that Agent
	 * 
	 * Involved REQs: 3.1, 1 (implies actor's constraints)
	 * 
	 * Test Cases (6; 3+ 3-):
	 * 
	 * + 1) A unauthenticated Actor registers successfully to the system by providing correct information, and then logs in. (all fields)
	 * 
	 * + 2) A unauthenticated Actor registers successfully to the system by providing correct information, and then logs in. (no postal address and phoneNumber)
	 * 
	 * - 3) A unauthenticated Actor tries to register to the system by providing incorrect information (duplicated userName)
	 * 
	 * - 4) A unauthenticated Actor tries to register to the system by providing incorrect information (empty email)
	 * 
	 * - 5) A unauthenticated Actor tries to register to the system by providing incorrect information (phoneNumber not following pattern)
	 * 
	 * - 6) A User tries to register to the system providing correct information (only unregisters Actors can do so)
	 */
	@Test
	public void driverAgentRegister() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the name of the user.
		// testingData[i][2] -> the surnames of the user.
		// testingData[i][3] -> the address of the user.
		// testingData[i][4] -> the phoneNumber of the user.
		// testingData[i][5] -> the email of the user.
		// testingData[i][6] -> the the username/password of the user.
		// testingData[i][7] -> the expected exception.

		final Object testingData[][] = {
			{
				null, "Catherine", "Zeta Jones", "Avd Hollywood n. 534", "+34633017787", "catherinezeta@icloud.com", "cath-z89", null
			}, {

				null, "Catherine", "Zeta Jones", null, null, "catherinezeta@icloud.com", "cath-z89", null
			}, {

				null, "Catherine", "Zeta Jones", "Avd Hollywood n. 534", "+34633017787", "catherinezeta@icloud.com", "agent1", DataIntegrityViolationException.class
			}, {

				null, "Catherine", "Zeta Jones", "Avd Hollywood n. 534", "+34633017787", "", "cath-z89", ConstraintViolationException.class
			}, {

				null, "Catherine", "Zeta Jones", "Avd Hollywood n. 534", "633lk0200", "catherinezeta@icloud.com", "cath-z89", ConstraintViolationException.class
			}, {

				"user1", "Catherine", "Zeta Jones", "Avd Hollywood n. 534", "+34633017787", "catherinezeta@icloud.com", "cath-z89", RuntimeException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();
			System.out.println(i);
			this.templateAgentRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	//v1.0 - Implemented by JA
	protected void templateAgentRegister(final String performer, final String name, final String surnames, final String address, final String phoneNumber, final String email, final String userAndPass, final Class<?> expected) {

		Class<?> caught = null;

		this.authenticate(performer);

		try {
			// 1. Register to the system as an an Agent by providing correct info
			final Agent agentToRegister = this.agentService.create();

			agentToRegister.getUserAccount().setUsername(userAndPass);
			agentToRegister.getUserAccount().setPassword(userAndPass);
			agentToRegister.setName(name);
			agentToRegister.setSurnames(surnames);
			agentToRegister.setPostalAddress(address);
			agentToRegister.setPhoneNumber(phoneNumber);
			agentToRegister.setEmail(email);

			this.agentService.save(agentToRegister);
			this.agentService.flush();

			// 2. Log in to the System as that Agent
			this.authenticate(userAndPass);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}
}
