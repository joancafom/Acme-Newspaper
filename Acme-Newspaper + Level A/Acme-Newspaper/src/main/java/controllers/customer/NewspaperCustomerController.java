/*
 * NewspaperCustomerController.java
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
import services.NewspaperService;
import controllers.AbstractController;
import domain.Article;
import domain.Customer;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/customer")
public class NewspaperCustomerController extends AbstractController {

	private final String		ACTOR_WS	= "customer/";

	// Services -------------------------------------------------

	@Autowired
	private ArticleService		articleService;

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private NewspaperService	newspaperService;


	// A-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId) {
		final ModelAndView res;

		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(customer);

		final Boolean subscriber = customer.getNewspapers().contains(newspaper);

		final Collection<Article> articles = this.articleService.getAllFinalByNewspaper(newspaper);

		res = new ModelAndView("newspaper/display");
		res.addObject("newspaper", newspaper);
		res.addObject("articles", articles);
		res.addObject("subscriber", subscriber);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Newspaper> newspapers = this.newspaperService.findAllPublished();

		result = new ModelAndView("newspaper/list");

		result.addObject("newspapers", newspapers);
		result.addObject("actorWS", this.ACTOR_WS);

		return result;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/listSearchResults", method = RequestMethod.GET)
	public ModelAndView listSearchResults(@RequestParam final String keyword) {
		final ModelAndView res;
		final Collection<Newspaper> newspapers = this.newspaperService.findPublishedByKeyword(keyword);

		res = new ModelAndView("newspaper/list");
		res.addObject("newspapers", newspapers);

		res.addObject("actorWS", this.ACTOR_WS);

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
