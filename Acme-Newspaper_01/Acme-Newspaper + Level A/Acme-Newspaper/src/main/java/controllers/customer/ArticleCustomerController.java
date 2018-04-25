/*
 * ArticleCustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ArticleService;
import services.CustomerService;
import services.SubscriptionService;
import controllers.AbstractController;
import domain.Article;
import domain.Customer;

@Controller
@RequestMapping("/article/customer")
public class ArticleCustomerController extends AbstractController {

	private final String		ACTOR_WS	= "customer/";

	// Services -------------------------------------------------

	@Autowired
	private ArticleService		articleService;

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private SubscriptionService	subscriptionService;


	// A-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		final ModelAndView res;
		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(customer);

		Assert.isTrue(this.subscriptionService.hasSubscription(customer, article.getNewspaper()) || article.getNewspaper().getIsPublic());
		Assert.isTrue(article.getPublicationDate() != null);

		res = new ModelAndView("article/display");
		res.addObject("article", article);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView res;
		res = new ModelAndView("article/search");

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final String keyword) {
		final ModelAndView res;

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(customer);

		final Collection<Article> articles = this.articleService.getCustomerSearchResults(keyword, customer);

		res = new ModelAndView("article/list");
		res.addObject("articles", articles);
		res.addObject("keyword", keyword);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
