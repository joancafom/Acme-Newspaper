
package services;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import repositories.AgentRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.ANMessage;
import domain.Advertisement;
import domain.Agent;
import forms.ActorRegistrationForm;

@Service
@Transactional
public class AgentService {

	// Managed Repository
	@Autowired
	private AgentRepository		agentRepository;

	// Supporting Services

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private FolderService		folderService;

	@Autowired
	private Validator			validator;


	// CRUD Methods -------------------------------

	//v1.0 - Implemented by JA
	public Agent create() {

		final Agent res = new Agent();

		final Authority userAuthority = new Authority();
		final UserAccount userAccount = this.userAccountService.create();

		userAuthority.setAuthority(Authority.AGENT);
		userAccount.getAuthorities().add(userAuthority);
		res.setUserAccount(userAccount);

		//Common for every Actor
		res.setReceivedMessages(new ArrayList<ANMessage>());
		res.setSentMessages(new ArrayList<ANMessage>());

		//Only Agent
		res.setAdvertisements(new ArrayList<Advertisement>());
		return res;

	}

	//v1.0 - Implemented by JA
	//v2.0 - Updated by JA (folders)
	public Agent save(final Agent agent) {

		Assert.notNull(agent);
		Assert.isTrue(agent.getId() == 0);

		//Access Control

		try {

			LoginService.getPrincipal();
			throw new RuntimeException("An authenticated Actor cannot register to the system");

		} catch (final IllegalArgumentException okFlow) {
			//Intentionally left in blank
		}

		//HashPassword
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(agent.getUserAccount().getPassword(), null);
		agent.getUserAccount().setPassword(hashedPassword);

		final Agent savedAgent = this.agentRepository.save(agent);

		this.folderService.createSystemFolders(savedAgent);

		return savedAgent;

	}

	// Other Business Methods -------------------------------

	// v1.0 - Implemented by Alicia
	public Agent findByUserAccount(final UserAccount userAccount) {
		final Agent res;

		Assert.notNull(userAccount);
		res = this.agentRepository.findByUserAccountId(userAccount.getId());

		return res;
	}

	//v1.0 - Implemented by JA

	//v1.0 - Implemented by JA
	public Agent reconstruct(final ActorRegistrationForm agentRegistrationForm, final BindingResult binding) {

		final Agent res = this.create();

		res.setName(agentRegistrationForm.getName());
		res.setSurnames(agentRegistrationForm.getSurnames());
		res.setPostalAddress(agentRegistrationForm.getPostalAddress());
		res.setPhoneNumber(agentRegistrationForm.getPhoneNumber());
		res.setEmail(agentRegistrationForm.getEmail());

		this.validator.validate(res, binding);

		res.getUserAccount().setUsername(agentRegistrationForm.getUsername());
		res.getUserAccount().setPassword(agentRegistrationForm.getPassword());

		final Errors userAccountErrors = new BeanPropertyBindingResult(res.getUserAccount(), binding.getObjectName());

		this.validator.validate(res.getUserAccount(), userAccountErrors);

		binding.addAllErrors(userAccountErrors);

		return res;

	}
}
