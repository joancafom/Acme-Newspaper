
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.NewspaperRepository;
import security.LoginService;
import domain.Administrator;
import domain.Article;
import domain.Newspaper;
import domain.User;

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
	public Newspaper create() {
		//final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		final Newspaper newspaper = new Newspaper();

		newspaper.setArticles(new HashSet<Article>());

		return newspaper;
	}

	/* v1.0 - josembell */
	public Collection<Newspaper> findAll() {
		return this.newspaperRepository.findAll();
	}

	/* v1.0 - josembell */
	public Collection<Newspaper> findAllPublished() {
		return this.newspaperRepository.findAllPublished();
	}

	/* v1.0 - josembell */
	public Newspaper findOne(final int newspaperId) {
		return this.newspaperRepository.findOne(newspaperId);
	}

	//v1.0 - Implemented by JA
	/* v2.0 - updated by josembell */
	// v3.0 - Updated by JA (taboo)
	public Newspaper save(final Newspaper newspaperToSave) {

		Assert.notNull(newspaperToSave);

		final User publisher = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(publisher);

		if (newspaperToSave.getId() != 0)
			Assert.isTrue(publisher.getNewspapers().contains(newspaperToSave));

		//Check for taboo words
		final Boolean containsTabooVeredict = this.systemConfigurationService.containsTaboo(newspaperToSave.getTitle() + " " + newspaperToSave.getDescription());
		newspaperToSave.setContainsTaboo(containsTabooVeredict);

		final Newspaper savedNewspaper = this.newspaperRepository.save(newspaperToSave);

		if (newspaperToSave.getId() == 0)
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

		for (final Article a : newspaper.getArticles())
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
	public Collection<Newspaper> findPublishedByKeyword(final String keyword) {
		return this.newspaperRepository.findPublishedByKeyword(keyword);
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
	public Newspaper reconstruct(final Newspaper newspaper, final BindingResult binding) {
		if (newspaper.getId() == 0) {
			newspaper.setArticles(new HashSet<Article>());
			this.validator.validate(newspaper, binding);
		} else {
			final Newspaper oldNewspaper = this.findOne(newspaper.getId());
			newspaper.setArticles(oldNewspaper.getArticles());
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
}
