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

	// Services -------------------------------------------------

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ArticleService		articleService;

	@Autowired
	private UserService			userService;


	// C-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId) {
		final ModelAndView res;

		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);

		final Collection<Article> articles = this.articleService.getAllFinalByNewspaper(newspaper);

		res = new ModelAndView("newspaper/display");
		res.addObject("newspaper", newspaper);
		res.addObject("articles", articles);

		res.addObject("actorWS", "user/");

		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Newspaper> newspapers = this.newspaperService.findAllPublished();

		result = new ModelAndView("newspaper/list");

		result.addObject("newspapers", newspapers);
		result.addObject("actorWS", "user/");

		return result;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/listSearchResults", method = RequestMethod.GET)
	public ModelAndView listSearchResults(@RequestParam final String keyword) {
		final ModelAndView res;
		final Collection<Newspaper> newspapers = this.newspaperService.findPublishedByKeyword(keyword);

		res = new ModelAndView("newspaper/list");
		res.addObject("newspapers", newspapers);

		res.addObject("actorWS", "user/");

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
				res = new ModelAndView("newspaper/user/display.do?newspaperId=" + newspaperToPublish.getId());
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

		res.addObject("actorWS", "user/");

		return res;
	}

}
