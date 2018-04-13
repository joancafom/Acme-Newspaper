/*
 * ChirpAdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import controllers.AbstractController;
import domain.Chirp;

@Controller
@RequestMapping("/chirp/administrator")
public class ChirpAdministratorController extends AbstractController {

	// Services -------------------------------------------------

	@Autowired
	private ChirpService	chirpService;


	// B-Level Requirements -------------------------------------

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int chirpId) {

		final ModelAndView res;

		final Chirp chirp = this.chirpService.findOne(chirpId);
		Assert.notNull(chirp);

		res = new ModelAndView("chirp/delete");
		res.addObject("chirp", chirp);

		return res;
	}

	// v1.0 - Implemented by JA
	@RequestMapping(value = "/delete", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Chirp chirp, final BindingResult binding) {

		ModelAndView res;

		try {
			this.chirpService.delete(chirp);
			res = new ModelAndView("redirect:/chirp/administrator/list.do");
		} catch (final Throwable oops) {
			res = new ModelAndView("chirp/delete");
			res.addObject("chirp", chirp);
			res.addObject("message", "chirp.commit.error");
		}

		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-147820-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;

		final Page<Chirp> chirps = this.chirpService.findTabooedChirps(page, 5);
		final Integer resultSize = new Long(chirps.getTotalElements()).intValue();

		result = new ModelAndView("chirp/stream");
		result.addObject("chirps", chirps);
		result.addObject("actorWS", "administrator/");
		result.addObject("resultSize", resultSize);
		result.addObject("landing", "list");

		return result;
	}

}
