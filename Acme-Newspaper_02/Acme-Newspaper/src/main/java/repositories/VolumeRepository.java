
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.User;
import domain.Volume;

@Repository
public interface VolumeRepository extends JpaRepository<Volume, Integer> {

	//v1.0 - Implemented by JA
	@Query("select v from Volume v")
	Page<Volume> findAllPaged(Pageable pageable);

	//v1.0 - Implemented by JA
	@Query("select u from Volume v, User u where v member of u.volumes")
	Page<User> findAllWithCreator(Pageable pageable);

}
