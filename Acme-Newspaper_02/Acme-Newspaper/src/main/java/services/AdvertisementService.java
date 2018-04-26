
package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdvertisementRepository;
import domain.Advertisement;
import domain.Agent;
import domain.Newspaper;

@Service
@Transactional
public class AdvertisementService {

	// Managed Repository
	@Autowired
	private AdvertisementRepository	advertisementRepository;


	// Supporting Services

	// CRUD Methods -------------------------------

	// Other Business Methods -------------------------------

	// v1.0 - Implemented by Alicia
	public Integer getAdvertisementsPerNewspaperAndAgent(final Newspaper newspaper, final Agent agent) {
		Assert.notNull(newspaper);
		Assert.notNull(agent);

		final Integer res = this.advertisementRepository.advertisementsPerNewspaperAndAgent(newspaper.getId(), agent.getId());
		Assert.notNull(res);

		return res;
	}

	//v1.0 - Implemented by JA
	public Advertisement getRandomAdvertisement(final Newspaper newspaper) {

		Advertisement res = null;

		Assert.notNull(newspaper);

		final List<Advertisement> ads = new ArrayList<Advertisement>(newspaper.getAdvertisements());

		if (!ads.isEmpty()) {
			Collections.shuffle(ads);
			res = ads.iterator().next();
		}

		return res;

	}

}
