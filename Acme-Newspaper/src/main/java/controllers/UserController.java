/*
 * UserController.java
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

import services.UserService;
import domain.User;
import forms.UserRegistrationForm;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	//Services
	@Autowired
	private UserService	userService;


	//C-level requirements -------------------------------

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {

		final ModelAndView res;

		final UserRegistrationForm newUserForm = new UserRegistrationForm();
		res = this.createEditModelAndView(newUserForm);

		return res;

	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final UserRegistrationForm userRegistrationForm, final BindingResult binding) {

		ModelAndView res;
		final User userToSave;

		userToSave = this.userService.reconstruct(userRegistrationForm, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(userRegistrationForm);
		else
			try {

				Assert.isTrue(userRegistrationForm.getAcceptedTerms());
				Assert.isTrue(userRegistrationForm.getPassword().equals(userRegistrationForm.getPasswordConfirmation()));

				this.userService.save(userToSave);

				res = new ModelAndView("welcome/index");

			} catch (final DataIntegrityViolationException oops) {
				res = this.createEditModelAndView(userRegistrationForm, "user.userAccount.username.duplicated");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(userRegistrationForm, "user.commit.error");
			}

		return res;

	}

	//Ancillary Methods -------------------------------

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final UserRegistrationForm userRegistrationForm) {

		final ModelAndView res;

		res = this.createEditModelAndView(userRegistrationForm, null);

		return res;

	}

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final UserRegistrationForm userRegistrationForm, final String message) {

		final ModelAndView res;

		res = new ModelAndView("user/register");
		res.addObject("userRegistrationForm", userRegistrationForm);
		res.addObject("message", message);

		return res;
	}
}
