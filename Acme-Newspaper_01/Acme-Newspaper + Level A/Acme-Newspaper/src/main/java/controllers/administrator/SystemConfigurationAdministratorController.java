/*
 * ArticleAdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigurationService;
import controllers.AbstractController;

@Controller
@RequestMapping("/systemConfiguration/administrator")
public class SystemConfigurationAdministratorController extends AbstractController {

	// Services -------------------------------------------------

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// B-Level Requirements -------------------------------------

	/* v1.0 - josembel */
	@RequestMapping(value = "/listTabooWords", method = RequestMethod.GET)
	public ModelAndView listTabooWords() {
		final ModelAndView result;
		final Collection<String> tabooWords = this.systemConfigurationService.getTabooWords();

		result = new ModelAndView("systemConfiguration/list");
		result.addObject("tabooWords", tabooWords);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/addTabooWord", method = RequestMethod.GET)
	public ModelAndView addTabooWord() {
		final ModelAndView result;

		result = this.createEditModelAndView(null);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/deleteTabooWord", method = RequestMethod.GET)
	public ModelAndView deleteTabooWord(@RequestParam final String tabooWord) {
		ModelAndView result;

		try {
			this.systemConfigurationService.deleteTabooWord(tabooWord);
			result = new ModelAndView("redirect:/systemConfiguration/administrator/listTabooWords.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/systemConfiguration/administrator/listTabooWords.do");
			result.addObject("message", "systemConfiguration.commit.error");
		}

		return result;

	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editTabooWord(@RequestParam final String tabooWord) {
		ModelAndView res;

		try {
			this.systemConfigurationService.addTabooWord(tabooWord);
			res = new ModelAndView("redirect:/systemConfiguration/administrator/listTabooWords.do");
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(tabooWord, "systemConfiguration.commit.error");
		}

		return res;
	}

	protected ModelAndView createEditModelAndView(final String tabooWord) {
		return this.createEditModelAndView(tabooWord, null);
	}

	private ModelAndView createEditModelAndView(final String tabooWord, final String message) {
		final ModelAndView result = new ModelAndView("systemConfiguration/edit");
		result.addObject("tabooWord", tabooWord);
		result.addObject("message", message);
		return result;
	}
}
