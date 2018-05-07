
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
import domain.Actor;
import domain.Folder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FolderServiceTest extends AbstractTest {

	// System Under Test -----------------------------

	@Autowired
	private FolderService	folderService;

	//Fixtures ---------------------------------------

	@PersistenceContext
	private EntityManager	entityManager;

	@Autowired
	private ActorService	actorService;


	/*
	 * v1.0 - josembell
	 * [UC-036] - Create and Edit Folder
	 * REQ: 12, 13.1
	 */
	@Test
	public void driverCreateAndEditFolder() {
		final Object testingData[][] = {
			{
				/* + 1) Un user crea un folder y lo edita con un nombre correcto */
				"customer3", null, "Test", "New-Test", null
			}, {
				/* + 2) Un user crea un child folder y lo edita con un nombre correcto */
				"customer3", "folder93", "Test", "New-Test", null
			}, {
				/* - 3) Un usuario no identificado crea un folder */
				null, null, "Test", "New-Test", IllegalArgumentException.class
			}, {
				/* - 4) Un usuario identificado crea un folder sin nombre */
				"customer3", null, "", "New-Test", ConstraintViolationException.class
			}, {
				/* - 5) Un usuario crea un folder con un nombre ya en el mismo nivel */
				"customer3", null, "Meetings", "New-Test", IllegalArgumentException.class
			}, {
				/* - 6) Un usuario crea un child folder con un nombre ya en el mismo nivel */
				"customer3", "folder93", "Year 2017", "New-Test", IllegalArgumentException.class
			}, {
				/* - 7) Un usuario edita un folder sin nombre */
				"customer3", null, "Test", "", ConstraintViolationException.class
			}, {
				/* - 8) Un usuario edita un folder con un nombre ya elegido */
				"customer3", null, "Test", "Meetings", IllegalArgumentException.class
			}, {
				/* - 9) Un usuario edita un child folder con un nombre ya elegido */
				"customer3", "folder93", "Test", "Year 2017", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			Folder folder = null;
			if (testingData[i][1] != null)
				folder = this.folderService.findOne(this.getEntityId((String) testingData[i][1]));

			this.startTransaction();

			//System.out.println("test " + i);
			this.templateCreateAndEditFolder((String) testingData[i][0], folder, (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			//System.out.println("test " + i + " ok");

			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	/* v1.0 - josembell */
	protected void templateCreateAndEditFolder(final String username, final Folder parentFolder, final String name, final String newName, final Class<?> expected) {

		Class<?> caught = null;

		/* 1. Log in to the system */
		super.authenticate(username);

		try {

			/* 2. List Folders */
			final Collection<Folder> folders1 = this.folderService.findAllByPrincipal();
			final int numFolders1 = folders1.size();

			/* 3. Create a folder */
			final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			final Folder folder = this.folderService.create(actor, parentFolder);
			folder.setName(name);

			/* -> Save */
			final Folder saved = this.folderService.save(folder);
			this.folderService.flush();

			/* 4. Change the name */
			saved.setName(newName);

			/* -> Save */
			this.folderService.save(saved);
			this.folderService.flush();

			/* 5. Check that there's one more folder in the list */
			final Collection<Folder> folders2 = this.folderService.findAllByPrincipal();
			final int numFolders2 = folders2.size();

			Assert.isTrue(numFolders1 + 1 == numFolders2);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-037: Organize Folders
	 * 1. Log in to the system
	 * 2. List all the folders of the current Actor
	 * 3. Select a non-system Folder of hers/his and nest it in another custom Folder of hers/his.
	 * 
	 * 
	 * Involved REQs: 12, 13.1
	 * 
	 * Test Cases (5; 2+ 3-):
	 * 
	 * + 1) An Actor logs in, lists her/his folders and selects a non-system Folder to nest in inside another custom Folder
	 * 
	 * - 2) An Actor logs in, lists her/his folders and selects a system Folder to nest in inside another custom Folder (only custom Folders may be organized)
	 * 
	 * - 3) An Actor logs in, lists her/his folders and selects a non-system Folder to nest in inside a system Folder (only nested in custom Folders)
	 * 
	 * - 4) An Actor logs in, lists her/his folders and selects a non-system Folder to nest in inside itself (makes no sense to nest inside itself)
	 * 
	 * - 5) An Actor logs in, lists her/his folders and selects a non-system Folder to nest in inside another actor's custom Folder
	 */

	@Test
	public void driverOrganizeFolders() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the folder to move
		// testingData[i][2] -> the folder in which to nest
		// testingData[i][3] -> the username of the owner of the folder in which to nest
		// testingData[i][4] -> the expected exception.

		final Object testingData[][] = {
			{
				"customer3", "December", "Meetings", "customer3", null
			}, {
				"customer3", "In Box", "Year 2017", "customer3", IllegalArgumentException.class
			}, {
				"customer3", "Meetings", "In Box", "customer3", IllegalArgumentException.class
			}, {
				"customer3", "Meetings", "Meetings", "customer3", IllegalArgumentException.class
			}, {
				"customer3", "Meetings", "Etc", "agent1", IllegalArgumentException.class
			}
		};

		Folder toMove = null;
		Folder inToNest = null;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][1] != null)
				toMove = this.folderService.findByActorAndName(this.actorService.findOne(this.getEntityId((String) testingData[i][0])), (String) testingData[i][1]);

			if (testingData[i][2] != null)
				inToNest = this.folderService.findByActorAndName(this.actorService.findOne(this.getEntityId((String) testingData[i][3])), (String) testingData[i][2]);

			this.startTransaction();

			this.templateOrganizeFolders((String) testingData[i][0], toMove, inToNest, (Class<?>) testingData[i][4]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}
	//v1.0 - Implemented by JA
	protected void templateOrganizeFolders(final String performer, final Folder toMove, final Folder inToNest, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Log in to the system
		this.authenticate(performer);

		try {

			//2. List all the final folders of the current Actor
			final Collection<Folder> folderBefore = this.folderService.findAllByPrincipal();

			//3. Select a non-system Folder final of hers/final his and nest final it in another final custom Folder of hers/his. (by params)
			toMove.setParentFolder(inToNest);
			this.folderService.save(toMove);

			this.folderService.flush();

			Assert.isTrue(this.folderService.findByActorAndName(this.actorService.findByUserAccount(LoginService.getPrincipal()), toMove.getName()).getParentFolder().equals(inToNest));
			Assert.isTrue(this.folderService.findByActorAndName(this.actorService.findByUserAccount(LoginService.getPrincipal()), inToNest.getName()).getChildFolders().contains(toMove));

			final Collection<Folder> folderAfter = this.folderService.findAllByPrincipal();

			Assert.isTrue(folderAfter.containsAll(folderBefore));
			Assert.isTrue(folderBefore.containsAll(folderAfter));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}
}
