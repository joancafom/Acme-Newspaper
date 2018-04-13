/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NewspaperService;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper")
public class NewspaperController extends AbstractController {

	/* Services */
	@Autowired
	private NewspaperService	newspaperService;


	/* Level C Requirements */

	/* v1.0 - josembell */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId) {
		ModelAndView result;
		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);
		Assert.isTrue(newspaper.getPublicationDate() != null);

		result = new ModelAndView("newspaper/display");
		result.addObject("newspaper", newspaper);
		result.addObject("articles", newspaper.getArticles());
		result.addObject("resultSize", newspaper.getArticles().size());

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/listPublished", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;

		final Page<Newspaper> pageResult = this.newspaperService.findAllPublished(page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = pageResult.getTotalPages() * 5;

		result = new ModelAndView("newspaper/list");
		result.addObject("resultSize", resultSize);
		result.addObject("newspapers", newspapers);
		result.addObject("landing", "listPublished");

		return result;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/listSearchResults", method = RequestMethod.GET)
	public ModelAndView listSearchResults(@RequestParam final String keyword, @RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		final ModelAndView res;

		final Page<Newspaper> pageResult = this.newspaperService.findPublishedByKeyword(keyword, page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = pageResult.getTotalPages() * 5;

		res = new ModelAndView("newspaper/list");
		res.addObject("newspapers", newspapers);
		res.addObject("resultSize", resultSize);
		res.addObject("landing", "listSearchResults");

		return res;
	}

	// v1.0 Implemented by Alicia
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView res;

		res = new ModelAndView("newspaper/search");

		return res;
	}

}
