
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SubscriptionRepository;
import domain.Customer;
import domain.Newspaper;

@Service
@Transactional
public class SubscriptionService {

	// Managed Repository
	@Autowired
	private SubscriptionRepository	subscriptionRepository;


	//Level A Requirements -----------

	public Boolean hasSubscription(final Customer customer, final Newspaper newspaper) {
		Assert.notNull(customer);
		Assert.notNull(newspaper);

		return this.subscriptionRepository.getSubscriptionCustomerNewspaperId(customer.getId(), newspaper.getId()) == null ? false : true;
	}
}
