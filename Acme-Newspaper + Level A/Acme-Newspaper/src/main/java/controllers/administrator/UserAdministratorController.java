/*
 * UserController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.Chirp;
import domain.User;

@Controller
@RequestMapping("/user/administrator")
public class UserAdministratorController extends AbstractController {

	private final String	ACTOR_WS	= "administrator/";

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

		final Collection<Article> publishedArticles = this.articleService.getPublisedArticles(userToDisplay);
		//Null is checked inside the method already

		final Collection<Chirp> myChirps = new ArrayList<Chirp>(userToDisplay.getChirps());

		res = new ModelAndView("user/display");
		res.addObject("user", userToDisplay);
		res.addObject("publishedArticles", publishedArticles);
		res.addObject("chirps", myChirps);
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

}
