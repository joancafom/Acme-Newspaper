
package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ANMessageRepository;
import security.LoginService;
import domain.ANMessage;
import domain.Actor;
import domain.Folder;

@Service
@Transactional
public class ANMessageService {

	// Managed Repository ----------------------------------------------------

	@Autowired
	private ANMessageRepository			anMessageRepository;

	// Supporting Services ---------------------------------------------------

	@Autowired
	private ActorService				actorService;

	@Autowired
	private FolderService				folderService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	// Validator -------------------------------------------------------------

	@Autowired
	private Validator					validator;


	// CRUD Methods ----------------------------------------------------------

	// v1.0 - Implemented by Alicia
	public ANMessage create() {
		final Actor sender = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sender);

		final ANMessage anMessage = new ANMessage();
		anMessage.setSender(sender);
		anMessage.setSentMoment(new Date());

		final Folder folder = this.folderService.findByActorAndName(sender, "Out Box");
		anMessage.setFolder(folder);

		return anMessage;
	}

	// v1.0 - Implemented by Alicia
	public ANMessage findOne(final int anMessageId) {
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(actor);

		final ANMessage anMessage = this.anMessageRepository.findOne(anMessageId);

		Assert.isTrue(actor.getSentMessages().contains(anMessage) || actor.getReceivedMessages().contains(anMessage));

		return anMessage;
	}

	// v1.0 - Implemented by Alicia
	public ANMessage save(final ANMessage anMessage) {
		Assert.notNull(anMessage);

		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(actor);

		return this.anMessageRepository.save(anMessage);
	}

	// Other Business Methods ------------------------------------------------

	// v1.0 - Implemented by Alicia
	public ANMessage reconstruct(final ANMessage anMessage, final BindingResult binding) {
		Assert.notNull(anMessage);

		final Actor sender = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sender);

		if (anMessage.getId() == 0) {
			final Folder folder = this.folderService.findByActorAndName(sender, "Out Box");

			anMessage.setSentMoment(new Date());
			anMessage.setSender(sender);
			anMessage.setFolder(folder);
		}

		this.validator.validate(anMessage, binding);

		return anMessage;
	}

	// v1.0 - Implemented by Alicia
	public void send(final Actor receiver, final ANMessage messageToSend) {
		Assert.notNull(receiver);
		Assert.notNull(messageToSend);

		final Actor sender = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sender);

		final ANMessage receivedMessage = this.create();
		receivedMessage.setBody(messageToSend.getBody());
		receivedMessage.setPriority(messageToSend.getPriority());
		receivedMessage.setRecipient(messageToSend.getRecipient());
		receivedMessage.setSender(messageToSend.getSender());
		receivedMessage.setSubject(messageToSend.getSubject());

		final Date sentMoment = new Date();
		messageToSend.setSentMoment(sentMoment);
		receivedMessage.setSentMoment(sentMoment);

		final Boolean containsTabooVeredict = this.systemConfigurationService.containsTaboo(receivedMessage.getBody() + " " + receivedMessage.getSubject());

		final Folder outBox = this.folderService.findByActorAndName(sender, "Out Box");
		messageToSend.setFolder(outBox);
		sender.getSentMessages().add(messageToSend);

		Folder receptionFolder;
		if (containsTabooVeredict)
			receptionFolder = this.folderService.findByActorAndName(receiver, "Spam Box");
		else
			receptionFolder = this.folderService.findByActorAndName(receiver, "In Box");
		receivedMessage.setFolder(receptionFolder);
		receiver.getReceivedMessages().add(receivedMessage);

		this.save(messageToSend);
		this.save(receivedMessage);
	}

}
