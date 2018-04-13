/*
 * UserController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.user;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ArticleService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.Chirp;
import domain.User;

@Controller
@RequestMapping("/user/user")
public class UserUserController extends AbstractController {

	private final String	ACTOR_WS	= "user/";

	//Services
	@Autowired
	private UserService		userService;

	@Autowired
	private ArticleService	articleService;


	//C-level requirements -------------------------------

	//v1.0 - Implemented by JA
	// v2.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {

		final ModelAndView res;

		final User userToDisplay = this.userService.findOne(userId);
		Assert.notNull(userToDisplay);

		final Collection<Article> publishedArticles = this.articleService.getPublisedArticles(userToDisplay);
		//Null is checked inside the method already

		final User principal = this.userService.findByUserAccount(LoginService.getPrincipal());
		Boolean following = false;

		if (principal.getFollowees().contains(userToDisplay))
			following = true;

		Boolean mine = false;
		final Collection<Chirp> myChirps = new ArrayList<Chirp>(userToDisplay.getChirps());

		if (principal.getId() == userId)
			mine = true;

		res = new ModelAndView("user/display");
		res.addObject("user", userToDisplay);
		res.addObject("publishedArticles", publishedArticles);
		res.addObject("following", following);
		res.addObject("mine", mine);
		res.addObject("chirps", myChirps);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;

	}
	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/followers", method = RequestMethod.GET)
	public ModelAndView listFollowers() {

		final ModelAndView res;

		final User viewer = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(viewer);

		final Collection<User> usersToList = viewer.getFollowers();

		Assert.notNull(usersToList);

		res = new ModelAndView("user/followers");
		res.addObject("users", usersToList);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;

	}

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/following", method = RequestMethod.GET)
	public ModelAndView listFollowing() {

		final ModelAndView res;

		final User viewer = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(viewer);

		final Collection<User> usersToList = viewer.getFollowees();

		Assert.notNull(usersToList);

		res = new ModelAndView("user/following");
		res.addObject("users", usersToList);
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

	// B-Level Requirements -------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	public ModelAndView follow(@RequestParam final int userId) {
		ModelAndView res;
		final User user = this.userService.findOne(userId);

		Assert.notNull(user);

		res = new ModelAndView("redirect:/user/user/display.do?userId=" + user.getId());

		try {
			this.userService.follow(user);
		} catch (final Throwable oops) {
			res.addObject("message", "user.commit.error");
		}

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/unfollow", method = RequestMethod.GET)
	public ModelAndView unfollow(@RequestParam final int userId) {
		ModelAndView res;
		final User user = this.userService.findOne(userId);

		Assert.notNull(user);

		res = new ModelAndView("redirect:/user/user/display.do?userId=" + user.getId());

		try {
			this.userService.unfollow(user);
		} catch (final Throwable oops) {
			res.addObject("message", "user.commit.error");
		}

		return res;
	}

}
