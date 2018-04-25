/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	// Services ------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;


	// Display Dashboard ---------------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping("/display-dashboard")
	public ModelAndView dashboard() {
		final ModelAndView res;
		res = new ModelAndView("administrator/display-dashboard");

		// C-Level Requirements

		res.addObject("avgNewspapersPerUser", this.administratorService.getAvgNewspapersPerUser());
		res.addObject("stdNewspapersPerUser", this.administratorService.getStdNewspapersPerUser());
		res.addObject("avgArticlesPerWriter", this.administratorService.getAvgArticlesPerWriter());
		res.addObject("stdArticlesPerWriter", this.administratorService.getStdArticlesPerWriter());
		res.addObject("avgArticlesPerNewspaper", this.administratorService.getAvgArticlesPerNewspaper());
		res.addObject("stdArticlesPerNewspaper", this.administratorService.getStdArticlesPerNewspaper());
		res.addObject("newspapers10MoreArticlesThanAverage", this.administratorService.getNewspapers10MoreArticlesThanAverage());
		res.addObject("newspapers10FewerArticlesThanAverage", this.administratorService.getNewspapers10FewerArticlesThanAverage());
		res.addObject("ratioUsersHaveCreatedANewspaper", this.administratorService.getRatioUsersHaveCreatedANewspaper());
		res.addObject("ratioUsersHaveWrittenAnArticle", this.administratorService.getRatioUsersHaveWrittenAnArticle());

		// B-Level Requirements

		res.addObject("avgFollowUpsPerArticle", this.administratorService.getAvgFollowUpsPerArticle());
		res.addObject("avgFollowUpsPerArticleOneWeek", this.administratorService.getAvgFollowUpsPerArticleOneWeek());
		res.addObject("avgFollowUpsPerArticleTwoWeeks", this.administratorService.getAvgFollowUpsPerArticleTwoWeeks());
		res.addObject("avgChirpsPerUser", this.administratorService.getAvgChirpsPerUser());
		res.addObject("stdChirpsPerUser", this.administratorService.getStdChirpsPerUser());
		res.addObject("ratioUsersAbove75AvgChirps", this.administratorService.getRatioUsersAbove75AvgChirps());

		// A-Level Requirements
		res.addObject("ratioPublicVSPrivateNewspapers", this.administratorService.getRatioPublicVSPrivateNewspapers());
		res.addObject("avgArticlesPerPrivateNewspaper", this.administratorService.getAvgArticlesPerPrivateNewspaper());
		res.addObject("avgArticlesPerPublicNewspaper", this.administratorService.getAvgArticlesPerPublicNewspaper());
		res.addObject("ratioSubscribersVSTotalNumberCustomers", this.administratorService.getRatioSubscribersVSTotalNumberCustomers());
		res.addObject("avgRatioPrivateVSPublicNewspapersPerPublisher", this.administratorService.getAvgRatioPrivateVSPublicNewspapersPerPublisher());

		res.addObject("actorWS", "administrator/");

		return res;
	}
}
