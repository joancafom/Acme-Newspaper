
package controllers.agent;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Newspaper;
import forms.AdvertiseForm;

@Controller
@RequestMapping("/advertisement/agent")
public class AdvertisementAgentController extends AbstractController {

	//private final String			ACTOR_WS	= "agent/";

	/* Services */
	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private NewspaperService		newspaperService;


	// C-Level Requirements -------------------------------------

	/* v1.0 - josembell */
	@RequestMapping(value = "/advertise", method = RequestMethod.GET)
	public ModelAndView advertise(@RequestParam final int newspaperId) {
		ModelAndView result = null;
		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);

		final AdvertiseForm form = new AdvertiseForm();
		form.setNewspaper(newspaper);

		result = this.advertiseModelAndView(form);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/advertise", method = RequestMethod.POST, params = "advertise")
	public ModelAndView advertise(final AdvertiseForm form, final BindingResult binding) {
		ModelAndView res;

		try {
			this.advertisementService.advertise(form.getNewspaper(), this.advertisementService.findOne(form.getAdvertisementId()));
			res = new ModelAndView("redirect:/");

		} catch (final Throwable oops) {
			res = this.advertiseModelAndView(form, "advertisement.commit.error");
		}

		return res;
	}
	/* v1.0 - josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Advertisement advertisement = this.advertisementService.create();

		result = this.createEditModelAndView(advertisement);
		result.addObject("toCreate", true);

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

	/* v1.0 - josembell */
	protected ModelAndView advertiseModelAndView(final AdvertiseForm form) {
		return this.advertiseModelAndView(form, null);
	}

	/* v1.0 - josembell */
	protected ModelAndView advertiseModelAndView(final AdvertiseForm form, final String message) {
		ModelAndView result;
		result = new ModelAndView("advertisement/advertise");
		result.addObject("advertiseForm", form);
		result.addObject("message", message);
		final Collection<Advertisement> advertisements = this.advertisementService.findAdvertisementsYetToAdvertInNewspaper(form.getNewspaper());
		result.addObject("advertisements", advertisements);

		return result;
	}
}
