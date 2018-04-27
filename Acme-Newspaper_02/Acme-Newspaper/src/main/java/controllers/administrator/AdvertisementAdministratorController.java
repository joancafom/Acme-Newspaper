
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import controllers.AbstractController;
import domain.Advertisement;

@Controller
@RequestMapping("/advertisement/administrator")
public class AdvertisementAdministratorController extends AbstractController {

	//private final String			ACTOR_WS	= "administrator/";

	/* Services */
	@Autowired
	private AdvertisementService	advertisementService;


	// C-Level Requirements -------------------------------------

	/* v1.0 - josembell */
	// v2.0 - Updated by Alicia
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listTabooed(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		ModelAndView result = null;

		final Page<Advertisement> pageResult = this.advertisementService.findTabooedAdvertisements(page, 5);
		final Collection<Advertisement> advertisements = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("advertisement/list");
		result.addObject("advertisements", advertisements);
		result.addObject("resultSize", resultSize);
		result.addObject("landing", "list");

		return result;
	}
	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		ModelAndView res = null;

		final Page<Advertisement> pageResult = this.advertisementService.findAll(page, 5);
		final Collection<Advertisement> advertisements = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		res = new ModelAndView("advertisement/list");
		res.addObject("advertisements", advertisements);
		res.addObject("resultSize", resultSize);
		res.addObject("landing", "listAll");

		return res;
	}

	// B-Level Requirements -------------------------------------

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int advertisementId) {
		final ModelAndView res;

		final Advertisement advertisement = this.advertisementService.findOne(advertisementId);
		Assert.notNull(advertisement);

		res = new ModelAndView("advertisement/delete");
		res.addObject("advertisement", advertisement);

		return res;
	}

	// v1.0 - Implemented by Alicia
	@RequestMapping(value = "/delete", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Advertisement prunedAdvertisement, final BindingResult binding) {
		ModelAndView res;

		final Advertisement advertisement = this.advertisementService.reconstruct(prunedAdvertisement, binding);
		Assert.notNull(advertisement);

		try {
			this.advertisementService.delete(advertisement);
			res = new ModelAndView("redirect:/advertisement/administrator/list.do");
		} catch (final Throwable oops) {
			res = new ModelAndView("advertisement/delete");
			res.addObject("advertisement", prunedAdvertisement);
			res.addObject("message", "advertisement.commit.error");
		}

		return res;
	}

}
