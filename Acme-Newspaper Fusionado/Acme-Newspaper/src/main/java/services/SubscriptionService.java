
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SubscriptionRepository;
import security.LoginService;
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

	public Subscription save(final Subscription subscription) {

		Assert.notNull(subscription);

		final Customer subscriber = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(subscriber);
		Assert.notNull(subscription.getNewspaper());

		Assert.isTrue(!this.hasSubscription(subscriber, subscription.getNewspaper()));
		Assert.isTrue(!subscription.getNewspaper().getIsPublic());
		Assert.notNull(subscription.getNewspaper().getPublicationDate());

		Assert.isTrue(subscription.getId() == 0);

		return this.subscriptionRepository.save(subscription);
	}
	//Level A Requirements -----------

	public Boolean hasSubscription(final Customer customer, final Newspaper newspaper) {
		Assert.notNull(customer);
		Assert.notNull(newspaper);

		return this.subscriptionRepository.getSubscriptionCustomerNewspaperId(customer.getId(), newspaper.getId()) == null ? false : true;
	}
}
