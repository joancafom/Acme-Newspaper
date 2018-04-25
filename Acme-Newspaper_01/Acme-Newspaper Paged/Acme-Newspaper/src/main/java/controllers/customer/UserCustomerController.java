/*
 * UserController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.customer;

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
import services.ChirpService;
import services.CustomerService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.Chirp;
import domain.Customer;
import domain.User;

@Controller
@RequestMapping("/user/customer")
public class UserCustomerController extends AbstractController {

	private final String	ACTOR_WS	= "customer/";

	// Services -------------------------------------------
	@Autowired
	private ArticleService	articleService;

	@Autowired
	private CustomerService	customerService;

	@Autowired
	private UserService		userService;

	@Autowired
	private ChirpService	chirpService;


	// A-level requirements -------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId, @RequestParam(value = "d-5707915-p", defaultValue = "1") final Integer publicsPage, @RequestParam(value = "d-580417-p", defaultValue = "1") final Integer privatesPage, @RequestParam(
		value = "d-147820-p", defaultValue = "1") final Integer chirpsPage) {
		final ModelAndView res;

		final User userToDisplay = this.userService.findOne(userId);
		Assert.notNull(userToDisplay);

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(customer);

		final Page<Article> publicArticles = this.articleService.getPublishedAndPublicByWriter(userToDisplay, publicsPage, 5);
		final int publicsSize = new Long(publicArticles.getTotalElements()).intValue();

		final Page<Article> privateArticles = this.articleService.getSuscribedByWriterAndCustomer(userToDisplay, customer, privatesPage, 5);
		final int privatesSize = new Long(privateArticles.getTotalElements()).intValue();

		final Page<Chirp> myChirps = this.chirpService.getChirpsByUser(userToDisplay, chirpsPage, 5);
		final Integer chirpsSize = new Long(myChirps.getTotalElements()).intValue();

		res = new ModelAndView("user/display");
		res.addObject("user", userToDisplay);
		res.addObject("publicArticles", publicArticles);
		res.addObject("publicsSize", publicsSize);
		res.addObject("privateArticles", privateArticles);
		res.addObject("privatesSize", privatesSize);
		res.addObject("chirps", myChirps);
		res.addObject("chirpsSize", chirpsSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;

	}

	//v1.0 - Implemented by Alicia
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-49809-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Page<User> usersToList = this.userService.findAll(page, 5);
		final Integer resultSize = new Long(usersToList.getTotalElements()).intValue();
		Assert.notNull(usersToList);

		res = new ModelAndView("user/list");
		res.addObject("users", usersToList);
		res.addObject("resultSize", resultSize);
		res.addObject("landing", "list");

		res.addObject("actorWS", this.ACTOR_WS);

		return res;

	}
}
