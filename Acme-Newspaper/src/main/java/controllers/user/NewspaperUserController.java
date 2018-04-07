/*
 * NewspaperUserController.java
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ArticleService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/newspaper/user")
public class NewspaperUserController extends AbstractController {

	private final String		ACTOR_WS	= "user/";

	// Services -------------------------------------------------

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ArticleService		articleService;

	@Autowired
	private UserService			userService;


	// C-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	// v2.0 - Modified by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId) {
		final ModelAndView res;

		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);

		final User viewer = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(viewer);

		final Boolean own = viewer.getNewspapers().contains(newspaper);

		final Collection<Article> articles = new ArrayList<Article>();
		final Collection<Article> finalArticles = this.articleService.getAllFinalByNewspaper(newspaper);

		if (own)
			//If the user is the publisher, he/she can see all articles
			articles.addAll(newspaper.getArticles());
		else {
			//If not, he/she can only see the published ones and his/her articles in draft mode
			articles.addAll(finalArticles);
			articles.addAll(this.articleService.getUnpublisedArticles(viewer));
		}

		final Boolean canBePublished = (newspaper.getArticles().size() != 0) && (finalArticles.size() == newspaper.getArticles().size());

		res = new ModelAndView("newspaper/display");
		res.addObject("newspaper", newspaper);
		res.addObject("articles", articles);
		res.addObject("actorWS", this.ACTOR_WS);
		res.addObject("own", own);
		res.addObject("canBePublished", canBePublished);

		return res;
	}
	/* v1.0 - josembell */
	@RequestMapping(value = "/listPublished", method = RequestMethod.GET)
	public ModelAndView listPublished() {
		final ModelAndView result;
		final Collection<Newspaper> newspapers = this.newspaperService.findAllPublished();

		result = new ModelAndView("newspaper/list");

		result.addObject("newspapers", newspapers);
		result.addObject("actorWS", this.ACTOR_WS);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView listMine() {
		final ModelAndView result;
		Collection<Newspaper> newspapers = null;
		result = new ModelAndView("newspaper/list");

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		newspapers = user.getNewspapers();

		result.addObject("mine", true);
		result.addObject("newspapers", newspapers);
		result.addObject("actorWS", this.ACTOR_WS);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Newspaper newspaper = this.newspaperService.create();

		result = this.createEditModelAndView(newspaper);

		return result;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/listSearchResults", method = RequestMethod.GET)
	public ModelAndView listSearchResults(@RequestParam final String keyword) {
		final ModelAndView res;
		final Collection<Newspaper> newspapers = this.newspaperService.findPublishedByKeyword(keyword);

		res = new ModelAndView("newspaper/list");
		res.addObject("newspapers", newspapers);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/listUnpublished", method = RequestMethod.GET)
	public ModelAndView listUnpublished() {
		final Boolean unpublished = true;
		final ModelAndView res;
		final Collection<Newspaper> newspapers = this.newspaperService.findAllUnpublished();

		res = new ModelAndView("newspaper/list");

		res.addObject("newspapers", newspapers);
		res.addObject("unpublished", unpublished);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/publish", method = RequestMethod.POST, params = "save")
	public ModelAndView publish(final Newspaper prunedNewspaper, final BindingResult binding) {

		ModelAndView res;

		final Newspaper newspaperToPublish = this.newspaperService.reconstructPruned(prunedNewspaper, binding);

		if (binding.hasErrors()) {
			res = new ModelAndView("newspaper/publish");
			res.addObject("newspaper", prunedNewspaper);
			res.addObject("message", "newspaper.commit.error");
		} else
			try {
				this.newspaperService.publish(newspaperToPublish);
				res = new ModelAndView("redirect:display.do?newspaperId=" + newspaperToPublish.getId());
			} catch (final RuntimeException oops) {
				res = new ModelAndView("newspaper/publish");
				res.addObject("newspaper", prunedNewspaper);
				res.addObject("message", "newspaper.articles.notFinal");
			} catch (final Throwable oops) {
				res = new ModelAndView("newspaper/publish");
				res.addObject("newspaper", prunedNewspaper);
				res.addObject("message", "newspaper.commit.error");
			}

		return res;
	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	public ModelAndView requestPublish(@RequestParam final int newspaperId) {

		final ModelAndView res;

		final User publisher = this.userService.findByUserAccount(LoginService.getPrincipal());
		final Newspaper newspaperToPublish = this.newspaperService.findOne(newspaperId);

		//No newspaper can be published if any of its articles is in draft
		Assert.isTrue(this.newspaperService.canBePublished(newspaperToPublish, publisher));

		res = new ModelAndView("newspaper/publish");
		res.addObject("newspaper", newspaperToPublish);

		return res;

	}

	// v1.0 Implemented by Alicia
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView res;

		res = new ModelAndView("newspaper/search");

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Newspaper newspaper, final BindingResult binding) {
		ModelAndView res;
		final Newspaper newspaperToSave;

		newspaperToSave = this.newspaperService.reconstruct(newspaper, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(newspaper);
		else
			try {

				this.newspaperService.save(newspaperToSave);

				res = new ModelAndView("redirect:/newspaper/user/listMine.do");

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(newspaper, "newspaper.commit.error");
			}

		return res;

	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Newspaper newspaper) {
		ModelAndView result;
		result = this.createEditModelAndView(newspaper, null);
		return result;
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Newspaper newspaper, final String message) {
		ModelAndView result;
		result = new ModelAndView("newspaper/edit");
		result.addObject("newspaper", newspaper);
		result.addObject("message", message);
		return result;
	}

}
