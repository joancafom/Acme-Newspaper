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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.UserService;
import domain.Article;
import domain.User;
import forms.UserRegistrationForm;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	private final String	ACTOR_WS	= "";

	//Services
	@Autowired
	private UserService		userService;

	@Autowired
	private ArticleService	articleService;


	//C-level requirements -------------------------------

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {

		final ModelAndView res;

		final User userToDisplay = this.userService.findOne(userId);
		Assert.notNull(userToDisplay);

		final Collection<Article> publicArticles = this.articleService.getPublishedAndPublicByWriter(userToDisplay);
		//Null is checked inside the method already

		res = new ModelAndView("user/display");
		res.addObject("user", userToDisplay);
		res.addObject("publicArticles", publicArticles);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;

	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView res;

		final Collection<User> usersToList = this.userService.findAll();
		Assert.notNull(usersToList);

		res = new ModelAndView("user/list");
		res.addObject("users", usersToList);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;

	}

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
