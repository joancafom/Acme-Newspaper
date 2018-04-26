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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.ChirpService;
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

	@Autowired
	private ChirpService	chirpService;


	//C-level requirements -------------------------------

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer articlesPage, @RequestParam(value = "d-147820-p", defaultValue = "1") final Integer chirpsPage) {

		final ModelAndView res;

		final User userToDisplay = this.userService.findOne(userId);
		Assert.notNull(userToDisplay);

		final Page<Article> publishedArticles = this.articleService.getPublisedArticles(userToDisplay, articlesPage, 5);
		final int articlesSize = new Long(publishedArticles.getTotalElements()).intValue();

		final Page<Chirp> chirps = this.chirpService.getChirpsByUser(userToDisplay, chirpsPage, 5);
		final Integer chirpsSize = new Long(chirps.getTotalElements()).intValue();

		res = new ModelAndView("user/display");
		res.addObject("user", userToDisplay);
		res.addObject("publishedArticles", publishedArticles);
		res.addObject("articlesSize", articlesSize);
		res.addObject("chirps", chirps);
		res.addObject("chirpsSize", chirpsSize);
		res.addObject("admin", true);

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

}
