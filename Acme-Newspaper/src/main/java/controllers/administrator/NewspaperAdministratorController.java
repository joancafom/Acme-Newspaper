/*
 * NewspaperAdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Article;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/administrator")
public class NewspaperAdministratorController extends AbstractController {

	// Services -------------------------------------------------

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ArticleService		articleService;


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

		res.addObject("actorWS", "administrator/");

		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/listPublished", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Newspaper> newspapers = this.newspaperService.findAllPublished();

		result = new ModelAndView("newspaper/list");

		result.addObject("newspapers", newspapers);
		result.addObject("actorWS", "administrator/");

		return result;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/listSearchResults", method = RequestMethod.GET)
	public ModelAndView listSearchResults(@RequestParam final String keyword) {
		final ModelAndView res;
		final Collection<Newspaper> newspapers = this.newspaperService.findPublishedByKeyword(keyword);

		res = new ModelAndView("newspaper/list");
		res.addObject("newspapers", newspapers);

		res.addObject("actorWS", "administrator/");

		return res;
	}

	// v1.0 Implemented by Alicia
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView res;

		res = new ModelAndView("newspaper/search");

		res.addObject("actorWS", "administrator/");

		return res;
	}
}
