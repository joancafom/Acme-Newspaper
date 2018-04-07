
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.NewspaperRepository;
import security.LoginService;
import domain.Article;
import domain.Newspaper;
import domain.User;

@Service
@Transactional
public class NewspaperService {

	/* Managed Repository */
	@Autowired
	private NewspaperRepository	newspaperRepository;

	@Autowired
	private Validator			validator;

	@Autowired
	private UserService			userService;


	/* v1.0 - josembell */
	public Newspaper create() {
		//final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		final Newspaper newspaper = new Newspaper();

		newspaper.setArticles(new HashSet<Article>());

		return newspaper;
	}

	//v1.0 - Implemented by JA
	/*v2.0 - updated by josembell */
	public Newspaper save(final Newspaper newspaperToSave) {

		Assert.notNull(newspaperToSave);

		final User publisher = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(publisher);

		if (newspaperToSave.getId() != 0)
			Assert.isTrue(publisher.getNewspapers().contains(newspaperToSave));

		final Newspaper savedNewspaper = this.newspaperRepository.save(newspaperToSave);

		publisher.getNewspapers().add(savedNewspaper);

		return savedNewspaper;
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

	/* v1.0 - josembell */
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
	// Other Business Process --------------------------------------------

	// C-Level Requirements  ----------------------------

	// v1.0 - Implemented by Alicia
	public Collection<Newspaper> findPublishedByKeyword(final String keyword) {
		return this.newspaperRepository.findPublishedByKeyword(keyword);
	}

	//v2.0 - Implemented by JA
	public Boolean canBePublished(final Newspaper newspaperToPublish, final User publisher) {

		Boolean res = true;

		Assert.notNull(newspaperToPublish);
		Assert.notNull(publisher);
		Assert.notNull(publisher.getNewspapers());
		Assert.isTrue(publisher.getNewspapers().contains(newspaperToPublish));
		Assert.isNull(newspaperToPublish.getPublicationDate());

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

}
