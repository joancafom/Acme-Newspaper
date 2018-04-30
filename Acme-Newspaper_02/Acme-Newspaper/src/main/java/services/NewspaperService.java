
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.NewspaperRepository;
import security.LoginService;
import domain.Administrator;
import domain.Advertisement;
import domain.Agent;
import domain.Article;
import domain.Newspaper;
import domain.User;
import domain.Volume;

@Service
@Transactional
public class NewspaperService {

	/* Managed Repository */
	@Autowired
	private NewspaperRepository			newspaperRepository;

	//Supporting Services
	@Autowired
	private UserService					userService;

	@Autowired
	private AgentService				agentService;

	@Autowired
	private AdministratorService		adminService;

	@Autowired
	private ArticleService				articleService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private Validator					validator;


	/* v1.0 - josembell */
	// v2.0 - Updated by Alicia
	// v3.0 - Updated by JA (Suscription)
	// v4.0 - Updated by Alicia (AN2)
	public Newspaper create() {
		//final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		final Newspaper newspaper = new Newspaper();

		newspaper.setArticles(new HashSet<Article>());

		newspaper.setAdvertisements(new HashSet<Advertisement>());
		newspaper.setVolumes(new HashSet<Volume>());

		return newspaper;
	}

	/* v1.0 - josembell */
	public Collection<Newspaper> findAll() {
		return this.newspaperRepository.findAll();
	}

	/* v1.0 - josembell */
	public Page<Newspaper> findAll(final int page, final int size) {
		return this.newspaperRepository.findAllPag(new PageRequest(page - 1, size));
	}

	/* v1.0 - josembell */
	public Collection<Newspaper> findAllPublished() {
		return this.newspaperRepository.findAllPublished();
	}

	// v1.0 - Implemented by JA
	public Page<Newspaper> findAllPublished(final int page, final int size) {
		return this.newspaperRepository.findAllPublished(new PageRequest(page - 1, size));
	}

	/* v1.0 - josembell */
	public Newspaper findOne(final int newspaperId) {
		return this.newspaperRepository.findOne(newspaperId);
	}

	//v1.0 - Implemented by JA
	/* v2.0 - updated by josembell */
	// v3.0 - Updated by JA (taboo)
	// v4.0 - Updated by JA (agent)
	// v5.0 - Updated by JA (admin)
	public Newspaper save(final Newspaper newspaperToSave) {

		Assert.notNull(newspaperToSave);

		final User publisher = this.userService.findByUserAccount(LoginService.getPrincipal());

		//A newspaper can be created/updated by a publisher, but also updated (add Advert) by an Agent
		//If the publisher is null then we must ensure it's an agent who's performing the save. In addition,
		// an Admin may remove advertisements to it and hence perform a save too.
		if (publisher != null) {
			if (newspaperToSave.getId() != 0)
				Assert.isTrue(publisher.getNewspapers().contains(newspaperToSave));
		} else {
			Assert.isTrue(newspaperToSave.getId() != 0);
			final Agent agent = this.agentService.findByUserAccount(LoginService.getPrincipal());
			final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());

			Assert.isTrue(admin != null || agent != null);

			final Newspaper oldNewspaper = this.findOne(newspaperToSave.getId());

			Assert.notNull(oldNewspaper);
			Assert.isTrue(oldNewspaper.getIsPublic() == newspaperToSave.getIsPublic());
			Assert.isTrue(oldNewspaper.getDescription().equals(newspaperToSave.getDescription()));
			Assert.isTrue(oldNewspaper.getPicture().equals(newspaperToSave.getPicture()));
		}

		//Check for taboo words
		final Boolean containsTabooVeredict = this.systemConfigurationService.containsTaboo(newspaperToSave.getTitle() + " " + newspaperToSave.getDescription());
		newspaperToSave.setContainsTaboo(containsTabooVeredict);

		final Newspaper savedNewspaper = this.newspaperRepository.save(newspaperToSave);

		if (newspaperToSave.getId() == 0 && publisher != null)
			publisher.getNewspapers().add(savedNewspaper);

		return savedNewspaper;

	}
	// v1.0 - Implemented by Alicia
	public Newspaper saveTaboo(final Newspaper newspaper) {
		Assert.notNull(newspaper);

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		//Check for taboo words
		final Boolean containsTabooVeredict = this.systemConfigurationService.containsTaboo(newspaper.getTitle() + " " + newspaper.getDescription());
		newspaper.setContainsTaboo(containsTabooVeredict);

		return this.newspaperRepository.save(newspaper);
	}

	// v1.0 - Implemented by JA
	public void delete(final Newspaper newspaper) {

		Assert.notNull(newspaper);

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		for (final Article a : new HashSet<Article>(newspaper.getArticles()))
			this.articleService.delete(a);

		final User publisher = this.userService.getPublisher(newspaper);
		Assert.notNull(publisher);

		publisher.getNewspapers().remove(newspaper);

		this.newspaperRepository.delete(newspaper);

	}

	// Other Business Methods -------------------------------

	// C-Level Requirements  ----------------------------

	//v2.0 - Implemented by JA
	public Boolean canBePublished(final Newspaper newspaperToPublish, final User publisher) {

		Boolean res = true;

		Assert.notNull(newspaperToPublish);
		Assert.notNull(publisher);
		Assert.notNull(publisher.getNewspapers());
		Assert.isTrue(publisher.getNewspapers().contains(newspaperToPublish));
		Assert.isNull(newspaperToPublish.getPublicationDate());
		Assert.isTrue(!newspaperToPublish.getArticles().isEmpty());

		for (final Article a : newspaperToPublish.getArticles())
			if (!a.getIsFinal()) {
				res = false;
				break;
			}

		return res;
	}

	// v1.0 - Implemented by Alicia
	public Collection<Newspaper> findAllUnpublished() {
		return this.newspaperRepository.findAllUnpublished();
	}

	// v1.0 - Implemented by Alicia
	public Page<Newspaper> findByPublisher(final User publisher, final int page, final int size) {

		Assert.notNull(publisher);

		return this.newspaperRepository.findByPublisherId(publisher.getId(), new PageRequest(page - 1, size));
	}

	// v1.0 - Implemented by JA
	public Page<Newspaper> findAllUnpublished(final int page, final int size) {
		return this.newspaperRepository.findAllUnpublished(new PageRequest(page - 1, size));
	}

	// v1.0 - Implemented by Alicia
	public Collection<Newspaper> findPublishedByKeyword(final String keyword) {
		return this.newspaperRepository.findPublishedByKeyword(keyword);
	}

	// v1.0 - Implemented by JA
	public Page<Newspaper> findPublishedByKeyword(final String keyword, final int page, final int size) {
		return this.newspaperRepository.findPublishedByKeyword(keyword, new PageRequest(page - 1, size));
	}

	// v1.0 - Implemented by Alicia
	public void flush() {
		this.newspaperRepository.flush();
	}

	//v2.0 - Implemented by JA
	public Newspaper publish(final Newspaper newspaperToPublish) {

		Assert.notNull(newspaperToPublish);
		final User publisher = this.userService.findByUserAccount(LoginService.getPrincipal());

		final Boolean publishVeredict = this.canBePublished(newspaperToPublish, publisher);

		if (!publishVeredict)
			throw new RuntimeException("All articles must be in final mode for a Newspaper to be published");

		final Date nowMinusMillis = new Date(System.currentTimeMillis() - 1000L);
		newspaperToPublish.setPublicationDate(nowMinusMillis);

		return this.save(newspaperToPublish);
	}
	//v1.0 - Implemented by JA
	public Newspaper reconstructPruned(final Newspaper prunedNewspaper, final BindingResult binding) {

		Newspaper res;

		Assert.notNull(prunedNewspaper);
		res = this.findOne(prunedNewspaper.getId());

		Assert.notNull(res);
		this.validator.validate(res, binding);

		return res;

	}

	/* v1.0 - josembell */
	// v2.0 - Updated by Alicia
	// v3.0 - Updated by Alicia (AN2)
	public Newspaper reconstruct(final Newspaper newspaper, final BindingResult binding) {
		if (newspaper.getId() == 0) {
			newspaper.setArticles(new HashSet<Article>());

			newspaper.setAdvertisements(new HashSet<Advertisement>());
			newspaper.setVolumes(new HashSet<Volume>());
			this.validator.validate(newspaper, binding);
		} else {
			final Newspaper oldNewspaper = this.findOne(newspaper.getId());
			newspaper.setArticles(oldNewspaper.getArticles());

			newspaper.setAdvertisements(oldNewspaper.getAdvertisements());
			newspaper.setVolumes(oldNewspaper.getVolumes());
			this.validator.validate(newspaper, binding);
		}
		return newspaper;
	}
	// B-Level Requirements  ----------------------------

	// v1.0 - Implemented by Alicia
	public Collection<Newspaper> getTabooed() {
		return this.newspaperRepository.findTabooed();
	}

	// v1.0 - Implemented by Alicia
	public Page<Newspaper> getTabooed(final int page, final int size) {
		return this.newspaperRepository.findTabooed(new PageRequest(page - 1, size));
	}

	// v1.0 - Implemented by Alicia
	public Collection<Newspaper> getNotTabooed() {
		return this.newspaperRepository.findNotTabooed();
	}

	// A-Level Requirements ----------------------------

	// v1.0 - Implemented by JA
	public Newspaper privatize(final Newspaper newspaperToPrivatize) {

		Assert.notNull(newspaperToPrivatize);

		newspaperToPrivatize.setIsPublic(false);

		return this.save(newspaperToPrivatize);

	}

	// v1.0 - Implemented by JA
	public Newspaper unprivatize(final Newspaper newspaperToUnprivatize) {

		Assert.notNull(newspaperToUnprivatize);
		Assert.isNull(newspaperToUnprivatize.getPublicationDate());

		newspaperToUnprivatize.setIsPublic(true);
		return this.save(newspaperToUnprivatize);

	}

	// Acme-Newspaper 2.0 ---------------------------------------------------

	//There is no equivalent for Collection as you can access directly from .getNewspapers()
	// v1.0 - Implemented by JA
	public Page<Newspaper> findByVolume(final Volume volume, final int page, final int size) {
		Assert.notNull(volume);

		final Page<Newspaper> res = this.newspaperRepository.findByVolume(volume.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Implemented by JA
	public Collection<Newspaper> findPublishedByVolume(final Volume volume) {

		Assert.notNull(volume);

		final Collection<Newspaper> res = this.newspaperRepository.findPublishedByVolume(volume.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Implemented by JA
	public Page<Newspaper> findPublishedByVolume(final Volume volume, final int page, final int size) {
		Assert.notNull(volume);

		final Page<Newspaper> res = this.newspaperRepository.findPublishedByVolume(volume.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Implemented by Alicia
	public Collection<Newspaper> getAdvertised(final Agent agent) {
		Assert.notNull(agent);

		final Collection<Newspaper> res = this.newspaperRepository.findAdvertised(agent.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Implemented by Alicia
	public Page<Newspaper> getAdvertised(final Agent agent, final int page, final int size) {
		Assert.notNull(agent);

		final Page<Newspaper> res = this.newspaperRepository.findAdvertised(agent.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Implemented by JA
	public Collection<Newspaper> getNotAdvertised(final Agent agent) {
		Assert.notNull(agent);

		final Collection<Newspaper> res = this.newspaperRepository.findNotAdvertised(agent.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Implemented by JA
	public Page<Newspaper> getNotAdvertised(final Agent agent, final int page, final int size) {
		Assert.notNull(agent);

		final Page<Newspaper> res = this.newspaperRepository.findNotAdvertised(agent.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	/* v1.0 - josembell */
	public Collection<Newspaper> findNewspapersYetToBeIncludedInVolume(final Volume volume) {
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		Assert.notNull(volume);
		Assert.isTrue(user.getVolumes().contains(volume));

		return this.newspaperRepository.findNewspapersYetToBeIncludedInVolume(volume.getId(), user.getId());
	}

}
