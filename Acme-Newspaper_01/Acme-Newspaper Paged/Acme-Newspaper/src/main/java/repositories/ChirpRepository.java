
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Integer> {

	@Query("select c from User u1 join u1.followees u2 join u2.chirps c where u1.id=?1 order by c.moment desc")
	Collection<Chirp> getStream(int userId);

	//v1.0 - Implemented by JA
	@Query("select c from User u1 join u1.followees u2 join u2.chirps c where u1.id=?1 order by c.moment desc")
	Page<Chirp> getStream(int userId, Pageable pageable);

	@Query("select c from Chirp c where c.containsTaboo = true")
	Collection<Chirp> findTabooedChirps();

	//v1.0 - Implemented by JA
	@Query("select c from Chirp c where c.containsTaboo = true")
	Page<Chirp> findTabooedChirps(Pageable pageable);

	// v1.0 Implemented by Alicia
	@Query("select c from Chirp c where c.containsTaboo = false")
	Collection<Chirp> findNotTabooedChirps();

	// v1.0 - Implemented by Alicia
	@Query("select c from Chirp c where c.user.id = ?1")
	Page<Chirp> chirpsByUserId(int userId, Pageable pageable);
}
