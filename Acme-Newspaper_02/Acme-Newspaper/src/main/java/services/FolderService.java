
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FolderRepository;
import security.LoginService;
import security.UserAccount;
import domain.ANMessage;
import domain.Actor;
import domain.Folder;

@Service
@Transactional
public class FolderService {

	// Managed Repository ----------------------------------------------------

	@Autowired
	private FolderRepository	folderRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;

	@Autowired
	private ANMessageService	anMessageService;


	// Supporting Services ---------------------------------------------------

	// Validator -------------------------------------------------------------

	// CRUD Methods ----------------------------------------------------------

	// v1.0 - Implemented by Alicia
	public Folder create(final Actor actor, final Folder parentFolder) {
		Assert.notNull(actor);

		final List<ANMessage> messages = new ArrayList<ANMessage>();
		final List<Folder> childFolders = new ArrayList<Folder>();

		final Folder folder = new Folder();

		folder.setIsSystem(false);
		folder.setAnMessages(messages);
		folder.setChildFolders(childFolders);
		folder.setActor(actor);

		if (parentFolder != null) {
			folder.setParentFolder(parentFolder);
			parentFolder.getChildFolders().add(folder);
		}
		return folder;
	}

	/* v1.0 - josembell */
	public Collection<Folder> findAll() {
		final Collection<Folder> folders;

		Assert.notNull(this.folderRepository);
		folders = this.folderRepository.findAll();
		Assert.notNull(folders);

		return folders;
	}
	/* v1.0 - josembell */
	public Folder findOne(final int folderId) {
		return this.folderRepository.findOne(folderId);
	}

	/* v1.0 - josembell */
	// v2.0 - Updated by JA (parent folder)
	public Folder save(final Folder folder) {
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(actor);

		Assert.notNull(folder);
		Assert.isTrue(folder.getActor().equals(actor));

		Assert.isTrue(!folder.getIsSystem());
		Assert.isTrue(folder.getActor().getUserAccount().equals(LoginService.getPrincipal()));
		final String folderName = folder.getName().toLowerCase();
		Assert.isTrue(!folderName.equals("in box"));
		Assert.isTrue(!folderName.equals("out box"));
		Assert.isTrue(!folderName.equals("notification box"));
		Assert.isTrue(!folderName.equals("trash box"));
		Assert.isTrue(!folderName.equals("spam box"));

		if (folder.getParentFolder() != null) {
			Assert.isTrue(folder.getParentFolder().getIsSystem() == false);
			Assert.isTrue(!folder.getParentFolder().equals(folder));
			Assert.isTrue(folder.getParentFolder().getActor().equals(actor));
			Assert.isTrue(this.isHierarchyCompatible(folder, folder.getParentFolder()));
		}

		/*
		 * A folder cannot have the same name as another folder of the same
		 * actor
		 */
		if (folder.getParentFolder() == null)
			for (final Folder f : this.findAllParentFoldersByPrincipal()) {
				if (!f.equals(folder))
					Assert.isTrue(!f.getName().equals(folder.getName()));
			}
		else
			for (final Folder f : folder.getParentFolder().getChildFolders())
				if (!f.equals(folder))
					Assert.isTrue(!f.getName().equals(folder.getName()));

		return this.folderRepository.save(folder);
	}

	/* v1.0 - josembell */
	// v2.0 - Modified by JA
	public void deleteByPrincipal(final Folder folder) {
		Assert.notNull(folder);
		Assert.isTrue(!folder.getIsSystem());

		final UserAccount us = LoginService.getPrincipal();
		final Actor actor = this.actorService.findByUserAccount(us);
		Assert.notNull(actor);
		Assert.isTrue(folder.getActor().equals(actor));

		this.deleteHierarchy(folder);
	}

	// v1.0 - Implemented by JA
	private void deleteHierarchy(final Folder folder) {

		/*
		 * Recursively delete all the hierarchy of one folder
		 */

		Assert.notNull(folder);

		for (final ANMessage m : folder.getAnMessages())
			this.anMessageService.delete(m);

		final Collection<Folder> childrenCopy = new HashSet<Folder>(folder.getChildFolders());
		for (final Folder childFolder : childrenCopy)
			this.deleteHierarchy(childFolder);

		if (folder.getParentFolder() != null)
			folder.getParentFolder().getChildFolders().remove(folder);

		this.folderRepository.delete(folder);

	}
	//Other Business Methods -------------------------------------------------

	// v1.0 - Implemented by Alicia
	public void createSystemFolders(final Actor actor) {
		final String[] sysFolderNames = {
			"In Box", "Out Box", "Notification Box", "Trash Box", "Spam Box"
		};

		for (final String s : sysFolderNames) {
			final Folder f = this.create(actor, null);
			f.setIsSystem(true);
			f.setName(s);
			f.setActor(actor);
			this.folderRepository.save(f);
		}
	}

	/* v1.0 - josembell */
	public Collection<Folder> findAllByPrincipal() {
		final Collection<Folder> folders;

		final UserAccount userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.findByUserAccount(userAccount);

		Assert.notNull(this.folderRepository);
		folders = this.folderRepository.findAllByActorId(actor.getId());
		Assert.notNull(folders);

		return folders;
	}

	/* v1.0 - josembell */
	public Folder findOneByPrincipal(final int folderId) {
		Folder folder;

		folder = this.folderRepository.findOne(folderId);

		Assert.isTrue(folder.getActor().getUserAccount().equals(LoginService.getPrincipal()));

		return folder;
	}

	/* v1.0 - josembell */
	public Folder findByActorAndName(final Actor actor, final String name) {
		Folder folder;

		Assert.notNull(actor);
		Assert.notNull(name);

		folder = this.folderRepository.findByActorIdAndName(actor.getId(), name);

		return folder;
	}

	/* v1.0 - josembell */
	public Collection<Folder> findAllParentFoldersByPrincipal() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Folder> folders = this.findAllByPrincipal();
		final Collection<Folder> res = new HashSet<Folder>();
		for (final Folder f : folders)
			if (f.getParentFolder() == null)
				res.add(f);

		return res;

	}

	//v1.0 - Implemented by JA
	private Boolean isHierarchyCompatible(final Folder folderToMove, final Folder destiny) {

		/*
		 * Private method only called within the code, so no need to check
		 * if the parameters are valid or not
		 * 
		 * Checks that the destiny folder is not in the same branch as the
		 * folderToMove
		 */

		if (destiny.getParentFolder() == null)
			return true;
		else if (destiny.getParentFolder().equals(folderToMove))
			return false;
		else
			return this.isHierarchyCompatible(folderToMove, destiny.getParentFolder());

	}

	/* v1.0 - josembell */
	public Folder reconstruct(final Folder prunedFolder, final BindingResult binding) {
		Assert.notNull(prunedFolder);
		//final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());

		if (prunedFolder.getId() == 0) {
			prunedFolder.setChildFolders(new HashSet<Folder>());
			prunedFolder.setActor(this.actorService.findByUserAccount(LoginService.getPrincipal()));
			prunedFolder.setAnMessages(new HashSet<ANMessage>());
			prunedFolder.setIsSystem(false);

		} else {
			final Folder old = this.findOne(prunedFolder.getId());
			prunedFolder.setChildFolders(old.getChildFolders());
			prunedFolder.setAnMessages(old.getAnMessages());
			prunedFolder.setIsSystem(old.getIsSystem());
			prunedFolder.setActor(old.getActor());

		}

		this.validator.validate(prunedFolder, binding);
		return prunedFolder;
	}

	/* v1.0 - josembell */
	public Collection<Folder> findAllNotSystemByPrincipal() {
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(actor);
		return this.folderRepository.findAllNotSystemByActorId(actor.getId());
	}

	// v1.0 - Implemented by Alicia
	public Collection<Folder> getAllExceptOneForPrincipal(final Folder folder) {
		Assert.notNull(folder);

		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(actor);

		final Collection<Folder> res = this.folderRepository.findAllExceptOneForActor(folder.getId(), actor.getId());

		return res;
	}

	/* v1.0 - josembell */
	public void flush() {
		this.folderRepository.flush();

	}

	public Folder saveSendMessage(final Folder folder) {
		Assert.notNull(folder);

		return this.folderRepository.save(folder);
	}

	/* v1.0 - josembell */
	public Page<Folder> findAllParentFoldersByPrincipal(final Integer page, final int size) {
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(actor);
		final Page<Folder> res = this.folderRepository.findAllParentFoldersByPrincipalPaged(actor.getId(), new PageRequest(page - 1, size));
		return res;
	}

	/* v1.0 - josembell */
	public Page<Folder> findChildFoldersOfFolderByPrincipal(final Integer page, final int size, final Folder folder) {
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(actor);
		return this.folderRepository.findChildFoldersOfFolderByPrincipalPaged(actor.getId(), folder.getId(), new PageRequest(page - 1, size));
	}

	//v1.0 - Implemented by JA
	public Collection<Folder> findCompatibleFoldersToMove(final Folder folderToMove) {

		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(actor);
		Assert.notNull(folderToMove);

		//Watch out for Transients!
		Assert.isTrue(folderToMove.getId() != 0);
		Assert.isTrue(folderToMove.getActor().equals(actor));
		Assert.isTrue(!folderToMove.getIsSystem());

		final Folder originalFolder = this.findOne(folderToMove.getId());

		final Collection<Folder> excludedFolders = this.getExludedRecursive(originalFolder);
		excludedFolders.add(originalFolder);

		return this.folderRepository.findCompatibleFoldersToMoveByExcludedAndActorId(excludedFolders, actor.getId());
	}

	//v1.0 - Implemented by JA
	private Set<Folder> getExludedRecursive(final Folder folder) {

		/*
		 * Compute the folders to exclude from the hierarchy
		 */

		Assert.notNull(folder);

		final Set<Folder> res = new HashSet<Folder>();

		if (!folder.getChildFolders().isEmpty()) {
			res.addAll(folder.getChildFolders());
			for (final Folder child : folder.getChildFolders())
				res.addAll(this.getExludedRecursive(child));
		}

		return res;

	}
}
