
package controllers.agent;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AdvertisementService;
import services.AgentService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Agent;
import domain.Newspaper;
import forms.AdvertiseForm;

@Controller
@RequestMapping("/advertisement/agent")
public class AdvertisementAgentController extends AbstractController {

	private final String			ACTOR_WS	= "agent/";

	/* Services */
	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private AgentService			agentService;


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
	public ModelAndView advertise(@Valid final AdvertiseForm form, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.advertiseModelAndView(form);
		else
			try {
				this.advertisementService.advertise(form.getNewspaper(), form.getAdvertisement());
				res = new ModelAndView("redirect:/newspaper/agent/display.do?newspaperId=" + form.getNewspaper().getId());

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
				res = new ModelAndView("redirect:/advertisement/agent/list.do");

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(advertisement, "advertisement.commit.error");
			}

		return res;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-4281171-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;
		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);

		final Page<Advertisement> pageResult = this.advertisementService.findAdvertisementsByAgent(page, 5);
		final Collection<Advertisement> advertisements = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("advertisement/list");
		result.addObject("advertisements", advertisements);
		result.addObject("resultSize", resultSize);
		result.addObject("landing", "list");
		result.addObject("actorWS", this.ACTOR_WS);

		return result;
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

		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);

		result = new ModelAndView("advertisement/advertise");
		result.addObject("advertiseForm", form);
		result.addObject("message", message);
		final Collection<Advertisement> advertisements = this.advertisementService.findAdvertisementsYetToAdvertInNewspaper(form.getNewspaper());
		result.addObject("advertisements", advertisements);
		if (agent.getAdvertisements().isEmpty())
			result.addObject("noAdvertsCreated", true);
		if (form.getNewspaper().getAdvertisements().containsAll(agent.getAdvertisements()) && !agent.getAdvertisements().isEmpty())
			result.addObject("noMoreAdverts", true);
		else if (!form.getNewspaper().getAdvertisements().containsAll(agent.getAdvertisements()))
			result.addObject("noMoreAdverts", false);

		return result;
	}
}
