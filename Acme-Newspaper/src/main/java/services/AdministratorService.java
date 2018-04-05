
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;
import domain.Newspaper;

@Service
@Transactional
public class AdministratorService {

	// Managed Repository ------------------------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;


	// CRUD Methods ------------------------------------------------------

	// Other Business Process --------------------------------------------

	public Administrator findByUserAccount(final UserAccount userAccount) {
		return this.administratorRepository.findByUserAccount(userAccount.getId());
	}

	// C-Level Requirements  ----------------------------

	// v1.0 - Implemented by Alicia
	public Double getAvgNewspapersPerUser() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgNewspapersPerUser();
	}

	// v1.0 - Implemented by Alicia
	public Double getStdNewspapersPerUser() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdNewspapersPerUser();
	}

	// v1.0 - Implemented by Alicia
	public Double getAvgArticlesPerWriter() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgArticlesPerWriter();
	}

	// v1.0 - Implemented by Alicia
	public Double getStdArticlesPerWriter() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdArticlesPerWriter();
	}

	// v1.0 - Implemented by Alicia
	public Double getAvgArticlesPerNewspaper() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgArticlesPerNewspaper();
	}

	// v1.0 - Implemented by Alicia
	public Double getStdArticlesPerNewspaper() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdArticlesPerNewspaper();
	}

	// v1.0 - Implemented by Alicia
	public Collection<Newspaper> getNewspapers10MoreArticlesThanAverage() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.getNewspapers10MoreArticlesThanAverage();
	}

	// v1.0 - Implemented by Alicia
	public Collection<Newspaper> getNewspapers10FewerArticlesThanAverage() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.getNewspapers10FewerArticlesThanAverage();
	}

	// v1.0 - Implemented by Alicia
	public Double getRatioUsersHaveCreatedANewspaper() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.ratioUsersHaveCreatedANewspaper();
	}

	// v1.0 - Implemented by Alicia
	public Double getRatioUsersHaveWrittenAnArticle() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.ratioUsersHaveWrittenAnArticle();
	}
}
