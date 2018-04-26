
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdvertisementRepository;
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

}
