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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NewspaperService;
import controllers.AbstractController;

@Controller
@RequestMapping("/advertisement/agent")
public class NewspaperAgentController extends AbstractController {

	// Services -------------------------------------------------

	@Autowired
	private NewspaperService	newspaperService;


	// C-Level Requirements -------------------------------------

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/listNotAdvertised", method = RequestMethod.GET)
	public ModelAndView listWithAdverts(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;

		res = new ModelAndView("newspaper/list");

		return res;
	}
}
