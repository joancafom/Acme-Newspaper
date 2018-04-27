
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
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
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listTabooed(@RequestParam(value = "d-3664915-p", defaultValue = "1") final Integer page) {
		ModelAndView result = null;

		final Page<Advertisement> pageResult = this.advertisementService.findTabooedAdvertisements(page, 5);
		final Collection<Advertisement> advertisements = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("advertisement/list");
		result.addObject("advertisements", advertisements);
		result.addObject("resultSize", resultSize);

		return result;
	}

}
