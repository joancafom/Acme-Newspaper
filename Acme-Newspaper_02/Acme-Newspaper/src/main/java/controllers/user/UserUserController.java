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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ArticleService;
import services.ChirpService;
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

	@Autowired
	private ChirpService	chirpService;


	//C-level requirements -------------------------------

	//v1.0 - Implemented by JA
	// v2.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer articlesPage, @RequestParam(value = "d-147820-p", defaultValue = "1") final Integer chirpsPage) {

		final ModelAndView res;

		final User userToDisplay = this.userService.findOne(userId);
		Assert.notNull(userToDisplay);

		final Page<Article> publishedArticles = this.articleService.getPublisedArticles(userToDisplay, articlesPage, 5);
		final int articlesSize = new Long(publishedArticles.getTotalElements()).intValue();

		final User principal = this.userService.findByUserAccount(LoginService.getPrincipal());
		Boolean following = false;

		if (principal.getFollowees().contains(userToDisplay))
			following = true;

		Boolean mine = false;

		final Page<Chirp> myChirps = this.chirpService.getChirpsByUser(userToDisplay, chirpsPage, 5);
		final Integer chirpsSize = new Long(myChirps.getTotalElements()).intValue();

		if (principal.getId() == userId)
			mine = true;

		res = new ModelAndView("user/display");
		res.addObject("user", userToDisplay);
		res.addObject("publishedArticles", publishedArticles);
		res.addObject("articlesSize", articlesSize);
		res.addObject("following", following);
		res.addObject("mine", mine);
		res.addObject("chirps", myChirps);
		res.addObject("chirpsSize", chirpsSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;

	}
	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/followers", method = RequestMethod.GET)
	public ModelAndView listFollowers(@RequestParam(value = "d-49809-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;

		final User viewer = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(viewer);

		final Page<User> usersToList = this.userService.getFollowersByUser(viewer, page, 5);
		final Integer resultSize = new Long(usersToList.getTotalElements()).intValue();

		Assert.notNull(usersToList);

		res = new ModelAndView("user/followers");
		res.addObject("users", usersToList);
		res.addObject("resultSize", resultSize);
		res.addObject("landing", "followers");

		res.addObject("actorWS", this.ACTOR_WS);

		return res;

	}

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/following", method = RequestMethod.GET)
	public ModelAndView listFollowing(@RequestParam(value = "d-49809-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;

		final User viewer = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(viewer);

		final Page<User> usersToList = this.userService.getFolloweesByUser(viewer, page, 5);
		final Integer resultSize = new Long(usersToList.getTotalElements()).intValue();

		Assert.notNull(usersToList);

		res = new ModelAndView("user/following");
		res.addObject("users", usersToList);
		res.addObject("resultSize", resultSize);
		res.addObject("landing", "following");

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
