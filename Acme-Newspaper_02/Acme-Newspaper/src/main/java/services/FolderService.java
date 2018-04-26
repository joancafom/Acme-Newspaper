
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FolderRepository;
import domain.ANMessage;
import domain.Actor;
import domain.Folder;

@Service
@Transactional
public class FolderService {

	// Managed Repository ----------------------------------------------------

	@Autowired
	private FolderRepository	folderRepository;


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

}
