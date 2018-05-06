/*
 * VolumeSubscriptionCustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.customer;

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
import services.VolumeService;
import services.VolumeSubscriptionService;
import controllers.AbstractController;
import domain.Customer;
import domain.Volume;
import domain.VolumeSubscription;

@Controller
@RequestMapping("/volumeSubscription/customer")
public class VolumeSubscriptionCustomerController extends AbstractController {

	// Services -------------------------------------------------

	@Autowired
	private CustomerService				customerService;

	@Autowired
	private VolumeService				volumeService;

	@Autowired
	private VolumeSubscriptionService	volumeSubscriptionService;


	// B-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int volumeId) {
		final ModelAndView res;

		final Volume volume = this.volumeService.findOne(volumeId);
		Assert.notNull(volume);

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(customer);

		Assert.isTrue(!this.volumeSubscriptionService.hasVolumeSubscriptionVolume(customer, volume));

		final VolumeSubscription volumeSubscription = this.volumeSubscriptionService.create(volume);

		res = this.createEditModelAndView(volumeSubscription);

		return res;
	}

	//v1.0 - Implemented by Alicia
	/* v2.0 - josembell -> pruned */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final VolumeSubscription prunedVolumeSubscription, final BindingResult binding) {
		ModelAndView res;

		final VolumeSubscription volumeSubscription = this.volumeSubscriptionService.reconstruct(prunedVolumeSubscription, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(volumeSubscription);
		else
			try {

				this.volumeSubscriptionService.save(volumeSubscription);
				res = new ModelAndView("redirect:/volume/customer/display.do?volumeId=" + volumeSubscription.getVolume().getId());

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(volumeSubscription, "volumeSubscription.commit.error");
			}

		return res;
	}

	//Ancillary Methods -------------------------------

	//v1.0 - Implemented by Alicia
	protected ModelAndView createEditModelAndView(final VolumeSubscription volumeSubscription) {
		final ModelAndView res;

		res = this.createEditModelAndView(volumeSubscription, null);

		return res;
	}

	//v1.0 - Implemented by Alicia
	protected ModelAndView createEditModelAndView(final VolumeSubscription volumeSubscription, final String message) {
		final ModelAndView res;

		res = new ModelAndView("volumeSubscription/edit");
		res.addObject("volumeSubscription", volumeSubscription);
		res.addObject("message", message);

		return res;
	}

}
