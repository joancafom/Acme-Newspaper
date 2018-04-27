
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdvertisementRepository;
import security.LoginService;
import domain.Advertisement;
import domain.Agent;
import domain.CreditCard;
import domain.Newspaper;

@Service
@Transactional
public class AdvertisementService {

	// Managed Repository
	@Autowired
	private AdvertisementRepository	advertisementRepository;

	// Supporting Services

	@Autowired
	private AgentService			agentService;


	// CRUD Methods -------------------------------

	/* v1.0 - josembell */
	public Advertisement create() {
		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);

		final Advertisement advertisement = new Advertisement();
		advertisement.setAgent(agent);
		advertisement.setContainsTaboo(false);
		advertisement.setNewspapers(new HashSet<Newspaper>());
		advertisement.setCreditCard(new CreditCard());

		return advertisement;
	}

	/* v1.0 - josembell */
	public Advertisement save(final Advertisement advertisement) {
		Assert.notNull(advertisement);
		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);
		Assert.isTrue(advertisement.getAgent().equals(agent));

		final LocalDate now = new LocalDate();
		Assert.notNull(advertisement.getCreditCard());

		// Assert (year == current && month == current) || year == future || (year == current && month == future)
		Assert.isTrue((now.getYear() == advertisement.getCreditCard().getYear() && now.getMonthOfYear() == advertisement.getCreditCard().getMonth()) || (now.getYear() < advertisement.getCreditCard().getYear())
			|| (now.getYear() == advertisement.getCreditCard().getYear() && now.getMonthOfYear() < advertisement.getCreditCard().getMonth()));

		final Advertisement saved = this.advertisementRepository.save(advertisement);
		if (advertisement.getId() == 0)
			agent.getAdvertisements().add(saved);

		return saved;
	}

	/* v1.0 - josembell */
	public Advertisement findOne(final int advertisementId) {
		return this.advertisementRepository.findOne(advertisementId);
	}

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

	/* v1.0 - josembell */
	public void advertise(final Newspaper newspaper, final Advertisement advertisement) {
		Assert.notNull(newspaper);
		Assert.notNull(advertisement);
		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);
		Assert.isTrue(agent.getAdvertisements().contains(advertisement));
		Assert.isTrue(advertisement.getAgent().equals(agent));
		Assert.isTrue(!advertisement.getNewspapers().contains(newspaper));
		Assert.isTrue(!newspaper.getAdvertisements().contains(advertisement));

		newspaper.getAdvertisements().add(advertisement);
		advertisement.getNewspapers().add(newspaper);
	}

	/* v1.0 - josembell */
	public Collection<Advertisement> findAdvertisementsYetToAdvertInNewspaper(final Newspaper newspaper) {
		Assert.notNull(newspaper);
		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);

		return this.advertisementRepository.findAdvertisementsYetToAdvertInNewspaper(newspaper.getId(), agent.getId());

	}

	/* v1.0 - josembell */
	public Collection<Advertisement> findTabooedAdvertisements() {
		return this.advertisementRepository.findTabooedAdvertisements();
	}

}
