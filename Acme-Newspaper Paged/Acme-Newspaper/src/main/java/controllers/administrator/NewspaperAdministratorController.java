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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
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

	private final String		ACTOR_WS	= "administrator/";

	// Services -------------------------------------------------

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ArticleService		articleService;


	// C-Level Requirements -------------------------------------

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/delete", method = RequestMethod.POST, params = "save")
	public ModelAndView delete(final Newspaper prunedNewspaper, final BindingResult binding) {

		ModelAndView res;

		final Newspaper newspaperToDelete = this.newspaperService.reconstructPruned(prunedNewspaper, binding);
		Assert.notNull(newspaperToDelete);

		try {
			this.newspaperService.delete(newspaperToDelete);
			res = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			res = new ModelAndView("newspaper/delete");
			res.addObject("newspaper", prunedNewspaper);
			res.addObject("message", "newspaper.commit.error");
		}

		return res;
	}
	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);

		final Page<Article> pageResult = this.articleService.getAllFinalByNewspaper(newspaper, page, 5);
		final Collection<Article> articles = pageResult.getContent();
		final Integer resultSize = pageResult.getTotalPages() * 5;

		res = new ModelAndView("newspaper/display");
		res.addObject("newspaper", newspaper);
		res.addObject("articles", articles);
		res.addObject("resultSize", resultSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	/* v1.0 - josembell */
	// v2.0 - Modified by JA
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;

		final Page<Newspaper> pageResult = this.newspaperService.findAll(page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = pageResult.getTotalPages() * 5;

		result = new ModelAndView("newspaper/list");

		result.addObject("newspapers", newspapers);
		result.addObject("resultSize", resultSize);
		result.addObject("actorWS", this.ACTOR_WS);

		return result;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/listSearchResults", method = RequestMethod.GET)
	public ModelAndView listSearchResults(@RequestParam final String keyword, @RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Page<Newspaper> pageResult = this.newspaperService.findPublishedByKeyword(keyword, page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = pageResult.getTotalPages() * 5;

		res = new ModelAndView("newspaper/list");
		res.addObject("newspapers", newspapers);
		res.addObject("resultSize", resultSize);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/listTabooed", method = RequestMethod.GET)
	public ModelAndView listTabooed(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Page<Newspaper> pageResult = this.newspaperService.getTabooed(page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = pageResult.getTotalPages() * 5;

		res = new ModelAndView("newspaper/list");
		res.addObject("newspapers", newspapers);
		res.addObject("resultSize", resultSize);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView requestDelete(@RequestParam final int newspaperId) {

		final ModelAndView res;

		final Newspaper newspaperToDelete = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaperToDelete);

		res = new ModelAndView("newspaper/delete");
		res.addObject("newspaper", newspaperToDelete);

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
}
