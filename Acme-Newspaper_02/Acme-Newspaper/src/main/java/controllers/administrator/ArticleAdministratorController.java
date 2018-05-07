/*
 * ArticleAdministratorController.java
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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.ArticleService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Article;

@Controller
@RequestMapping("/article/administrator")
public class ArticleAdministratorController extends AbstractController {

	// Services -------------------------------------------------

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private AdvertisementService	advertisementService;


	// C-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int articleId) {
		final ModelAndView res;

		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);
		Assert.isTrue(article.getIsFinal());

		res = new ModelAndView("article/delete");
		res.addObject("article", article);

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/delete", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Article prunedArticle, final BindingResult binding) {

		ModelAndView res;

		final Article article = this.articleService.reconstructDelete(prunedArticle, binding);
		Assert.notNull(article);

		try {
			this.articleService.delete(article);
			res = new ModelAndView("redirect:/newspaper/administrator/display.do?newspaperId=" + article.getNewspaper().getId());
		} catch (final Throwable oops) {
			res = new ModelAndView("article/delete");
			res.addObject("article", prunedArticle);
			res.addObject("message", "article.commit.error");
		}

		return res;
	}

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by JA (ads)
	// v3.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId, @RequestParam(value = "d-4004458-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);

		if (article.getMainArticle() == null)
			Assert.isTrue(article.getIsFinal());
		else
			Assert.isTrue(article.getMainArticle().getIsFinal());

		final Advertisement ad = this.advertisementService.getRandomAdvertisement(article.getNewspaper());

		final Page<Article> pageResult = this.articleService.getFollowUpsByArticle(article, page, 5);
		final Collection<Article> followUps = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("article/display");
		res.addObject("article", article);
		res.addObject("followUps", followUps);
		res.addObject("resultSize", resultSize);
		res.addObject("ad", ad);

		res.addObject("actorWS", "administrator/");

		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView result;

		result = new ModelAndView("article/search");
		result.addObject("actorWS", "administrator/");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String keyword, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;
		final Collection<Article> articles;
		final Page<Article> pageResult;

		res = new ModelAndView("article/list");
		res.addObject("actorWS", "administrator/");

		if (keyword != null) {

			pageResult = this.articleService.findPublishedByKeyword(keyword, page, 5);

			res.addObject("keyword", keyword);

		} else
			pageResult = this.articleService.findTabooedFinalArticles(page, 5);

		articles = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res.addObject("articles", articles);
		res.addObject("resultSize", resultSize);

		return res;
	}
}
