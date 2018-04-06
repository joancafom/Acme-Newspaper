
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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

	//Supporting Services
	@Autowired
	private UserService			userService;

	@Autowired
	private Validator			validator;


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
	public Newspaper save(final Newspaper newspaperToSave) {

		Assert.notNull(newspaperToSave);

		final User publisher = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(publisher);

		if (newspaperToSave.getId() != 0)
			Assert.isTrue(publisher.getNewspapers().contains(newspaperToSave));

		return this.newspaperRepository.save(newspaperToSave);

	}

	// Other Business Methods -------------------------------

	// C-Level Requirements  ----------------------------

	//v1.0 - Implemented by JA
	public Boolean canBePublished(final Newspaper newspaperToPublish, final User publisher) {

		Boolean res = true;

		Assert.notNull(newspaperToPublish);
		Assert.notNull(publisher);
		Assert.notNull(publisher.getNewspapers());
		Assert.isTrue(publisher.getNewspapers().contains(newspaperToPublish));

		for (final Article a : newspaperToPublish.getArticles())
			if (!a.getIsFinal()) {
				res = false;
				break;
			}

		return res;
	}

	// v1.0 - Implemented by Alicia
	public Collection<Newspaper> findPublishedByKeyword(final String keyword) {
		return this.newspaperRepository.findPublishedByKeyword(keyword);
	}

		//v1.0 - Implemented by JA
	public Newspaper publish(final Newspaper newspaperToPublish) {

		Assert.notNull(newspaperToPublish);
		final User publisher = this.userService.findByUserAccount(LoginService.getPrincipal());

		final Boolean publishVeredict = this.canBePublished(newspaperToPublish, publisher);

		if (!publishVeredict)
			throw new RuntimeException("All articles must be in final mode for a Newspaper to be published");

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
