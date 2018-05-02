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
import org.springframework.data.domain.Page;
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
import services.SubscriptionService;
import services.UserService;
import services.VolumeSubscriptionService;
import controllers.AbstractController;
import domain.Article;
import domain.Customer;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/newspaper/customer")
public class NewspaperCustomerController extends AbstractController {

	private final String				ACTOR_WS	= "customer/";

	// Services -------------------------------------------------

	@Autowired
	private ArticleService				articleService;

	@Autowired
	private CustomerService				customerService;

	@Autowired
	private NewspaperService			newspaperService;

	@Autowired
	private SubscriptionService			subscriptionService;

	@Autowired
	private UserService					userService;

	@Autowired
	private VolumeSubscriptionService	volumeSubscriptionService;


	// A-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by Alicia (AN2)
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);
		Assert.notNull(newspaper.getPublicationDate());

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(customer);

		final Boolean subscriber = this.subscriptionService.hasSubscription(customer, newspaper) || this.volumeSubscriptionService.hasVolumeSubscriptionNewspaper(customer, newspaper);

		final User writer = this.userService.getWriterByNewspaper(newspaper);

		final Page<Article> pageResult = this.articleService.getAllFinalByNewspaper(newspaper, page, 5);
		final Collection<Article> articles = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("newspaper/display");
		res.addObject("newspaper", newspaper);
		res.addObject("articles", articles);
		res.addObject("subscriber", subscriber);
		res.addObject("resultSize", resultSize);
		res.addObject("writer", writer);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;

		final Page<Newspaper> pageResult = this.newspaperService.findAllPublished(page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("newspaper/list");

		result.addObject("newspapers", newspapers);
		result.addObject("resultSize", resultSize);
		result.addObject("actorWS", this.ACTOR_WS);
		result.addObject("landing", "list");

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

	// v1.0 Implemented by Alicia
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView res;

		res = new ModelAndView("newspaper/search");

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
