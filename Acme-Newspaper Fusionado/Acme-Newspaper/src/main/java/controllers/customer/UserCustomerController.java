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
import services.UserService;
import controllers.AbstractController;
import domain.Article;
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


	// A-level requirements -------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {
		final ModelAndView res;

		final User userToDisplay = this.userService.findOne(userId);
		Assert.notNull(userToDisplay);

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(customer);

		final Collection<Article> publicArticles = this.articleService.getPublishedAndPublicByWriter(userToDisplay);
		final Collection<Article> privateArticles = this.articleService.getSuscribedByWriterAndCustomer(userToDisplay, customer);

		res = new ModelAndView("user/display");
		res.addObject("user", userToDisplay);
		res.addObject("publicArticles", publicArticles);
		res.addObject("privateArticles", privateArticles);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;

	}

	//v1.0 - Implemented by Alicia
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView res;

		final Collection<User> usersToList = this.userService.findAll();
		Assert.notNull(usersToList);

		res = new ModelAndView("user/list");
		res.addObject("users", usersToList);
		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
}
