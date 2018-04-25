
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

	// v1.0 - Implemented by JA
	@Query("select s from Subscription s where s.subscriber.id = ?1 and s.newspaper.id = ?2")
	Subscription getSubscriptionCustomerNewspaperId(final int customerId, final int newspaperId);

}
