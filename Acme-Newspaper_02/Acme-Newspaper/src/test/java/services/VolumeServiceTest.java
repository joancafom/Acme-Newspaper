
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

import utilities.AbstractTest;
import domain.Newspaper;
import domain.Volume;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class VolumeServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private VolumeService		volumeService;

	//Fixtures ---------------------------------------

	@Autowired
	private NewspaperService	newspaperService;

	@PersistenceContext
	private EntityManager		entityManager;


	// -------------------------------------------------------------------------------
	// [UC-031] Display a Volume and add a Newspaper
	// 
	// Related Requirements:
	//   · REQ 10.1: Create a volume with as many published newspapers as he or she
	//               wishes. Note that the newspapers in a volume can be added or
	//               removed at any time. The same newspaper may be used to create
	//               different volumes.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverDisplayAndAddNewspaper() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> newspaper to add.
		// testingData[i][2] -> volume to edit.
		// testingData[i][3] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A User successfully adds a Newspaper to a Volume.
				"user5", "newspaper7", "volume2", null
			}, {
				// 2 - (-) A User adds a Newspaper he hasn't written to a Volume.
				"user5", "newspaper1", "volume2", IllegalArgumentException.class
			}, {
				// 3 - (-) A User adds a Newspaper to a Volume that isn't hers.
				"user5", "newspaper7", "volume1", IllegalArgumentException.class
			}, {
				// 4 - (-) A User adds a Newspaper that already belongs to the Volume.
				"user5", "newspaper5", "volume2", IllegalArgumentException.class
			}, {
				// 5 - (-) An Agent adds a Newspaper to a Volume.
				"agent1", "newspaper7", "volume2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			final Newspaper newspaper = this.newspaperService.findOne(super.getEntityId((String) testingData[i][1]));
			final Volume volume = this.volumeService.findOne(super.getEntityId((String) testingData[i][2]));

			this.startTransaction();

			this.templateDisplayAndAddNewspaper((String) testingData[i][0], newspaper, volume, (Class<?>) testingData[i][3]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateDisplayAndAddNewspaper(final String username, final Newspaper newspaper, final Volume volume, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List the Newspapers of a Volume

			final Collection<Newspaper> oldNewspapers = volume.getNewspapers();
			Assert.isTrue(!oldNewspapers.contains(newspaper));

			// 3. Add a new Newspaper to that Volume

			this.volumeService.addNewspaper(volume, newspaper);

			// Flush

			this.volumeService.flush();

			// 4. List the Newspapers again

			final Collection<Newspaper> newNewspapers = volume.getNewspapers();
			Assert.isTrue(newNewspapers.contains(newspaper));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-031] Display a Volume and remove a Newspaper
	// 
	// Related Requirements:
	//   · REQ 10.1: Create a volume with as many published newspapers as he or she
	//               wishes. Note that the newspapers in a volume can be added or
	//               removed at any time. The same newspaper may be used to create
	//               different volumes.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverDisplayAndRemoveNewspaper() {

		// testingData[i][0] -> username of the logged actor.
		// testingData[i][1] -> newspaper to remove.
		// testingData[i][2] -> volume to edit.
		// testingData[i][3] -> thrown exception.

		final Object testingData[][] = {
			{
				// 1 - (+) A User successfully removes a Newspaper from a Volume.
				"user5", "newspaper5", "volume2", null
			}, {
				// 2 - (-) A User removes a Newspaper from a Volume that isn't hers.
				"user5", "newspaper1", "volume1", IllegalArgumentException.class
			}, {
				// 3 - (-) A User removes a Newspaper that doesn't belongs to the Volume.
				"user5", "newspaper7", "volume2", IllegalArgumentException.class
			}, {
				// 4 - (-) A User removes a null Newspaper from the Volume.
				"user5", null, "volume2", IllegalArgumentException.class
			}, {
				// 5 - (-) An Agent removes a Newspaper from a Volume.
				"agent1", "newspaper1", "volume1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Newspaper newspaper = null;

			if (testingData[i][1] != null)
				newspaper = this.newspaperService.findOne(super.getEntityId((String) testingData[i][1]));

			final Volume volume = this.volumeService.findOne(super.getEntityId((String) testingData[i][2]));

			this.startTransaction();

			this.templateDisplayAndRemoveNewspaper((String) testingData[i][0], newspaper, volume, (Class<?>) testingData[i][3]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateDisplayAndRemoveNewspaper(final String username, final Newspaper newspaper, final Volume volume, final Class<?> expected) {
		Class<?> caught = null;

		// 1. Log in to the system
		super.authenticate(username);

		try {

			// 2. List the Newspapers of a Volume

			final Collection<Newspaper> oldNewspapers = volume.getNewspapers();
			Assert.isTrue(oldNewspapers.contains(newspaper));

			// 3. Remove a new Newspaper from that Volume

			this.volumeService.removeNewspaper(volume, newspaper);

			// Flush

			this.volumeService.flush();

			// 4. List the Newspapers again

			final Collection<Newspaper> newNewspapers = volume.getNewspapers();
			Assert.isTrue(!newNewspapers.contains(newspaper));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);
	}

	/*
	 * v1.0 - josembell
	 * [UC-029] - List and Create Volumes
	 * REQ: 8.1, 10.1
	 */
	@Test
	public void driverListAndCreateVolumes() {
		final Object testingData[][] = {
			{
				/* + 1) Un user crea un volumen de forma correcta */
				"user1", "Test", "This is a test", 2017, null
			}, {
				/* - 2) Un usuario no identificado crea un volumen */
				null, "Test", "This is a test", 2017, IllegalArgumentException.class
			}, {
				/* - 3) Un customer crea un volumen */
				"customer1", "Test", "This is a test", 2017, IllegalArgumentException.class
			}, {
				/* - 4) Un agent crea un volumen */
				"agent1", "Test", "This is a test", 2017, IllegalArgumentException.class
			}, {
				/* - 5) Un administrador crea un volumen */
				"admin", "Test", "This is a test", 2017, IllegalArgumentException.class
			}, {
				/* - 6) Un user crea un volumen sin titulo */
				"user1", null, "This is a test", 2017, ConstraintViolationException.class
			}, {
				/* - 7) Un user crea un volumen sin descripción */
				"user1", "Test", null, 2017, ConstraintViolationException.class
			}, {
				/* - 8) Un user crea un volumen con fecha errónea 1 */
				"user1", "Test", "This is a test", 1800, ConstraintViolationException.class
			}, {
				/* - 9) Un user crea un volumen con un año en el futuro */
				"user1", "Test", "This is a test", 2020, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			//System.out.println("test " + i);
			this.templateListAndCreateVolumes((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);
			//System.out.println("test " + i + " ok");

			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	protected void templateListAndCreateVolumes(final String username, final String title, final String description, final int year, final Class<?> expected) {

		Class<?> caught = null;

		/* 1. Log in to the system */
		super.authenticate(username);

		try {

			/* 2. List Volumes */
			final Collection<Volume> volumes1 = this.volumeService.findAll();
			final int numVolumes1 = volumes1.size();

			/* 3. Create a Volume */
			final Volume volume = this.volumeService.create();
			volume.setTitle(title);
			volume.setDescription(description);
			volume.setYear(year);

			/* -> Save */
			final Volume volumeSaved = this.volumeService.save(volume);
			this.volumeService.flush();

			/* 4. Check that there's a new volume */
			final Collection<Volume> volumes2 = this.volumeService.findAll();
			final int numVolumes2 = volumes2.size();

			Assert.isTrue(volumes2.contains(volumeSaved));
			Assert.isTrue(numVolumes1 + 1 == numVolumes2);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
