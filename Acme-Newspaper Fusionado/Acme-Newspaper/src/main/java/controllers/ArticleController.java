/*
 * WelcomeController.java
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
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import domain.Article;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractController {

	/* Services */
	@Autowired
	private ArticleService	articleService;


	/* Level C Requirements */

	/* v1.0 - josembell */
	// v2.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		ModelAndView result;
		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);
		Assert.isTrue(article.getPublicationDate() != null);
		Assert.isTrue(article.getNewspaper().getIsPublic());

		result = new ModelAndView("article/display");
		result.addObject("article", article);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView result;

		result = new ModelAndView("article/search");

		return result;
	}

	// v2.0 - Updated by Alicia
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final String keyword) {
		final ModelAndView result;
		final Collection<Article> articles = this.articleService.getPublicAndPublishedByKeyword(keyword);

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("keyword", keyword);

		return result;
	}
}
