/*
 * ArticleAgentController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.agent;

import org.springframework.beans.factory.annotation.Autowired;
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
import controllers.AbstractController;
import domain.Agent;
import domain.Article;

@Controller
@RequestMapping("/article/agent")
public class ArticleAgentController extends AbstractController {

	private final String			ACTOR_WS	= "agent/";

	// Services -------------------------------------------------

	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private AgentService			agentService;

	@Autowired
	private ArticleService			articleService;


	// C-Level Requirements

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		final ModelAndView res;
		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);

		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);

		Assert.isTrue(article.getPublicationDate() != null || (this.advertisementService.getAdvertisementsPerNewspaperAndAgent(article.getNewspaper(), agent) > 0 && article.getIsFinal()));
		Assert.isTrue(article.getNewspaper().getIsPublic());

		res = new ModelAndView("article/display");
		res.addObject("article", article);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

}
