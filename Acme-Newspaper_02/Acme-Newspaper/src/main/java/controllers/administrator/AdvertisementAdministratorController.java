
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	public ModelAndView listTabooed() {
		ModelAndView result = null;
		final Collection<Advertisement> advertisements = this.advertisementService.findTabooedAdvertisements();

		result = new ModelAndView("advertisement/list");
		result.addObject("advertisements", advertisements);

		return result;
	}

}
