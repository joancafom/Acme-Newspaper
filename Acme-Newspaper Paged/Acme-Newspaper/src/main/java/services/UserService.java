
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Article;
import domain.Chirp;
import domain.Newspaper;
import domain.User;
import forms.UserRegistrationForm;

@Service
@Transactional
public class UserService {

	// Managed Repository
	@Autowired
	private UserRepository		userRepository;

	// Supporting Services

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private Validator			validator;


	// CRUD Methods -------------------------------

	//v1.0 - Implemented by JA
	public User create() {

		final User res = new User();

		final Authority userAuthority = new Authority();
		final UserAccount userAccount = this.userAccountService.create();

		userAuthority.setAuthority(Authority.USER);
		userAccount.getAuthorities().add(userAuthority);
		res.setUserAccount(userAccount);

		res.setNewspapers(new ArrayList<Newspaper>());
		res.setArticles(new ArrayList<Article>());
		res.setChirps(new ArrayList<Chirp>());
		res.setFollowers(new ArrayList<User>());
		res.setFollowees(new ArrayList<User>());

		return res;

	}

	//v1.0 - Implemented by JA
	public Collection<User> findAll() {

		return this.userRepository.findAll();
	}

	// v1.0 - Implemented by Alicia
	public Page<User> findAll(final int page, final int size) {
		return this.userRepository.findAllPag(new PageRequest(page - 1, size));
	}

	//v1.0 - Implemented by JA
	public User findOne(final int userId) {

		return this.userRepository.findOne(userId);
	}

	//v1.0 - Implemented by JA
	public User save(final User user) {

		Assert.notNull(user);
		Assert.isTrue(user.getId() == 0);

		//Access Control

		try {

			LoginService.getPrincipal();
			throw new RuntimeException("An authenticated Actor cannot register to the system");

		} catch (final IllegalArgumentException okFlow) {
			//Intentionally left in blank
		}

		//HashPassword
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(user.getUserAccount().getPassword(), null);
		user.getUserAccount().setPassword(hashedPassword);

		return this.userRepository.save(user);

	}

	// Other Business Methods -------------------------------

	//v1.0 - Implemented by JA

	public User getPublisher(final Newspaper newspaper) {

		final User res;

		Assert.notNull(newspaper);
		res = this.userRepository.findPublisherByNewspaper(newspaper);

		return res;

	}

	public User findByUserAccount(final UserAccount userAccount) {

		final User res;

		Assert.notNull(userAccount);

		res = this.userRepository.findByUserAccountId(userAccount.getId());

		return res;

	}

	// v1.0 - Implemented by Alicia
	public void flush() {
		this.userRepository.flush();
	}

	// v1.0 - Implemented by Alicia
	public void follow(final User user) {
		Assert.notNull(user);

		final User principal = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(principal);

		Assert.isTrue(!principal.getFollowees().contains(user));

		principal.getFollowees().add(user);

		this.userRepository.save(user);
	}

	//v1.0 - Implemented by JA
	public User reconstruct(final UserRegistrationForm userRegistrationForm, final BindingResult binding) {

		final User res = this.create();

		res.setName(userRegistrationForm.getName());
		res.setSurnames(userRegistrationForm.getSurnames());
		res.setPostalAddress(userRegistrationForm.getPostalAddress());
		res.setPhoneNumber(userRegistrationForm.getPhoneNumber());
		res.setEmail(userRegistrationForm.getEmail());

		this.validator.validate(res, binding);

		res.getUserAccount().setUsername(userRegistrationForm.getUsername());
		res.getUserAccount().setPassword(userRegistrationForm.getPassword());

		final Errors userAccountErrors = new BeanPropertyBindingResult(res.getUserAccount(), binding.getObjectName());

		this.validator.validate(res.getUserAccount(), userAccountErrors);

		binding.addAllErrors(userAccountErrors);

		return res;

	}

	// v1.0 - Implemented by Alicia
	public void unfollow(final User user) {
		Assert.notNull(user);

		final User principal = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(principal);

		Assert.isTrue(principal.getFollowees().contains(user));

		principal.getFollowees().remove(user);

		this.userRepository.save(user);
	}

	//v1.0 - Implemented by Alicia
	public Page<User> getFollowersByUser(final User user, final int page, final int size) {
		Assert.notNull(user);

		final Page<User> res = this.userRepository.followersByUserId(user.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	//v1.0 - Implemented by Alicia
	public Page<User> getFolloweesByUser(final User user, final int page, final int size) {
		Assert.notNull(user);

		final Page<User> res = this.userRepository.followeesByUserId(user.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}
}
