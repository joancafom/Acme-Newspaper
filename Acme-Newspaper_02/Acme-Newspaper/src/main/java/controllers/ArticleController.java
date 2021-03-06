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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.ArticleService;
import domain.Advertisement;
import domain.Article;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractController {

	/* Services */
	@Autowired
	private ArticleService			articleService;

	@Autowired
	private AdvertisementService	advertisementService;


	/* Level C Requirements */

	/* v1.0 - josembell */
	// v2.0 - Updated by Alicia
	// v3.0 - Updated by JA (ads)
	// v4.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId, @RequestParam(value = "d-4004458-p", defaultValue = "1") final Integer page) {
		ModelAndView result;
		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);

		if (article.getMainArticle() == null) {
			Assert.isTrue(article.getPublicationDate() != null);
			Assert.isTrue(article.getNewspaper().getIsPublic());
		} else {
			Assert.isTrue(article.getMainArticle().getPublicationDate() != null);
			Assert.isTrue(article.getMainArticle().getNewspaper().getIsPublic());
		}

		final Advertisement ad = this.advertisementService.getRandomAdvertisement(article.getNewspaper());

		final Page<Article> pageResult = this.articleService.getFollowUpsByArticle(article, page, 5);
		final Collection<Article> followUps = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("followUps", followUps);
		result.addObject("resultSize", resultSize);
		result.addObject("ad", ad);

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
	public ModelAndView list(@RequestParam final String keyword, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;

		final Page<Article> pageResult = this.articleService.getPublicAndPublishedByKeyword(keyword, page, 5);
		final Collection<Article> articles = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("keyword", keyword);
		result.addObject("resultSize", resultSize);

		return result;
	}
}
