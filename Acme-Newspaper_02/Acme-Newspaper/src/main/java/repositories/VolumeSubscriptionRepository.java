
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.VolumeSubscription;

@Repository
public interface VolumeSubscriptionRepository extends JpaRepository<VolumeSubscription, Integer> {

	// v1.0 - Implemented by Alicia
	// v2.0 - Modified by JA
	@Query("select vs from VolumeSubscription vs, Newspaper n where vs.subscriber.id = ?1 and n.id = ?2 and n member of vs.volume.newspapers")
	Collection<VolumeSubscription> getVolumeSubscriptionCustomerNewspaperId(final int customerId, final int newspaperId);

	// v1.0 - Implemented by Alicia
	@Query("select vs from VolumeSubscription vs where vs.subscriber.id = ?1 and vs.volume.id = ?2")
	VolumeSubscription getVolumeSubscriptionCustomerVolumeId(final int customerId, final int volumeId);

}
