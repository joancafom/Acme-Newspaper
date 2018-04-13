
package services;

import java.util.Collection;

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
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UserServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private UserService		userService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;


	// -------------------------------------------------------------------------------
	// [UC-010]Follow and list Users.
	// 
	// Related Requirements:
	//   · REQ 16.2: An actor who is authenticated as a user must be able to follow or
	//               unfollow another user.
	//   · REQ 16.3: An actor who is authenticated as a user must be able to list the
	//               users who he or she follows.
	//   · REQ 16.4: An actor who is authenticated as a user must be able to list the
	//               users who follow him or her.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverFollowAndListUsers() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> User to follow.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A User correctly follows another User.
				"user1", "user3", null
			}, {
				// 2 - (-) A User follows a User who she/he is already following.
				"user1", "user2", IllegalArgumentException.class
			}, {
				// 3 - (-) A User follows a null User.
				"user1", null, IllegalArgumentException.class
			}, {
				// 4 - (-) An unauthenticated actor follows a User.
				null, "user1", IllegalArgumentException.class
			}, {
				// 5 - (-) An Administrator follows a User.
				"admin", "user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			User userToFollow = null;

			if (testingData[i][1] != null)
				userToFollow = this.userService.findOne(super.getEntityId((String) testingData[i][1]));

			this.startTransaction();

			this.templateFollowAndListUsers((String) testingData[i][0], userToFollow, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateFollowAndListUsers(final String username, final User userToFollow, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List the Users in the system

			final Collection<User> usersList = this.userService.findAll();

			if (userToFollow != null)
				Assert.isTrue(usersList.contains(userToFollow));

			// 3. Follow the User

			this.userService.follow(userToFollow);

			// Flush
			this.userService.flush();

			// 4. List the Users that I'm following

			final Collection<User> following = this.userService.findByUserAccount(LoginService.getPrincipal()).getFollowees();
			Assert.isTrue(following.contains(this.userService.findOne(userToFollow.getId())));

			// 5. Check that the other User has me as a follower

			final Collection<User> followers = this.userService.findOne(userToFollow.getId()).getFollowers();
			Assert.isTrue(followers.contains(this.userService.findByUserAccount(LoginService.getPrincipal())));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	// -------------------------------------------------------------------------------
	// [UC-011]Unfollow and list Users.
	// 
	// Related Requirements:
	//   · REQ 16.2: An actor who is authenticated as a user must be able to follow or
	//               unfollow another user.
	//   · REQ 16.3: An actor who is authenticated as a user must be able to list the
	//               users who he or she follows.
	//   · REQ 16.4: An actor who is authenticated as a user must be able to list the
	//               users who follow him or her.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverUnfollowAndListUsers() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> User to unfollow.
		// testingData[i][2] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A User correctly unfollows another User.
				"user1", "user2", null
			}, {
				// 2 - (-) A User unfollows a User who she/he is not following.
				"user1", "user3", IllegalArgumentException.class
			}, {
				// 3 - (-) A User unfollows a null User.
				"user1", null, IllegalArgumentException.class
			}, {
				// 4 - (-) An unauthenticated actor unfollows a User.
				null, "user1", IllegalArgumentException.class
			}, {
				// 5 - (-) An Administrator unfollows a User.
				"admin", "user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			User userToUnfollow = null;

			if (testingData[i][1] != null)
				userToUnfollow = this.userService.findOne(super.getEntityId((String) testingData[i][1]));

			this.startTransaction();

			this.templateUnfollowAndListUsers((String) testingData[i][0], userToUnfollow, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateUnfollowAndListUsers(final String username, final User userToUnfollow, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List the Users in the system

			final Collection<User> usersList = this.userService.findAll();

			if (userToUnfollow != null)
				Assert.isTrue(usersList.contains(userToUnfollow));

			// 3. Unfollow the User

			this.userService.unfollow(userToUnfollow);

			// Flush
			this.userService.flush();

			// 4. List the Users that I'm following

			final Collection<User> following = this.userService.findByUserAccount(LoginService.getPrincipal()).getFollowees();
			Assert.isTrue(!following.contains(this.userService.findOne(userToUnfollow.getId())));

			// 5. Check that the other User doesn't have me as a follower

			final Collection<User> followers = this.userService.findOne(userToUnfollow.getId()).getFollowers();
			Assert.isTrue(!followers.contains(this.userService.findByUserAccount(LoginService.getPrincipal())));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	/*
	 * v1.0 - josembell
	 * [UC-001] - Register List and Display Users
	 * 1. Register to the system as an User
	 * 2. Log In to the System
	 * 3. List the Users in the system
	 * 4. Select one User to display
	 * 
	 * REQ: 1, 4.1, 4.3
	 */
	@Test
	public void driverRegisterListAndDisplayUsers() {
		final Object testingData[][] = {
			{
				/* + 1) Un usuario no identificado se registra en el sistema y displayea un user existente */
				null, "user1", "josembell", "test1234", "Jose", "Bellido", "C/ Carrion 127", "+34633017787", "josembell97@gmail.com", null
			}, {
				/* - 2) Un usuario se registra sin nombre */
				null, "user1", "josembell", "test1234", "", "Bellido", "C/ Carrion 127", "+34633017787", "josembell97@gmail.com", ConstraintViolationException.class
			}, {
				/* - 3) Un usuario se registra sin apellidos */
				null, "user1", "josembell", "test1234", "Jose", "", "C/ Carrion 127", "+34633017787", "josembell97@gmail.com", ConstraintViolationException.class
			}, {
				/* - 4) Un usuario se registra sin email */
				null, "user1", "josembell", "test1234", "Jose", "Bellido", "C/ Carrion 127", "+34633017787", "", ConstraintViolationException.class
			}, {
				/* - 5) Un usuario no identificado se registra en el sistema y displayea un usuario no existente */
				null, "fail", "josembell", "test1234", "Jose", "Bellido", "C/ Carrion 127", "+34633017787", "josembell97@gmail.com", IllegalArgumentException.class
			}, {
				/* - 6) Un usuario no identificado se registra en el sistema y displayea un user null */
				null, null, "josembell", "test1234", "Jose", "Bellido", "C/ Carrion 127", "+34633017787", "josembell97@gmail.com", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			User user = null;
			if (testingData[i][1] != null)
				try {
					user = this.userService.findOne(this.getEntityId((String) testingData[i][1]));
				} catch (final Throwable oops) {
					user = null;
				}

			this.startTransaction();
			//System.out.println("test " + i);
			this.templateRegisterListAndDisplayUsers((String) testingData[i][0], user, (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (Class<?>) testingData[i][9]);
			//System.out.println("test " + i + " ok");
			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0-josembell */
	protected void templateRegisterListAndDisplayUsers(final String username, final User userDisplay, final String nickname, final String password, final String name, final String surnames, final String address, final String phone, final String email,
		final Class<?> expected) {

		Class<?> caught = null;

		try {
			/* 1. Registrarse en el sistema */
			final User user = this.userService.create();
			user.getUserAccount().setUsername(nickname);
			user.getUserAccount().setPassword(password);
			user.setName(name);
			user.setSurnames(surnames);
			user.setPostalAddress(address);
			user.setEmail(email);
			user.setPhoneNumber(phone);

			this.userService.save(user);

			/* 2. Loggearse en el sistema */
			this.authenticate(nickname);

			/* 3. Listar los usuarios del sistema */
			final Collection<User> users = this.userService.findAll();

			/* 4. Displayear uno -> el que entra por parÃ¡metros */
			Assert.isTrue(users.contains(userDisplay));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}
}
