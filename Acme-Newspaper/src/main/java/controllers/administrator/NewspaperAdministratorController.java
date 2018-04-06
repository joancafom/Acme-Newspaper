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
}
