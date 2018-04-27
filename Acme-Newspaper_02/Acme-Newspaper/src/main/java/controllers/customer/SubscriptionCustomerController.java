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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CustomerService;
import services.NewspaperService;
import services.SubscriptionService;
import services.VolumeSubscriptionService;
import controllers.AbstractController;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;

@Controller
@RequestMapping("/subscription/customer")
public class SubscriptionCustomerController extends AbstractController {

	// Services -------------------------------------------------

	@Autowired
	private CustomerService				customerService;

	@Autowired
	private NewspaperService			newspaperService;

	@Autowired
	private SubscriptionService			subscriptionService;

	@Autowired
	private VolumeSubscriptionService	volumeSubscriptionService;


	// A-Level Requirements -------------------------------------

	//v1.0 - Implemented by JA
	// v2.0 - Updated by Alicia (AN2)
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int newspaperId) {

		final ModelAndView res;

		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);
		Assert.isTrue(!newspaper.getIsPublic());
		Assert.notNull(newspaper.getPublicationDate());

		final Customer subscriber = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(subscriber);

		final Subscription subscription = this.subscriptionService.create(newspaper);
		Assert.isTrue(!this.subscriptionService.hasSubscription(subscriber, newspaper));
		Assert.isTrue(!this.volumeSubscriptionService.hasVolumeSubscriptionNewspaper(subscriber, newspaper));

		res = this.createEditModelAndView(subscription);

		return res;

	}
	//v1.0 - Implemented by JA
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Subscription subscription, final BindingResult binding) {

		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(subscription);
		else
			try {

				this.subscriptionService.save(subscription);
				res = new ModelAndView("redirect:/newspaper/customer/display.do?newspaperId=" + subscription.getNewspaper().getId());

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(subscription, "subscription.commit.error");
			}

		return res;

	}

	//Ancillary Methods -------------------------------

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final Subscription subscription) {

		final ModelAndView res;

		res = this.createEditModelAndView(subscription, null);

		return res;

	}

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final Subscription subscription, final String message) {

		final ModelAndView res;

		res = new ModelAndView("subscription/edit");
		res.addObject("subscription", subscription);
		res.addObject("message", message);

		return res;
	}

}
