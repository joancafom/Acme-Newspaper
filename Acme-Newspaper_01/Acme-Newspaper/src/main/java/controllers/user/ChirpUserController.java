
package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import controllers.AbstractController;
import domain.Chirp;

@Controller
@RequestMapping("/chirp/user")
public class ChirpUserController extends AbstractController {

	/* Services */
	@Autowired
	private ChirpService	chirpService;


	/* v1.0 - josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Chirp chirp = this.chirpService.create();

		result = this.createEditModelAndView(chirp);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/stream", method = RequestMethod.GET)
	public ModelAndView stream() {
		final ModelAndView result;
		final Collection<Chirp> chirps = this.chirpService.getStream();

		result = new ModelAndView("chirp/stream");
		result.addObject("chirps", chirps);
		result.addObject("actorWS", "user/");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final Chirp chirp, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(chirp);
		else
			try {

				this.chirpService.save(chirp);
				res = new ModelAndView("redirect:/chirp/user/stream.do");

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(chirp, "chirp.commit.error");
			}

		return res;
	}

	/* v1.0 - josembell */
	protected ModelAndView createEditModelAndView(final Chirp chirp) {
		return this.createEditModelAndView(chirp, null);
	}

	/* v1.0 - josembell */
	private ModelAndView createEditModelAndView(final Chirp chirp, final String message) {
		ModelAndView result;
		result = new ModelAndView("chirp/edit");
		result.addObject("chirp", chirp);
		result.addObject("message", message);
		return result;
	}

}
