/*
 * ANMessageAdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ANMessageService;
import services.ActorService;
import services.FolderService;
import controllers.AbstractController;
import domain.ANMessage;
import domain.Actor;
import domain.Folder;

@Controller
@RequestMapping("/anMessage/administrator")
public class ANMessageAdministratorController extends AbstractController {

	private final String		ACTOR_WS	= "administrator/";

	// Services -------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ANMessageService	anMessageService;

	@Autowired
	private FolderService		folderService;


	// B-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by JA (broadcast)
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(required = false, defaultValue = "false") final Boolean isBroadcast) {
		final ModelAndView res;
		final ANMessage anMessage;

		anMessage = this.anMessageService.create();

		res = this.createEditModelAndView(anMessage);
		res.addObject("isBroadcast", isBroadcast);

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int anMessageId) {
		final ModelAndView res;

		final ANMessage anMessage = this.anMessageService.findOne(anMessageId);
		Assert.notNull(anMessage);

		res = new ModelAndView("redirect:/folder/" + this.ACTOR_WS + "list.do");

		try {
			this.anMessageService.delete(anMessage);
		} catch (final Throwable oops) {
			res.addObject("message", "anMessage.commit.error");
		}

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int anMessageId) {
		final ModelAndView res;

		final ANMessage anMessage = this.anMessageService.findOne(anMessageId);

		res = new ModelAndView("anMessage/display");
		res.addObject("ANMessage", anMessage);

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int anMessageId) {
		final ModelAndView res;

		final ANMessage anMessage = this.anMessageService.findOne(anMessageId);
		Assert.notNull(anMessage);

		res = this.createEditModelAndView(anMessage);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ANMessage prunedANMessage, final BindingResult binding) {
		ModelAndView res;

		final ANMessage anMessage = this.anMessageService.reconstruct(prunedANMessage, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(anMessage);
		else
			try {
				if (anMessage.getId() == 0)
					this.anMessageService.send(anMessage.getRecipient(), anMessage);
				else
					this.anMessageService.save(anMessage);
				res = new ModelAndView("redirect:/folder/" + this.ACTOR_WS + "list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(anMessage, "anMessage.commit.error");
			}

		return res;
	}

	//A-Level Requirements ---------------------------------------------------------

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveBroadcast")
	public ModelAndView sendBroadcast(final ANMessage prunedANMessage, final BindingResult binding) {
		ModelAndView res;

		final ANMessage anMessage = this.anMessageService.reconstructBroadcast(prunedANMessage, binding);

		if (binding.hasErrors()) {
			res = this.createEditModelAndView(anMessage);
			res.addObject("isBroadcast", true);
		} else
			try {
				this.anMessageService.broadcastNotification(anMessage);
				res = new ModelAndView("redirect:/folder/administrator/list.do");

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(anMessage, "anMessage.commit.error");
				res.addObject("isBroadcast", true);
			}

		return res;
	}
	// Ancillary Methods ----------------------------------------
	// v1.0 - Implemented by Alicia
	protected ModelAndView createEditModelAndView(final ANMessage anMessage) {
		ModelAndView res;

		res = this.createEditModelAndView(anMessage, null);

		return res;
	}

	// v1.0 - Implemented by Alicia
	protected ModelAndView createEditModelAndView(final ANMessage anMessage, final String message) {
		final ModelAndView res;

		res = new ModelAndView("anMessage/edit");
		res.addObject("ANMessage", anMessage);
		res.addObject("message", message);
		res.addObject("actorWS", this.ACTOR_WS);

		if (anMessage.getId() == 0) {
			final Collection<Actor> recipients = this.actorService.findAll();
			res.addObject("recipients", recipients);
		} else {
			final Collection<Folder> folders = this.folderService.getAllExceptOneForPrincipal(anMessage.getFolder());
			res.addObject("folders", folders);
		}

		return res;
	}
}
