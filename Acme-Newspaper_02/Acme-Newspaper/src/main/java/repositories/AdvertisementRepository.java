
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Advertisement;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

	// v1.0 - Implemented by Alicia
	@Query("select count(a) from Advertisement a, Newspaper n where n member of a.newspapers and n.id = ?1 and a.agent.id = ?2")
	Integer advertisementsPerNewspaperAndAgent(int newspaperId, int agentId);

	/* v1.0 - josembell */
	@Query("select distinct a from Advertisement a, Newspaper n where n.id=?1 and n not member of a.newspapers and a.agent.id=?2")
	Collection<Advertisement> findAdvertisementsYetToAdvertInNewspaper(int newspaperId, int agentId);

	/* v1.0 - josembell */
	@Query("select a from Advertisement a where a.containsTaboo = true")
	Collection<Advertisement> findTabooedAdvertisements();
}
