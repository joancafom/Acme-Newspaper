/*
 * ArticleAdministratorController.java
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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AdvertisementService;
import services.ArticleService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Article;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/article/user")
public class ArticleUserController extends AbstractController {

	// Services -------------------------------------------------

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private UserService				userService;

	@Autowired
	private AdvertisementService	advertisementService;


	/* Level C Requirements */

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by JA
	// v3.0 - Updated by Alicia
	// v4.0 - Updated by Alicia
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int entityId) {
		final ModelAndView res;

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		final Newspaper newspaper = this.newspaperService.findOne(entityId);
		final Article mainArticle = this.articleService.findOne(entityId);

		Assert.isTrue((newspaper != null) || (mainArticle != null));

		final Article article;

		if (newspaper != null) {
			article = this.articleService.create(newspaper);
			res = this.createEditModelAndView(article);

		} else {
			Assert.isNull(mainArticle.getMainArticle());
			Assert.isTrue(mainArticle.getWriter().equals(user));
			Assert.isTrue(mainArticle.getIsFinal());
			Assert.notNull(mainArticle.getNewspaper().getPublicationDate());

			article = this.articleService.create(mainArticle);
			res = this.createEditModelAndView(article);
		}

		return res;
	}

	/* v1.0 - josembell */
	// v2.0 - Updated by Alicia
	// v3.0 - Updated by JA (ads)
	// v4.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId, @RequestParam(value = "d-4004458-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;
		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		Boolean owned = false;
		if (article.getWriter().equals(user))
			owned = true;

		if (!article.getWriter().equals(user) && article.getMainArticle() == null)
			Assert.isTrue(article.getIsFinal() || user.getNewspapers().contains(article.getNewspaper()));
		else if (!article.getWriter().equals(user) && article.getMainArticle() != null)
			Assert.isTrue(article.getMainArticle().getIsFinal() || user.getNewspapers().contains(article.getMainArticle().getNewspaper()));

		final Advertisement ad = this.advertisementService.getRandomAdvertisement(article.getNewspaper());

		final Page<Article> pageResult = this.articleService.getFollowUpsByArticle(article, page, 5);
		final Collection<Article> followUps = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("followUps", followUps);
		result.addObject("resultSize", resultSize);
		result.addObject("ad", ad);
		result.addObject("owned", owned);

		result.addObject("actorWS", "user/");

		return result;
	}
	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int articleId) {
		ModelAndView result;

		final Article article = this.articleService.findOne(articleId);
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(article);
		Assert.notNull(user);

		Assert.isTrue(article.getWriter().equals(user));
		Assert.isTrue(!article.getIsFinal());
		Assert.isNull(article.getNewspaper().getPublicationDate());

		result = this.createEditModelAndView(article);

		return result;

	}

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by Alicia
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(@RequestParam final int entityId, final Article prunedArticle, final BindingResult binding) {
		ModelAndView res = null;

		final Newspaper newspaper = this.newspaperService.findOne(entityId);
		final Article mainArticle = this.articleService.findOne(entityId);

		Assert.isTrue((newspaper != null) || (mainArticle != null));

		final Article article = this.articleService.reconstructSave(prunedArticle, newspaper, mainArticle, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(article);
		else
			try {
				this.articleService.save(article);
				if (mainArticle != null)
					res = new ModelAndView("redirect:/article/user/display.do?articleId=" + mainArticle.getId());
				else if (newspaper != null)
					res = new ModelAndView("redirect:/newspaper/user/display.do?newspaperId" + newspaper.getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(article, "article.commit.error");
			}

		return res;

	}
	/* v1.0 - josembell */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView result;

		result = new ModelAndView("article/search");
		result.addObject("actorWS", "user/");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final String keyword, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer page) {
		ModelAndView result;

		final Page<Article> pageResult = this.articleService.findPublishedByKeyword(keyword, page, 5);
		final Collection<Article> articles = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("keyword", keyword);
		result.addObject("actorWS", "user/");
		result.addObject("resultSize", resultSize);

		return result;
	}

	// Ancillary Methods -------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Article article) {

		ModelAndView result;
		result = this.createEditModelAndView(article, null);

		return result;
	}

	// v1.0 - Unknown (josembell maybe)
	// v2.0 - Modified by JA
	// v3.0 - Updated by Alicia
	private ModelAndView createEditModelAndView(final Article article, final String message) {

		final ModelAndView res;

		res = new ModelAndView("article/edit");

		res.addObject("article", article);
		res.addObject("message", message);

		if (article.getMainArticle() != null) {
			//We are in a Follow Up

			res.addObject("mainArticleId", article.getMainArticle().getId());
			res.addObject("isFollowUp", true);
		} else {
			res.addObject("newspaperId", article.getNewspaper().getId());
			res.addObject("isFollowUp", false);
		}

		return res;
	}

}
