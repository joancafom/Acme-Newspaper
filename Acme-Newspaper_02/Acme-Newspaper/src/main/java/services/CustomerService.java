
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import repositories.CustomerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.ANMessage;
import domain.Customer;
import domain.Subscription;
import domain.VolumeSubscription;
import forms.ActorRegistrationForm;

@Service
@Transactional
public class CustomerService {

	// Managed Repository
	@Autowired
	private CustomerRepository	customerRepository;

	// Supporting Services

	@Autowired
	private FolderService		folderService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private Validator			validator;


	// CRUD Methods -------------------------------

	//v1.0 - Implemented by JA
	//v1.0 - Updated by JA (Subscription)
	// v2.0 - Updated by Alicia (AN2)
	public Customer create() {

		final Customer res = new Customer();

		final Authority userAuthority = new Authority();
		final UserAccount userAccount = this.userAccountService.create();

		userAuthority.setAuthority(Authority.CUSTOMER);
		userAccount.getAuthorities().add(userAuthority);
		res.setUserAccount(userAccount);

		res.setSubscriptions(new ArrayList<Subscription>());

		res.setReceivedMessages(new ArrayList<ANMessage>());
		res.setSentMessages(new ArrayList<ANMessage>());
		res.setVolumeSubscriptions(new ArrayList<VolumeSubscription>());

		return res;

	}
	//v1.0 - Implemented by JA
	public Collection<Customer> findAll() {

		return this.customerRepository.findAll();
	}

	//v1.0 - Implemented by JA
	public Customer findOne(final int customerId) {

		return this.customerRepository.findOne(customerId);
	}

	//v1.0 - Implemented by JA
	// v2.0 - Updated by Alicia (AN2)
	public Customer save(final Customer customer) {

		Assert.notNull(customer);
		Assert.isTrue(customer.getId() == 0);

		//Access Control

		try {

			LoginService.getPrincipal();
			throw new RuntimeException("An authenticated Actor cannot register to the system");

		} catch (final IllegalArgumentException okFlow) {
			//Intentionally left in blank
		}

		//HashPassword
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(customer.getUserAccount().getPassword(), null);
		customer.getUserAccount().setPassword(hashedPassword);

		final Customer customerS = this.customerRepository.save(customer);

		this.folderService.createSystemFolders(customerS);

		return customerS;

	}

	// Other Business Methods -------------------------------

	//v1.0 - Implemented by JA
	public Customer findByUserAccount(final UserAccount userAccount) {

		final Customer res;

		Assert.notNull(userAccount);

		res = this.customerRepository.findByUserAccountId(userAccount.getId());

		return res;

	}

	// v1.0 - Implemented by JA
	public void flush() {
		this.customerRepository.flush();
	}

	//v1.0 - Implemented by JA
	public Customer reconstruct(final ActorRegistrationForm customerRegistrationForm, final BindingResult binding) {

		final Customer res = this.create();

		res.setName(customerRegistrationForm.getName());
		res.setSurnames(customerRegistrationForm.getSurnames());
		res.setPostalAddress(customerRegistrationForm.getPostalAddress());
		res.setPhoneNumber(customerRegistrationForm.getPhoneNumber());
		res.setEmail(customerRegistrationForm.getEmail());

		this.validator.validate(res, binding);

		res.getUserAccount().setUsername(customerRegistrationForm.getUsername());
		res.getUserAccount().setPassword(customerRegistrationForm.getPassword());

		final Errors customerAccountErrors = new BeanPropertyBindingResult(res.getUserAccount(), binding.getObjectName());

		this.validator.validate(res.getUserAccount(), customerAccountErrors);

		binding.addAllErrors(customerAccountErrors);

		return res;

	}

}
