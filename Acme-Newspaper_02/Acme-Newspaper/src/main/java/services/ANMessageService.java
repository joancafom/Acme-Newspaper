
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
		Assert.isTrue(anMessage.getFolder().getActor().equals(actor) || (anMessage.getSender().equals(actor) && anMessage.getId() == 0));

		final ANMessage saved = this.anMessageRepository.save(anMessage);
		this.folderService.saveSendMessage(anMessage.getFolder());
		return saved;
	}
	// v1.0 - Implemented by Alicia
	public void flush() {
		this.anMessageRepository.flush();
	}

	// Other Business Methods ------------------------------------------------

	// v1.0 - Implemented by Alicia
	// v2.0 - Modified by JA
	public ANMessage reconstruct(final ANMessageForm anMessageForm, final BindingResult binding) {
		Assert.notNull(anMessageForm);
		Assert.isTrue(anMessageForm.getId() == 0);

		final Actor sender = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sender);

		final ANMessage anMessage = this.create();

		anMessage.setSubject(anMessageForm.getSubject());
		anMessage.setBody(anMessageForm.getBody());
		anMessage.setPriority(anMessageForm.getPriority());
		anMessage.setRecipients(new ArrayList<Actor>());

		if (anMessageForm.getRecipients().size() == 1 && anMessageForm.getRecipients().contains("0"))
			anMessage.setRecipients(null);
		else
			for (final String s : anMessageForm.getRecipients())
				anMessage.getRecipients().add(this.actorService.findOne(new Integer(s)));

		final Folder folder = this.folderService.findByActorAndName(sender, "Out Box");

		anMessage.setSentMoment(new Date(System.currentTimeMillis() - 1000L));
		anMessage.setSender(sender);
		anMessage.setFolder(folder);

		this.validator.validate(anMessage, binding);

		return anMessage;
	}

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by Alicia
	// v3.0 - Modified by JA
	public ANMessage reconstruct(final ANMessage anMessage, final BindingResult binding) {
		Assert.notNull(anMessage);
		Assert.isTrue(anMessage.getId() != 0);

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
		Assert.isTrue(anMessage.getId() == 0);

		final Administrator sender = this.adminService.findByUserAccount(LoginService.getPrincipal());
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
	// v3.0 - Updated by JA
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
	// v4.0 - Updated by JA
	// v5.0 - Updated by Alicia
	public void send(final Collection<Actor> recipients, final ANMessage messageToSend) {

		Assert.notNull(recipients);
		Assert.notEmpty(recipients);
		Assert.notNull(messageToSend);
		Assert.isTrue(messageToSend.getId() == 0);

		final Actor sender = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(sender);

		messageToSend.setSender(sender);

		//Broadcast messages may only be sent by Administrators
		if (!(sender instanceof Administrator))
			Assert.isTrue(recipients.size() == 1);

		final Boolean containsTabooVeredict = this.systemConfigurationService.containsTaboo(messageToSend.getBody() + " " + messageToSend.getSubject());

		final Date sentMoment = new Date(System.currentTimeMillis() - 1000L);
		messageToSend.setSentMoment(sentMoment);

		final Folder outBox = this.folderService.findByActorAndName(sender, "Out Box");
		messageToSend.setFolder(outBox);
		final ANMessage messageToSendS = this.save(messageToSend);

		outBox.getAnMessages().add(messageToSendS);
		this.folderService.saveSendMessage(outBox);

		sender.getSentMessages().add(messageToSendS);
		this.actorService.save(sender);

		for (final Actor a : recipients) {

			final ANMessage receivedMessage = this.create();
			receivedMessage.setBody(messageToSend.getBody());
			receivedMessage.setPriority(messageToSend.getPriority());
			receivedMessage.setRecipients(messageToSend.getRecipients());
			receivedMessage.setSender(sender);
			receivedMessage.setSubject(messageToSend.getSubject());

			receivedMessage.setSentMoment(sentMoment);

			Folder receptionFolder;
			if (containsTabooVeredict)
				receptionFolder = this.folderService.findByActorAndName(a, "Spam Box");
			else
				receptionFolder = this.folderService.findByActorAndName(a, "In Box");

			receivedMessage.setFolder(receptionFolder);
			final ANMessage anReceivedMessageS = this.save(receivedMessage);

			receptionFolder.getAnMessages().add(anReceivedMessageS);
			this.folderService.saveSendMessage(receptionFolder);

			a.getReceivedMessages().add(anReceivedMessageS);
			this.actorService.save(a);
		}

	}

	/* v1.0 - josembell */
	public Page<ANMessage> findMessagesByFolder(final Integer page, final int size, final Folder parentFolder) {
		final Page<ANMessage> res = this.anMessageRepository.findMessagesByFolderPaged(parentFolder.getId(), new PageRequest(page - 1, size));
		return res;
	}
}
