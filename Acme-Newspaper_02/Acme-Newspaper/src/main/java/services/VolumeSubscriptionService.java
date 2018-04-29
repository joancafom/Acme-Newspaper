
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.VolumeSubscriptionRepository;
import security.LoginService;
import domain.Customer;
import domain.Newspaper;
import domain.Volume;
import domain.VolumeSubscription;

@Service
@Transactional
public class VolumeSubscriptionService {

	// Managed Repository ------------------------------------

	@Autowired
	private VolumeSubscriptionRepository	volumeSubscriptionRepository;

	// Supporting Services -----------------------------------

	@Autowired
	private CustomerService					customerService;


	// CRUD Methods ------------------------------------------

	// v1.0 - Implemented by Alicia
	public VolumeSubscription create(final Volume volume) {
		Assert.notNull(volume);

		final VolumeSubscription res = new VolumeSubscription();

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(customer);

		res.setSubscriber(customer);
		res.setVolume(volume);

		return res;
	}

	// v1.0 - Implemented by Alicia
	public VolumeSubscription findOne(final int volumeSubscriptionId) {
		return this.volumeSubscriptionRepository.findOne(volumeSubscriptionId);
	}

	// v1.0 - Implemented by Alicia
	public Collection<VolumeSubscription> findAll() {
		return this.volumeSubscriptionRepository.findAll();
	}

	// v1.0 - Implemented by Alicia
	public VolumeSubscription save(final VolumeSubscription volumeSubscription) {
		Assert.notNull(volumeSubscription);
		Assert.isTrue(volumeSubscription.getId() == 0);

		final Customer customer = this.customerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(customer);

		Assert.isTrue(!this.hasVolumeSubscriptionVolume(customer, volumeSubscription.getVolume()));

		Assert.notNull(volumeSubscription.getSubscriber());
		Assert.isTrue(volumeSubscription.getSubscriber().equals(customer));

		final LocalDate now = new LocalDate();
		Assert.notNull(volumeSubscription.getCreditCard());

		// Assert (year == current && month == current) || year == future || (year == current && month == future)
		Assert.isTrue((now.getYear() == volumeSubscription.getCreditCard().getYear() && now.getMonthOfYear() == volumeSubscription.getCreditCard().getMonth()) || (now.getYear() < volumeSubscription.getCreditCard().getYear())
			|| (now.getYear() == volumeSubscription.getCreditCard().getYear() && now.getMonthOfYear() < volumeSubscription.getCreditCard().getMonth()));

		return this.volumeSubscriptionRepository.save(volumeSubscription);
	}

	public void flush() {
		this.volumeSubscriptionRepository.flush();
	}

	// B-Level Requirements ----------------------------------

	// v1.0 - Implemented by Alicia
	public Boolean hasVolumeSubscriptionNewspaper(final Customer customer, final Newspaper newspaper) {
		Assert.notNull(customer);
		Assert.notNull(newspaper);

		return this.volumeSubscriptionRepository.getVolumeSubscriptionCustomerNewspaperId(customer.getId(), newspaper.getId()) == null ? false : true;
	}

	// v1.0 - Implemented by Alicia
	public Boolean hasVolumeSubscriptionVolume(final Customer customer, final Volume volume) {
		Assert.notNull(customer);
		Assert.notNull(volume);

		return this.volumeSubscriptionRepository.getVolumeSubscriptionCustomerVolumeId(customer.getId(), volume.getId()) == null ? false : true;
	}

}
