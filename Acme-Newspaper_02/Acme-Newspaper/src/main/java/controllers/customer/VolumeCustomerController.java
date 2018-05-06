/*
 * VolumeCustomerController.java
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
import services.CustomerService;
import services.NewspaperService;
import services.UserService;
import services.VolumeService;
import services.VolumeSubscriptionService;
import controllers.AbstractController;
import domain.Customer;
import domain.Newspaper;
import domain.User;
import domain.Volume;

@Controller
@RequestMapping("/volume/customer")
public class VolumeCustomerController extends AbstractController {

	private final String				ACTOR_WS	= "customer/";

	//Services

	@Autowired
	private CustomerService				customerService;

	@Autowired
	private NewspaperService			newspaperService;

	@Autowired
	private VolumeService				volumeService;

	@Autowired
	private VolumeSubscriptionService	volumeSubscriptionService;

	@Autowired
	private UserService					userService;


	//Level C Requirements

	//v1.0 - Implemented by JA
	// v2.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int volumeId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;

		final Volume volume = this.volumeService.findOne(volumeId);
		Assert.notNull(volume);

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());

		final Page<Newspaper> pageResult = this.newspaperService.findPublishedByVolume(volume, page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		final Boolean subscriber = this.volumeSubscriptionService.hasVolumeSubscriptionVolume(customer, volume);

		final User publisher = this.userService.findPublisherByVolume(volume);

		res = new ModelAndView("volume/display");
		res.addObject("volume", volume);
		res.addObject("publisher", publisher);
		res.addObject("newspapers", newspapers);
		res.addObject("resultSize", resultSize);
		res.addObject("subscriber", subscriber);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
	//v1.0 - Implemented by JA
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-448844-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;

		final Page<Volume> pageResult = this.volumeService.findAll(page, 5);
		final Collection<Volume> volumes = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("volume/list");
		res.addObject("resultSize", resultSize);
		res.addObject("volumes", volumes);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

}
