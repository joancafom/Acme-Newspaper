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
import controllers.AbstractController;
import domain.Advertisement;
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
	// v2.0 - Updated by JA (ads)
	// v3.0 - Updated by Alicia
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId, @RequestParam(value = "d-4004458-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;
		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);

		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);

		if (article.getMainArticle() == null) {
			Assert.isTrue(article.getPublicationDate() != null || (this.advertisementService.getAdvertisementsPerNewspaperAndAgent(article.getNewspaper(), agent) > 0 && article.getIsFinal()));
			Assert.isTrue(article.getNewspaper().getIsPublic());
		} else {
			Assert.isTrue(article.getMainArticle().getPublicationDate() != null || (this.advertisementService.getAdvertisementsPerNewspaperAndAgent(article.getMainArticle().getNewspaper(), agent) > 0 && article.getMainArticle().getIsFinal()));
			Assert.isTrue(article.getMainArticle().getNewspaper().getIsPublic());
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

}
