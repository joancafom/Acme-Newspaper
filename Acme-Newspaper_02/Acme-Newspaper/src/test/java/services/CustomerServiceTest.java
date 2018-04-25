
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CustomerServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private CustomerService	customerService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;


	/*
	 * v1.0 - josembell
	 * [UC-018] - Register as a Customer
	 * 
	 * REQ: 19, 20, 21.1
	 */
	@Test
	public void driverRegisterCustomer() {
		final Object testingData[][] = {
			{
				/* + 1) Un usuario no identificado se registra en el sistema */
				null, "josembell", "test1234", "Jose", "Bellido", "C/ Carrion 127", "+34633017787", "josembell97@gmail.com", null
			}, {
				/* - 2) Un usuario se registra sin nombre */
				null, "josembell", "test1234", "", "Bellido", "C/ Carrion 127", "+34633017787", "josembell97@gmail.com", ConstraintViolationException.class
			}, {
				/* - 3) Un usuario se registra sin apellidos */
				null, "josembell", "test1234", "Jose", "", "C/ Carrion 127", "+34633017787", "josembell97@gmail.com", ConstraintViolationException.class
			}, {
				/* - 4) Un usuario se registra sin email */
				null, "josembell", "test1234", "Jose", "Bellido", "C/ Carrion 127", "+34633017787", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();
			//System.out.println("test " + i);
			this.templateRegisterListAndDisplayUsers((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
			//System.out.println("test " + i + " ok");
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	protected void templateRegisterListAndDisplayUsers(final String username, final String nickname, final String password, final String name, final String surnames, final String address, final String phone, final String email, final Class<?> expected) {
		Class<?> caught = null;
		try {
			/* 1. Registrarse en el sistema */
			final Customer customer = this.customerService.create();
			customer.getUserAccount().setUsername(nickname);
			customer.getUserAccount().setPassword(password);
			customer.setName(name);
			customer.setSurnames(surnames);
			customer.setPostalAddress(address);
			customer.setEmail(email);
			customer.setPhoneNumber(phone);

			this.customerService.save(customer);

			/* 2. Loggearse en el sistema */
			this.authenticate(nickname);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();
	}
}
