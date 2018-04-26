/*
 * UserAgentController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AgentService;
import services.ArticleService;
import services.ChirpService;
import services.UserService;
import controllers.AbstractController;
import domain.Agent;
import domain.Article;
import domain.Chirp;
import domain.User;

@Controller
@RequestMapping("/user/agent")
public class UserAgentController extends AbstractController {

	private final String	ACTOR_WS	= "agent/";

	//Services --------------------------------------------

	@Autowired
	private AgentService	agentService;

	@Autowired
	private ArticleService	articleService;

	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private UserService		userService;


	// C-level requirements -------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer articlesPage, @RequestParam(value = "d-147820-p", defaultValue = "1") final Integer chirpsPage) {
		final ModelAndView res;

		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);

		final User userToDisplay = this.userService.findOne(userId);
		Assert.notNull(userToDisplay);

		final Page<Article> publicArticles = this.articleService.getPublishedAndPublicByWriter(userToDisplay, articlesPage, 5);
		final int resultSize = new Long(publicArticles.getTotalElements()).intValue();

		final Page<Chirp> myChirps = this.chirpService.getChirpsByUser(userToDisplay, chirpsPage, 5);
		final Integer chirpsSize = new Long(myChirps.getTotalElements()).intValue();

		res = new ModelAndView("user/display");
		res.addObject("user", userToDisplay);
		res.addObject("publicArticles", publicArticles);
		res.addObject("resultSize", resultSize);
		res.addObject("chirps", myChirps);
		res.addObject("chirpsSize", chirpsSize);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;

	}
}
