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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.ChirpService;
import services.UserService;
import domain.Article;
import domain.Chirp;
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

	@Autowired
	private ChirpService	chirpService;


	//C-level requirements -------------------------------

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer articlesPage, @RequestParam(value = "d-147820-p", defaultValue = "1") final Integer chirpsPage) {

		final ModelAndView res;

		final User userToDisplay = this.userService.findOne(userId);
		Assert.notNull(userToDisplay);

		final Page<Article> publicArticles = this.articleService.getPublishedAndPublicByWriter(userToDisplay, articlesPage, 5);
		final int resultSize = new Long(publicArticles.getTotalElements()).intValue();

		final Page<Chirp> myChirps = this.chirpService.getChirpsByUser(userToDisplay, chirpsPage, 5);
		final Integer chirpsSize = new Long(myChirps.getTotalElements()).intValue();

		res = new ModelAndView("user/display");
		res.addObject("user", userToDisplay);
		res.addObject("publicArticles", publicArticles);
		res.addObject("resultSize", resultSize);
		res.addObject("chirps", myChirps);
		res.addObject("chirpsSize", chirpsSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-49809-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Page<User> usersToList = this.userService.findAll(page, 5);
		final Integer resultSize = new Long(usersToList.getTotalElements()).intValue();
		Assert.notNull(usersToList);

		res = new ModelAndView("user/list");
		res.addObject("users", usersToList);
		res.addObject("resultSize", resultSize);
		res.addObject("landing", "list");

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
