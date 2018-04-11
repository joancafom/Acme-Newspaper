
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

	// B-Level Requirements  ----------------------------

	// v1.0 - Implemented by Alicia
	public Double getAvgFollowUpsPerArticle() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgFollowUpsPerArticle();
	}

	// v1.0 - Implemented by Alicia
	public Double getAvgFollowUpsPerArticleOneWeek() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgFollowUpsPerArticleOneWeek();
	}

	// v1.0 - Implemented by Alicia
	public Double getAvgFollowUpsPerArticleTwoWeeks() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgFollowUpsPerArticleTwoWeeks();
	}

	// v1.0 - Implemented by Alicia
	public Double getAvgChirpsPerUser() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgChirpsPerUser();
	}

	// v1.0 - Implemented by Alicia
	public Double getStdChirpsPerUser() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdChirpsPerUser();
	}

	// v1.0 - Implemented by Alicia
	public Double getRatioUsersAbove75AvgChirps() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.ratioUsersAbove75AvgChirps();
	}

	// A-Level Requirements  ----------------------------

	// v1.0 - Implemented by Alicia
	public Double getRatioPublicVSPrivateNewspapers() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.ratioPublicVSPrivateNewspapers();
	}

	// v1.0 - Implemented by Alicia
	public Double getAvgArticlesPerPrivateNewspaper() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgArticlesPerPrivateNewspaper();
	}

	// v1.0 - Implemented by Alicia
	public Double getAvgArticlesPerPublicNewspaper() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgArticlesPerPublicNewspaper();
	}

	// v1.0 - Implemented by Alicia
	public Double getRatioSubscribersVSTotalNumberCustomers() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.ratioSubscribersVSTotalNumberCustomers();
	}

	// v1.0 - Implemented by JA
	public Double getAvgRatioPrivateVSPublicNewspapersPerPublisher() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Collection<Long> ratiosPrivateVSPublicNewspapers = this.administratorRepository.ratiosPrivateVSPublicNewspapersPerPublisher();

		Double res = 0.0;

		if (!ratiosPrivateVSPublicNewspapers.isEmpty()) {

			Double accSum = 0.0;

			for (final Number n : ratiosPrivateVSPublicNewspapers)
				accSum += n.doubleValue();

			res = accSum / ratiosPrivateVSPublicNewspapers.size();
		}

		return res;
	}
}
