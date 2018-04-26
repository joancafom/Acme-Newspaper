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
import services.AgentService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Agent;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/agent")
public class NewspaperAgentController extends AbstractController {

	private final String		ACTOR_WS	= "agent/";

	// Services -------------------------------------------------

	@Autowired
	private AgentService		agentService;

	@Autowired
	private NewspaperService	newspaperService;


	// C-Level Requirements -------------------------------------

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
	public ModelAndView listWithAdverts(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;

		res = new ModelAndView("newspaper/list");

		return res;
	}
}
