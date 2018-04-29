
package services;

import java.util.ArrayList;
import java.util.Collection;
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
import domain.Administrator;
import domain.Folder;
import forms.ANMessageForm;

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

	@Autowired
	private AdministratorService		adminService;

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

		anMessage.setRecipients(new ArrayList<Actor>());

		return anMessage;
	}

	// v1.0 - Implemented by Alicia
	public void delete(final ANMessage anMessage) {
		Assert.notNull(anMessage);

		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(actor);

		Assert.isTrue(actor.getReceivedMessages().contains(anMessage) || actor.getSentMessages().contains(anMessage));
		Assert.isTrue(anMessage.getFolder().getActor().equals(actor));

		if (anMessage.getFolder().getName().equals("Trash Box")) {
			actor.getReceivedMessages().remove(anMessage);
			actor.getSentMessages().remove(anMessage);

			this.anMessageRepository.delete(anMessage);
		} else {
			final Folder folder = this.folderService.findByActorAndName(actor, "Trash Box");
			anMessage.setFolder(folder);
			this.save(anMessage);
		}
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
	public ANMessage findOneTest(final int anMessageId) {
		final ANMessage anMessage = this.anMessageRepository.findOne(anMessageId);

		return anMessage;
	}

	// v1.0 - Implemented by Alicia
	/* v2.0 - josembell */
	public ANMessage save(final ANMessage anMessage) {
		Assert.notNull(anMessage);

		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(anMessage.getFolder());
		Assert.notNull(actor);
		Assert.isTrue(anMessage.getFolder().getActor().equals(actor) || anMessage.getSender().equals(actor));

		final ANMessage saved = this.anMessageRepository.save(anMessage);
		return saved;
	}

	// v1.0 - Implemented by Alicia
	public void flush() {
		this.anMessageRepository.flush();
	}

	// Other Business Methods ------------------------------------------------

	// v1.0 - Implemented by Alicia
	public ANMessage reconstruct(final ANMessageForm anMessageForm, final BindingResult binding) {
		Assert.notNull(anMessageForm);

		final Actor sender = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sender);

		final ANMessage anMessage = this.create();

		anMessage.setSubject(anMessageForm.getSubject());
		anMessage.setBody(anMessageForm.getBody());
		anMessage.setPriority(anMessageForm.getPriority());
		anMessage.getRecipients().add(this.actorService.findOne(new Integer(anMessageForm.getRecipients())));

		final Folder folder = this.folderService.findByActorAndName(sender, "Out Box");

		anMessage.setSentMoment(new Date(System.currentTimeMillis() - 1000L));
		anMessage.setSender(sender);
		anMessage.setFolder(folder);

		this.validator.validate(anMessage, binding);

		return anMessage;
	}

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by Alicia
	public ANMessage reconstruct(final ANMessage anMessage, final BindingResult binding) {
		Assert.notNull(anMessage);

		final Actor sender = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sender);

		final ANMessage oldANMessage = this.findOne(anMessage.getId());

		anMessage.setBody(oldANMessage.getBody());
		anMessage.setPriority(oldANMessage.getPriority());
		anMessage.setRecipients(oldANMessage.getRecipients());
		anMessage.setSender(oldANMessage.getSender());
		anMessage.setSentMoment(oldANMessage.getSentMoment());
		anMessage.setSubject(oldANMessage.getSubject());

		this.validator.validate(anMessage, binding);

		return anMessage;
	}

	// v1.0 - Implemented by JA
	// v2.0 - Updated by Alicia
	public ANMessage reconstructBroadcast(final ANMessage anMessage, final BindingResult binding) {
		Assert.notNull(anMessage);
		Assert.notNull(anMessage.getId() == 0);

		final Actor sender = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sender);

		final Folder folder = this.folderService.findByActorAndName(sender, "Out Box");

		anMessage.setSentMoment(new Date(System.currentTimeMillis() - 1000L));
		anMessage.setSender(sender);
		anMessage.setFolder(folder);

		//We put whatever actor we want as a Recipient to bypass the validation
		//Later on, this will be overwritten by sendBroadcast Method
		final Collection<Actor> recipients = new ArrayList<Actor>();
		recipients.add(sender);
		anMessage.setRecipients(recipients);

		this.validator.validate(anMessage, binding);

		return anMessage;
	}

	// A-Level Requirements ------------------------------------------------------

	//v1.0 - Implemented by JA
	// v2.0 - Updated by Alicia
	public void broadcastNotification(final ANMessage message) {

		Assert.notNull(message);

		//Ensure an admin is the sender
		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		//Ensure the sender of the message is the same one
		Assert.notNull(message.getSender());
		Assert.isTrue(admin.equals(message.getSender()));

		final Collection<Actor> allActors = this.actorService.findAll();

		message.setRecipients(allActors);
		this.send(allActors, message);

	}

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by JA
	// v3.0 - Updated by Alicia
	public void send(final Collection<Actor> recipients, final ANMessage messageToSend) {
		Assert.notNull(recipients);
		Assert.notEmpty(recipients);
		Assert.notNull(messageToSend);
		Assert.isTrue(messageToSend.getId() == 0);

		final Actor sender = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sender);

		messageToSend.setSender(sender);

		for (final Actor a : recipients) {

			final ANMessage receivedMessage = this.create();
			receivedMessage.setBody(messageToSend.getBody());
			receivedMessage.setPriority(messageToSend.getPriority());
			receivedMessage.setRecipients(messageToSend.getRecipients());
			receivedMessage.setSender(sender);
			receivedMessage.setSubject(messageToSend.getSubject());

			final Date sentMoment = new Date(System.currentTimeMillis() - 1000L);
			messageToSend.setSentMoment(sentMoment);
			receivedMessage.setSentMoment(sentMoment);

			final Boolean containsTabooVeredict = this.systemConfigurationService.containsTaboo(receivedMessage.getBody() + " " + receivedMessage.getSubject());

			final Folder outBox = this.folderService.findByActorAndName(sender, "Out Box");
			messageToSend.setFolder(outBox);
			sender.getSentMessages().add(messageToSend);

			Folder receptionFolder;
			if (containsTabooVeredict)
				receptionFolder = this.folderService.findByActorAndName(a, "Spam Box");
			else
				receptionFolder = this.folderService.findByActorAndName(a, "In Box");
			receivedMessage.setFolder(receptionFolder);
			a.getReceivedMessages().add(receivedMessage);

			this.save(receivedMessage);
		}

		this.save(messageToSend);
	}

}
