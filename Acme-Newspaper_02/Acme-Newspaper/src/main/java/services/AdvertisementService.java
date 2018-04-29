
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdvertisementRepository;
import security.LoginService;
import domain.Administrator;
import domain.Advertisement;
import domain.Agent;
import domain.CreditCard;
import domain.Newspaper;

@Service
@Transactional
public class AdvertisementService {

	// Managed Repository
	@Autowired
	private AdvertisementRepository		advertisementRepository;

	// Supporting Services

	@Autowired
	private AgentService				agentService;

	@Autowired
	private NewspaperService			newspaperService;

	@Autowired
	private AdministratorService		administratorService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	// Validator -------------------------------------------------------------

	@Autowired
	private Validator					validator;


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

	// v1.0 - Implemented by Alicia
	public void delete(final Advertisement advertisement) {
		Assert.notNull(advertisement);

		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		for (final Newspaper n : advertisement.getNewspapers()) {
			n.getAdvertisements().remove(advertisement);
			this.newspaperService.save(n);
		}

		this.advertisementRepository.delete(advertisement);
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

	// v1.0 - Implemented by Alicia
	public Collection<Advertisement> findAll() {
		return this.advertisementRepository.findAll();
	}

	// v1.0 - Implemented by Alicia
	public Page<Advertisement> findAll(final int page, final int size) {
		return this.advertisementRepository.findAllPag(new PageRequest(page - 1, size));
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
	// v2.0 - Modified by JA
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

		this.save(advertisement);
		this.newspaperService.save(newspaper);

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

	/* v1.0 - josembell */
	public Page<Advertisement> findTabooedAdvertisements(final int page, final int size) {
		return this.advertisementRepository.findTabooedAdvertisements(new PageRequest(page - 1, size));
	}

	/* v1.0 - josembell */
	public Collection<Advertisement> getNotTabooed() {
		return this.advertisementRepository.findNotTabooed();
	}

	/* v1.0 - josembell */
	public Page<Advertisement> findAdvertisementsByAgent(final int page, final int size) {
		final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(agent);
		final Page<Advertisement> adverts = this.advertisementRepository.findAdvertisementsByAgent(new PageRequest(page - 1, size), agent.getId());
		return adverts;
	}

	// v1.0 - Implemented by Alicia
	public Advertisement reconstruct(final Advertisement prunedAdvertisement, final BindingResult binding) {
		final Advertisement res;

		Assert.notNull(prunedAdvertisement);
		res = this.findOne(prunedAdvertisement.getId());

		Assert.notNull(res);
		this.validator.validate(res, binding);

		return res;
	}

	/* v1.0 - josembell */
	public Advertisement saveTaboo(final Advertisement advertisement) {
		Assert.notNull(advertisement);

		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		//Check for taboo words
		final Boolean containsTabooVeredict = this.systemConfigurationService.containsTaboo(advertisement.getTitle());
		advertisement.setContainsTaboo(containsTabooVeredict);

		return this.advertisementRepository.save(advertisement);
	}

	/* v1.0 - josembell */
	public void flush() {
		this.advertisementRepository.flush();
	}

}
