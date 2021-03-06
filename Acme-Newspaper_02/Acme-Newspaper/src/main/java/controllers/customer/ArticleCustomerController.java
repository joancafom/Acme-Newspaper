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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AdvertisementService;
import services.ArticleService;
import services.CustomerService;
import services.SubscriptionService;
import services.VolumeSubscriptionService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Article;
import domain.Customer;

@Controller
@RequestMapping("/article/customer")
public class ArticleCustomerController extends AbstractController {

	private final String				ACTOR_WS	= "customer/";

	// Services -------------------------------------------------

	@Autowired
	private AdvertisementService		advertisementService;

	@Autowired
	private ArticleService				articleService;

	@Autowired
	private CustomerService				customerService;

	@Autowired
	private SubscriptionService			subscriptionService;

	@Autowired
	private VolumeSubscriptionService	volumeSubscriptionService;


	// A-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by JA (ads)
	// v3.0 - Updated by Alicia (AN2)
	// v4.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId, @RequestParam(value = "d-4004458-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;
		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(customer);

		if (article.getMainArticle() == null) {
			Assert.isTrue(this.subscriptionService.hasSubscription(customer, article.getNewspaper()) || article.getNewspaper().getIsPublic() || this.volumeSubscriptionService.hasVolumeSubscriptionNewspaper(customer, article.getNewspaper()));
			Assert.isTrue(article.getPublicationDate() != null);
		} else {
			Assert.isTrue(this.subscriptionService.hasSubscription(customer, article.getMainArticle().getNewspaper()) || article.getMainArticle().getNewspaper().getIsPublic()
				|| this.volumeSubscriptionService.hasVolumeSubscriptionNewspaper(customer, article.getMainArticle().getNewspaper()));
			Assert.isTrue(article.getMainArticle().getPublicationDate() != null);
		}

		final Advertisement ad = this.advertisementService.getRandomAdvertisement(article.getNewspaper());

		final Page<Article> pageResult = this.articleService.getFollowUpsByArticle(article, page, 5);
		final Collection<Article> followUps = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("article/display");
		res.addObject("article", article);
		res.addObject("followUps", followUps);
		res.addObject("resultSize", resultSize);
		res.addObject("ad", ad);

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
	public ModelAndView list(@RequestParam final String keyword, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(customer);

		final Page<Article> pageResult = this.articleService.getCustomerSearchResults(keyword, customer, page, 5);
		final Collection<Article> articles = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("article/list");
		res.addObject("articles", articles);
		res.addObject("keyword", keyword);
		res.addObject("resultSize", resultSize);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
