/*
 * UserController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CustomerService;
import domain.Customer;
import forms.CustomerRegistrationForm;

@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractController {

	//private final String	ACTOR_WS	= "";

	//Services
	@Autowired
	private CustomerService	customerService;


	//C-level requirements -------------------------------

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {

		final ModelAndView res;

		final CustomerRegistrationForm newCustomerForm = new CustomerRegistrationForm();
		res = this.createEditModelAndView(newCustomerForm);

		return res;

	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final CustomerRegistrationForm customerRegistrationForm, final BindingResult binding) {

		ModelAndView res;
		final Customer customerToSave;

		customerToSave = this.customerService.reconstruct(customerRegistrationForm, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(customerRegistrationForm);
		else
			try {

				Assert.isTrue(customerRegistrationForm.getAcceptedTerms());
				Assert.isTrue(customerRegistrationForm.getPassword().equals(customerRegistrationForm.getPasswordConfirmation()));

				this.customerService.save(customerToSave);

				res = new ModelAndView("welcome/index");

			} catch (final DataIntegrityViolationException oops) {
				res = this.createEditModelAndView(customerRegistrationForm, "customer.userAccount.username.duplicated");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(customerRegistrationForm, "customer.commit.error");
			}

		return res;

	}

	//Ancillary Methods -------------------------------

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final CustomerRegistrationForm customerRegistrationForm) {

		final ModelAndView res;

		res = this.createEditModelAndView(customerRegistrationForm, null);

		return res;

	}

	//v1.0 - Implemented by JA
	protected ModelAndView createEditModelAndView(final CustomerRegistrationForm customerRegistrationForm, final String message) {

		final ModelAndView res;

		res = new ModelAndView("customer/register");
		res.addObject("customerRegistrationForm", customerRegistrationForm);
		res.addObject("message", message);

		return res;
	}
}
