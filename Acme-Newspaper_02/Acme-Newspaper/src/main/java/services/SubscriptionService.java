
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SubscriptionRepository;
import security.LoginService;
import domain.Administrator;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;

@Service
@Transactional
public class SubscriptionService {

	// Managed Repository
	@Autowired
	private SubscriptionRepository	subscriptionRepository;

	//Supporting Services
	@Autowired
	private CustomerService			customerService;

	@Autowired
	private AdministratorService	adminService;

	@Autowired
	private Validator				validator;


	//CRUD Methods ----------

	public Subscription create(final Newspaper newspaper) {

		final Subscription res = new Subscription();

		Assert.notNull(newspaper);

		final Customer subscriber = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(subscriber);

		res.setSubscriber(subscriber);
		res.setNewspaper(newspaper);

		return res;
	}

	public Subscription findOne(final int subscriptionId) {

		return this.subscriptionRepository.findOne(subscriptionId);
	}

	public Collection<Subscription> findAll() {

		return this.subscriptionRepository.findAll();
	}

	// v1.0 - Unknown
	// v2.0 - Updated by Alicia
	public Subscription save(final Subscription subscription) {

		Assert.notNull(subscription);

		final Customer subscriber = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(subscriber);
		Assert.notNull(subscription.getNewspaper());

		Assert.isTrue(!this.hasSubscription(subscriber, subscription.getNewspaper()));
		Assert.isTrue(!subscription.getNewspaper().getIsPublic());
		Assert.notNull(subscription.getNewspaper().getPublicationDate());

		Assert.isTrue(subscription.getId() == 0);

		final LocalDate now = new LocalDate();
		Assert.notNull(subscription.getCreditCard());

		// Assert (year == current && month == current) || year == future || (year == current && month == future)
		Assert.isTrue((now.getYear() == subscription.getCreditCard().getYear() && now.getMonthOfYear() == subscription.getCreditCard().getMonth()) || (now.getYear() < subscription.getCreditCard().getYear())
			|| (now.getYear() == subscription.getCreditCard().getYear() && now.getMonthOfYear() < subscription.getCreditCard().getMonth()));

		return this.subscriptionRepository.save(subscription);
	}

	public void delete(final Subscription subscription) {

		Assert.notNull(subscription);

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		subscription.getSubscriber().getSubscriptions().remove(subscription);
		this.customerService.saveCascade(subscription.getSubscriber());

		this.subscriptionRepository.delete(subscription);
	}

	/* v1.0 - josembell */
	public void flush() {
		this.subscriptionRepository.flush();

	}

	//Level A Requirements -----------

	public Boolean hasSubscription(final Customer customer, final Newspaper newspaper) {
		Assert.notNull(customer);
		Assert.notNull(newspaper);

		return this.subscriptionRepository.getSubscriptionCustomerNewspaperId(customer.getId(), newspaper.getId()) == null ? false : true;
	}

	public Collection<Subscription> getSubscriptionByNewspaper(final Newspaper newspaper) {
		Assert.notNull(newspaper);

		return this.subscriptionRepository.getSubscriptionByNewspaper(newspaper.getId());
	}

	public Subscription reconstruct(final Subscription prunedSubscription, final BindingResult binding) {
		Assert.notNull(prunedSubscription);
		final Customer subscriber = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(subscriber);

		prunedSubscription.setSubscriber(subscriber);

		this.validator.validate(prunedSubscription, binding);
		return prunedSubscription;
	}

}
