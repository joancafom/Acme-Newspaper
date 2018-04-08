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
@RequestMapping("/article/user")
public class ArticleUserController extends AbstractController {

	// Services -------------------------------------------------

	@Autowired
	private ArticleService		articleService;

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private UserService			userService;


	/* Level C Requirements */

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by JA
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
			res.addObject("newspaperId", entityId);
			res.addObject("isFollowUp", false);

		}

		else {
			article = this.articleService.create(mainArticle);
			res = this.createEditModelAndView(article);

			final Collection<Newspaper> unpublishedNewspapers = this.newspaperService.findAllUnpublished();
			res.addObject("mainArticleId", mainArticle.getId());
			res.addObject("unpublishedNewspapers", unpublishedNewspapers);
			res.addObject("isFollowUp", true);
		}

		return res;
	}

	/* v1.0 - josembell */
	// v2.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		final ModelAndView result;
		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		Boolean owned = false;

		if (article.getWriter().equals(user))
			owned = true;

		Assert.isTrue(article.getPublicationDate() != null || owned);

		result = new ModelAndView("article/display");
		result.addObject("article", article);
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
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(final Article prunedArticle, final BindingResult binding) {
		ModelAndView res;

		final Article article = this.articleService.reconstructSave(prunedArticle, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(article);
		else
			try {
				this.articleService.save(article);
				res = new ModelAndView("redirect:/newspaper/user/display.do?newspaperId=" + article.getNewspaper().getId());
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
	public ModelAndView list(@RequestParam final String keyword) {
		ModelAndView result;
		final Collection<Article> articles = this.articleService.findPublishedByKeyword(keyword);

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("keyword", keyword);
		result.addObject("actorWS", "user/");

		return result;
	}

	// Ancillary Methods -------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Article article) {

		ModelAndView result;
		result = this.createEditModelAndView(article, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final Article article, final String message) {

		final ModelAndView result;
		result = new ModelAndView("article/edit");
		result.addObject("article", article);
		result.addObject("message", message);

		return result;
	}

}
