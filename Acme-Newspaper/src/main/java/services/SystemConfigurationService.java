
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
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {

	/* Repositories */

	@Autowired
	private SystemConfigurationRepository	systemConfigurationRepository;

	@Autowired
	private AdministratorService			adminService;


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

		Assert.notNull(testString);
		final SystemConfiguration sysConfig = this.getCurrentSystemConfiguration();
		Assert.notNull(sysConfig);

		final Pattern p = Pattern.compile(sysConfig.getTabooWords().toLowerCase());
		final Matcher veredict = p.matcher(testString.toLowerCase());

		return veredict.find();
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
	public String addTabooWord(final String tabooWord) {
		Assert.isTrue(!this.getTabooWords().contains(tabooWord));
		Assert.isTrue(!tabooWord.contains("|"));

		final SystemConfiguration sysConfig = this.getCurrentSystemConfiguration();
		String tabooWords = sysConfig.getTabooWords();

		if (tabooWords.equals("") || tabooWords == null)
			tabooWords = tabooWords + tabooWord;
		else
			tabooWords = tabooWords + "|" + tabooWord;

		sysConfig.setTabooWords(tabooWords);
		this.save(sysConfig);

		return tabooWords;
	}

	/* v1.0 - josembell */
	public String deleteTabooWord(final String tabooWord) {
		Assert.isTrue(this.getTabooWords().contains(tabooWord));
		Assert.notNull(tabooWord);

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

		return tabooWords;
	}
}
