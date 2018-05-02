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
import java.util.HashSet;

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
	// v3.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);

		final User viewer = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(viewer);

		final Boolean own = viewer.getNewspapers().contains(newspaper);

		final Collection<Article> articles = new HashSet<Article>();
		Page<Article> pageResult;

		final Integer finalArticles = this.articleService.getAllFinalByNewspaperSize(newspaper);

		final User writer = this.userService.getWriterByNewspaper(newspaper);

		if (own) {
			//If the user is the publisher, he/she can see all articles

			pageResult = this.articleService.getAllArticlesByNewspaper(newspaper, page, 5);
			articles.addAll(pageResult.getContent());
		} else {
			//If not, he/she can only see the final ones and his/her articles in draft mode
			pageResult = this.articleService.getFinalAndUnpublishedArticles(viewer, newspaper, page, 5);
			articles.addAll(pageResult.getContent());
		}

		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();
		final Boolean canBePublished = (newspaper.getArticles().size() != 0) && (finalArticles == newspaper.getArticles().size());

		res = new ModelAndView("newspaper/display");
		res.addObject("newspaper", newspaper);
		res.addObject("articles", articles);
		res.addObject("userId", viewer.getId());
		res.addObject("actorWS", this.ACTOR_WS);
		res.addObject("own", own);
		res.addObject("resultSize", resultSize);
		res.addObject("canBePublished", canBePublished);
		res.addObject("writer", writer);

		return res;
	}
	/* v1.0 - josembell */
	@RequestMapping(value = "/listPublished", method = RequestMethod.GET)
	public ModelAndView listPublished(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;

		final Page<Newspaper> pageResult = this.newspaperService.findAllPublished(page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("newspaper/list");

		result.addObject("newspapers", newspapers);
		result.addObject("resultSize", resultSize);
		result.addObject("actorWS", this.ACTOR_WS);
		result.addObject("landing", "listPublished");

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView listMine(@RequestParam(required = false) final String message, @RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;

		Collection<Newspaper> newspapers = null;
		result = new ModelAndView("newspaper/list");

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

		final Page<Newspaper> pageResult = this.newspaperService.findByPublisher(user, page, 5);
		newspapers = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result.addObject("mine", true);
		result.addObject("newspapers", newspapers);

		if (message != null && message.equals("newspaper.commit.error"))
			result.addObject("message", "newspaper.commit.error");

		result.addObject("resultSize", resultSize);
		result.addObject("actorWS", this.ACTOR_WS);
		result.addObject("landing", "listMine");

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
	public ModelAndView listSearchResults(@RequestParam final String keyword, @RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Page<Newspaper> pageResult = this.newspaperService.findPublishedByKeyword(keyword, page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("newspaper/list");
		res.addObject("newspapers", newspapers);
		res.addObject("resultSize", resultSize);
		res.addObject("actorWS", this.ACTOR_WS);
		res.addObject("landing", "listSearchResults");

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/listUnpublished", method = RequestMethod.GET)
	public ModelAndView listUnpublished(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {

		final Boolean unpublished = true;
		final ModelAndView res;

		final Page<Newspaper> pageResult = this.newspaperService.findAllUnpublished(page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("newspaper/list");

		res.addObject("newspapers", newspapers);
		res.addObject("resultSize", resultSize);
		res.addObject("unpublished", unpublished);

		res.addObject("actorWS", this.ACTOR_WS);
		res.addObject("landing", "listUnpublished");

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

	//A-Level Requirements ------------

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/privatize", method = RequestMethod.GET)
	public ModelAndView privatize(@RequestParam final int newspaperId) {

		ModelAndView res;

		final Newspaper newspaperToPrivatize = this.newspaperService.findOne(newspaperId);

		res = new ModelAndView("redirect:listMine.do");

		try {
			this.newspaperService.privatize(newspaperToPrivatize);
		} catch (final Throwable oops) {
			res.addObject("message", "newspaper.commit.error");
		}

		return res;
	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/unprivatize", method = RequestMethod.GET)
	public ModelAndView unprivatize(@RequestParam final int newspaperId) {

		ModelAndView res;

		final Newspaper newspaperToPrivatize = this.newspaperService.findOne(newspaperId);

		res = new ModelAndView("redirect:listMine.do");

		try {
			this.newspaperService.unprivatize(newspaperToPrivatize);
		} catch (final Throwable oops) {
			res.addObject("message", "newspaper.commit.error");
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
