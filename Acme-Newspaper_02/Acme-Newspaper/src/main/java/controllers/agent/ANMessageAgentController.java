/*
 * ANMessageAgentController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.agent;

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
import forms.ANMessageForm;

@Controller
@RequestMapping("/anMessage/agent")
public class ANMessageAgentController extends AbstractController {

	private final String		ACTOR_WS	= "agent/";

	// Services -------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ANMessageService	anMessageService;

	@Autowired
	private FolderService		folderService;


	// B-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView res;
		final ANMessageForm anMessage;

		anMessage = new ANMessageForm();

		res = this.createEditModelAndView(anMessage);

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
				this.anMessageService.save(anMessage);
				res = new ModelAndView("redirect:/folder/" + this.ACTOR_WS + "list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(anMessage, "anMessage.commit.error");
			}

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveForm")
	public ModelAndView save(final ANMessageForm formANMessage, final BindingResult binding) {
		ModelAndView res;

		final ANMessage anMessage = this.anMessageService.reconstruct(formANMessage, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(formANMessage);
		else
			try {
				this.anMessageService.send(anMessage.getRecipients(), anMessage);
				res = new ModelAndView("redirect:/folder/" + this.ACTOR_WS + "list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(formANMessage, "anMessage.commit.error");
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
	protected ModelAndView createEditModelAndView(final ANMessageForm anMessage) {
		ModelAndView res;

		res = this.createEditModelAndView(anMessage, null);

		return res;
	}

	// v1.0 - Implemented by Alicia
	protected ModelAndView createEditModelAndView(final ANMessage anMessage, final String message) {
		final ModelAndView res;

		final Collection<Folder> folders = this.folderService.getAllExceptOneForPrincipal(anMessage.getFolder());

		res = new ModelAndView("anMessage/edit");
		res.addObject("ANMessage", anMessage);
		res.addObject("message", message);
		res.addObject("folders", folders);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Implemented by Alicia
	protected ModelAndView createEditModelAndView(final ANMessageForm anMessage, final String message) {
		final ModelAndView res;

		final Collection<Actor> recipients = this.actorService.findAll();

		res = new ModelAndView("anMessage/edit");
		res.addObject("ANMessageForm", anMessage);
		res.addObject("message", message);
		res.addObject("recipients", recipients);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
