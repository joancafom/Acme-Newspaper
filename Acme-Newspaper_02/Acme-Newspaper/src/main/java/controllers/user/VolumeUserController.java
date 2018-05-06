/*
 * VolumeUserController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.user;

import java.util.Collection;
import java.util.List;

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
import services.NewspaperService;
import services.UserService;
import services.VolumeService;
import controllers.AbstractController;
import domain.Newspaper;
import domain.User;
import domain.Volume;
import forms.ManageVolumeForm;

@Controller
@RequestMapping("/volume/user")
public class VolumeUserController extends AbstractController {

	private final String		ACTOR_WS	= "user/";

	//Services
	@Autowired
	private VolumeService		volumeService;

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private UserService			userService;


	//Level C Requirements

	//v1.0 - Implemented by JA
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int volumeId, @RequestParam(value = "d-1332308-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		final Volume volume = this.volumeService.findOne(volumeId);
		Assert.notNull(volume);

		final Page<Newspaper> pageResult = this.newspaperService.findByVolume(volume, page, 5);
		final Collection<Newspaper> newspapers = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		final User publisher = this.userService.findPublisherByVolume(volume);

		res = new ModelAndView("volume/display");
		res.addObject("volume", volume);
		res.addObject("newspapers", newspapers);
		res.addObject("resultSize", resultSize);
		res.addObject("publisher", publisher);
		if (user.getVolumes().contains(volume))
			res.addObject("mine", true);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}
	//v1.0 - Implemented by JA
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-448844-p", defaultValue = "1") final Integer page) {

		final ModelAndView res;

		final Page<Volume> pageResult = this.volumeService.findAll(page, 5);
		final List<Volume> volumes = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("volume/list");
		res.addObject("resultSize", resultSize);
		res.addObject("volumes", volumes);

		res.addObject("actorWS", this.ACTOR_WS);

		return res;
	}

	// Level B Requirements

	/* v1.0 -josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Volume volume = this.volumeService.create();

		result = this.createEditModelAndView(volume);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Volume volume, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(volume);
		else
			try {

				this.volumeService.save(volume);
				res = new ModelAndView("redirect:/volume/user/list.do");

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(volume, "volume.commit.error");
			}

		return res;
	}

	/* v1.0 -josembell */
	@RequestMapping(value = "/addNewspaper", method = RequestMethod.GET)
	public ModelAndView addNewspaper(@RequestParam final int volumeId) {
		final ModelAndView result;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		final Volume volume = this.volumeService.findOne(volumeId);
		Assert.notNull(volume);
		Assert.isTrue(user.getVolumes().contains(volume));

		final ManageVolumeForm form = new ManageVolumeForm();
		form.setVolume(volume);

		result = this.manageModelAndView(form);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/addNewspaper", method = RequestMethod.POST, params = "add")
	public ModelAndView addNewspaper(@Valid final ManageVolumeForm form, final BindingResult binding) {
		ModelAndView res;
		if (binding.hasErrors())
			res = this.manageModelAndView(form);
		else
			try {
				this.volumeService.addNewspaper(form.getVolume(), form.getNewspaper());
				res = new ModelAndView("redirect:/volume/user/display.do?volumeId=" + form.getVolume().getId());

			} catch (final Throwable oops) {
				res = this.manageModelAndView(form, "volume.commit.error");
			}

		return res;
	}
	/* v1.0 -josembell */
	@RequestMapping(value = "/removeNewspaper", method = RequestMethod.GET)
	public ModelAndView removeNewspaper(@RequestParam final int volumeId, @RequestParam final int newspaperId) {
		ModelAndView result;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		final Volume volume = this.volumeService.findOne(volumeId);
		Assert.notNull(volume);
		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);
		Assert.isTrue(user.getNewspapers().contains(newspaper));
		Assert.isTrue(user.getVolumes().contains(volume));

		try {
			this.volumeService.removeNewspaper(volume, newspaper);
			result = new ModelAndView("redirect:/volume/user/display.do?volumeId=" + volume.getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/volume/user/display.do?volumeId=" + volume.getId());
			result.addObject("message", "volume.commit.error");
		}

		return result;
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Volume volume) {
		return this.createEditModelAndView(volume, null);
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Volume volume, final String message) {
		ModelAndView result;
		result = new ModelAndView("volume/edit");
		result.addObject("volume", volume);
		result.addObject("message", message);

		return result;
	}

	/* v1.0 - josembell */
	protected ModelAndView manageModelAndView(final ManageVolumeForm form) {
		return this.manageModelAndView(form, null);
	}

	/* v1.0 - josembell */
	protected ModelAndView manageModelAndView(final ManageVolumeForm form, final String message) {
		ModelAndView result;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		result = new ModelAndView("volume/manage");
		result.addObject("addNewspaper", true);
		result.addObject("manageVolumeForm", form);
		result.addObject("newspapers", this.newspaperService.findNewspapersYetToBeIncludedInVolume(form.getVolume()));
		result.addObject("message", message);

		if (user.getNewspapers().isEmpty())
			result.addObject("noNewspaperCreated", true);
		if (form.getVolume().getNewspapers().containsAll(user.getNewspapers()) && !user.getNewspapers().isEmpty())
			result.addObject("noMoreNewspapers", true);
		else if (!form.getVolume().getNewspapers().containsAll(user.getNewspapers()))
			result.addObject("noMoreNewspapers", false);

		return result;
	}
}
