/*
 * AgentController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AgentService;
import domain.Agent;
import forms.ActorRegistrationForm;

@Controller
@RequestMapping("/agent")
public class AgentController extends AbstractController {

	private final String	ACTOR_WS	= "";

	//Services
	@Autowired
	private AgentService	agentService;


	//C-level requirements -------------------------------

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {

		final ModelAndView res;

		final ActorRegistrationForm newUserForm = new ActorRegistrationForm();
		res = this.createEditModelAndView(newUserForm);

		return res;

	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(final ActorRegistrationForm agentRegistrationForm, final BindingResult binding) {

		ModelAndView res;
		final Agent agentToSave;

		agentToSave = this.agentService.reconstruct(agentRegistrationForm, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(agentRegistrationForm);
		else
			try {

				Assert.isTrue(agentRegistrationForm.getAcceptedTerms());
				Assert.isTrue(agentRegistrationForm.getPassword().equals(agentRegistrationForm.getPasswordConfirmation()));

				this.agentService.save(agentToSave);

				res = new ModelAndView("welcome/index");

			} catch (final DataIntegrityViolationException oops) {
				res = this.createEditModelAndView(agentRegistrationForm, "agent.userAccount.username.duplicated");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(agentRegistrationForm, "agent.commit.error");
			}

		return res;

	}

	//Ancillary Methods -------------------------------

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final ActorRegistrationForm userRegistrationForm) {

		final ModelAndView res;

		res = this.createEditModelAndView(userRegistrationForm, null);

		return res;

	}

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final ActorRegistrationForm userRegistrationForm, final String message) {

		final ModelAndView res;

		res = new ModelAndView("user/register");
		res.addObject("userRegistrationForm", userRegistrationForm);
		res.addObject("message", message);

		return res;
	}
}
