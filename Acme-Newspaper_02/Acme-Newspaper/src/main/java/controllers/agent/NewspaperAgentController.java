/*
 * NewspaperAgentController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.agent;

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
import services.AgentService;
import services.ArticleService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Agent;
import domain.Article;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/newspaper/agent")
public class NewspaperAgentController extends AbstractController {

	private final String			ACTOR_WS	= "agent/";

	// Services -------------------------------------------------

	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private AgentService			agentService;

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private UserService				userService;


	// C-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	// v2.0 - Updated by JA (canDisplay)
	// v3.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);

		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);

		final User writer = this.userService.getWriterByNewspaper(newspaper);

		//She or he can display the articles if it is published or has an associated advertisement campaign of a public newspaper.
		final boolean canDisplay = newspaper.getIsPublic() && (newspaper.getPublicationDate() != null || this.advertisementService.getAdvertisementsPerNewspaperAndAgent(newspaper, agent) > 0);

		final Page<Article> pageResult = this.articleService.getAllFinalByNewspaper(newspaper, page, 5);
		final Collection<Article> articles = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("newspaper/display");
		res.addObject("newspaper", newspaper);
		res.addObject("articles", articles);
		res.addObject("resultSize", resultSize);
		res.addObject("canDisplay", canDisplay);
		res.addObject("writer", writer);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/listAdvertised", method = RequestMethod.GET)
	public ModelAndView listAdvertised(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;

		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);

		final Page<Newspaper> pageResult = this.newspaperService.getAdvertised(agent, page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("newspaper/list");

		result.addObject("newspapers", newspapers);
		result.addObject("resultSize", resultSize);
		result.addObject("actorWS", this.ACTOR_WS);
		result.addObject("landing", "listAdvertised");

		return result;
	}

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/listNotAdvertised", method = RequestMethod.GET)
	public ModelAndView listNotAdvertised(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;

		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);

		final Page<Newspaper> pageResult = this.newspaperService.getNotAdvertised(agent, page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("newspaper/list");

		res.addObject("newspapers", newspapers);
		res.addObject("resultSize", resultSize);
		res.addObject("actorWS", this.ACTOR_WS);
		res.addObject("landing", "listNotAdvertised");

		return res;
	}
}
