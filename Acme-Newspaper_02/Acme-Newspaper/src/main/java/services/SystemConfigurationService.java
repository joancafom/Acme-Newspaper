
package services;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SystemConfigurationRepository;
import security.LoginService;
import domain.Administrator;
import domain.Advertisement;
import domain.Article;
import domain.Chirp;
import domain.Newspaper;
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {

	/* Repositories */

	@Autowired
	private SystemConfigurationRepository	systemConfigurationRepository;

	// Supporting Services -----------------------------------

	@Autowired
	private AdministratorService			adminService;

	@Autowired
	private ArticleService					articleService;

	@Autowired
	private ChirpService					chirpService;

	@Autowired
	private NewspaperService				newspaperService;

	@Autowired
	private AdvertisementService			advertisementService;


	/* CRUD Methods */

	public SystemConfiguration create() {

		//v1.0 - Implemented by JA

		final SystemConfiguration sysConfig = new SystemConfiguration();

		return sysConfig;
	}

	public SystemConfiguration findOne(final int systemConfigurationId) {
		//v1.0 - Implemented by JA

		return this.systemConfigurationRepository.findOne(systemConfigurationId);
	}

	public Collection<SystemConfiguration> findAll() {
		//v1.0 - Implemented by JA

		return this.systemConfigurationRepository.findAll();
	}

	public SystemConfiguration save(final SystemConfiguration sC) {

		//v1.0 - Implemented by JA

		Assert.notNull(sC);

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Collection<SystemConfiguration> allSCs = this.findAll();

		//Guarantee the uniqueness
		if (allSCs != null)
			for (final SystemConfiguration sysConfig : allSCs)
				if (!sysConfig.equals(sC))
					this.delete(sysConfig);

		return this.systemConfigurationRepository.save(sC);
	}

	public void delete(final SystemConfiguration sC) {

		//v1.0 - Implemented by JA

		Assert.notNull(sC);

		this.systemConfigurationRepository.delete(sC);
	}

	//Other Business Methods

	//v1.0 - Implemented by JA
	public Boolean containsTaboo(final String testString) {
		Boolean res;

		Assert.notNull(testString);
		final SystemConfiguration sysConfig = this.getCurrentSystemConfiguration();
		Assert.notNull(sysConfig);

		final Pattern p = Pattern.compile(sysConfig.getTabooWords().toLowerCase());

		if (p.toString() != "") {
			final Matcher veredict = p.matcher(testString.toLowerCase());
			res = veredict.find();
		} else
			res = false;

		return res;
	}

	// v1.0 - Implemented by Alicia
	public void flush() {
		this.systemConfigurationRepository.flush();
	}

	public SystemConfiguration getCurrentSystemConfiguration() {

		//v1.0 - Implemented by JA

		//Theoretically there is only one SystemConfiguration in our system, so a findAll operation
		//is not an overhead

		final Collection<SystemConfiguration> allSysConfig = this.findAll();
		SystemConfiguration res;

		res = allSysConfig == null ? null : allSysConfig.iterator().next();

		return res;
	}

	/* v1.0 - josembell */
	public Collection<String> getTabooWords() {
		final SystemConfiguration sysConfig = this.getCurrentSystemConfiguration();
		final String tabooWords = sysConfig.getTabooWords();
		final String[] tabooWordsSplitted = tabooWords.split("\\|");
		final Collection<String> collectionTabooWords;
		if (tabooWords.equals("") || tabooWords == null)
			collectionTabooWords = new HashSet<String>();
		else {
			final List<String> listTabooWords = Arrays.asList(tabooWordsSplitted);
			collectionTabooWords = new HashSet<String>(listTabooWords);
		}

		return collectionTabooWords;

	}
	/* v1.0 - josembell */
	// v2.0 - Modified by JA
	public String addTabooWord(final String tabooWordIn) {

		final String tabooWord = tabooWordIn.toLowerCase();
		Assert.notNull(tabooWord);
		Assert.isTrue(!tabooWord.equals(""));
		Assert.isTrue(!this.getTabooWords().contains(tabooWord));
		Assert.isTrue(!tabooWord.contains("|"));

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final SystemConfiguration sysConfig = this.getCurrentSystemConfiguration();
		Assert.notNull(sysConfig);
		String tabooWords = sysConfig.getTabooWords();

		if (tabooWords.equals("") || tabooWords == null)
			tabooWords = tabooWord;
		else
			tabooWords = tabooWords + "|" + tabooWord;

		sysConfig.setTabooWords(tabooWords);
		this.save(sysConfig);

		final Collection<Chirp> tabooChirps = this.chirpService.findNotTabooedChirps();
		final Collection<Article> tabooArticles = this.articleService.findNotTabooedArticles();
		final Collection<Newspaper> tabooNewspapers = this.newspaperService.getNotTabooed();
		final Collection<Advertisement> tabooAdvertisements = this.advertisementService.getNotTabooed();

		for (final Chirp chirp : tabooChirps)
			this.chirpService.saveTaboo(chirp);

		for (final Article article : tabooArticles)
			this.articleService.saveTaboo(article);

		for (final Newspaper newspaper : tabooNewspapers)
			this.newspaperService.saveTaboo(newspaper);

		for (final Advertisement advertisement : tabooAdvertisements)
			this.advertisementService.saveTaboo(advertisement);

		return tabooWords;
	}

	/* v1.0 - josembell */
	// v2.0 - Updated by Alicia
	public String deleteTabooWord(final String tabooWord) {
		Assert.notNull(tabooWord);
		Assert.isTrue(this.getTabooWords().contains(tabooWord));

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final SystemConfiguration sysConfig = this.getCurrentSystemConfiguration();
		Assert.notNull(sysConfig);
		String tabooWords = sysConfig.getTabooWords();

		tabooWords = "|" + tabooWords + "|";

		tabooWords = tabooWords.replaceAll("\\|" + tabooWord + "\\|", "|");

		if (tabooWords.equals("|"))
			tabooWords = "";
		else
			tabooWords = tabooWords.substring(1, tabooWords.length() - 1);

		sysConfig.setTabooWords(tabooWords);
		this.save(sysConfig);

		final Collection<Chirp> tabooChirps = this.chirpService.findTabooedChirps();
		final Collection<Article> tabooArticles = this.articleService.findTabooedArticles();
		final Collection<Newspaper> tabooNewspapers = this.newspaperService.getTabooed();
		final Collection<Advertisement> tabooAdvertisements = this.advertisementService.findTabooedAdvertisements();

		for (final Chirp chirp : tabooChirps)
			this.chirpService.saveTaboo(chirp);

		for (final Article article : tabooArticles)
			this.articleService.saveTaboo(article);

		for (final Newspaper newspaper : tabooNewspapers)
			this.newspaperService.saveTaboo(newspaper);

		for (final Advertisement advertisement : tabooAdvertisements)
			this.advertisementService.saveTaboo(advertisement);

		return tabooWords;
	}
}
