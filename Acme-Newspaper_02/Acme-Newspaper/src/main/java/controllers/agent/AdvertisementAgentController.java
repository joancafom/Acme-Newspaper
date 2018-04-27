
package controllers.agent;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Advertisement;

@Controller
@RequestMapping("/advertisement/agent")
public class AdvertisementAgentController extends AbstractController {

	private final String			ACTOR_WS	= "agent/";

	/* Services */
	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private NewspaperService		newspaperService;


	// C-Level Requirements -------------------------------------

	/* v1.0 - josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Advertisement advertisement = this.advertisementService.create();

		result = this.createEditModelAndView(advertisement);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView save(@Valid final Advertisement advertisement, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(advertisement);
		else
			try {

				this.advertisementService.save(advertisement);
				res = new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(advertisement, "advertisement.commit.error");
			}

		return res;
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Advertisement advertisement) {
		ModelAndView result;
		result = this.createEditModelAndView(advertisement, null);
		return result;
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Advertisement advertisement, final String message) {
		ModelAndView result;

		result = new ModelAndView("advertisement/edit");
		result.addObject("advertisement", advertisement);
		result.addObject("message", message);
		result.addObject("newspapers", this.newspaperService.findAll());
		return result;
	}
}
