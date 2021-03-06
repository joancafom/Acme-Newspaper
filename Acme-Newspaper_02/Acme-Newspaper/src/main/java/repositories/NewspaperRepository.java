
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;

@Repository
public interface NewspaperRepository extends JpaRepository<Newspaper, Integer> {

	@Query("select n from Newspaper n where n.publicationDate!=null")
	Collection<Newspaper> findAllPublished();

	// v1.0 - Implemented by JA
	@Query("select n from Newspaper n where n.publicationDate!=null")
	Page<Newspaper> findAllPublished(Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select n from Newspaper n where n.publicationDate = null")
	Collection<Newspaper> findAllUnpublished();

	// v1.0 - Implemented by JA
	@Query("select n from Newspaper n, User u where n member of u.newspapers and u.id = ?1")
	Page<Newspaper> findByPublisherId(final int publisherId, Pageable pageable);

	// v1.0 - Implemented by JA
	@Query("select n from Newspaper n where n.publicationDate = null")
	Page<Newspaper> findAllUnpublished(Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select n from Newspaper n where (n.title like %?1% or n.description like %?1%) and n.publicationDate != null")
	Collection<Newspaper> findPublishedByKeyword(String keyword);

	// v1.0 - Implemented by JA
	@Query("select n from Newspaper n where (n.title like %?1% or n.description like %?1%) and n.publicationDate != null")
	Page<Newspaper> findPublishedByKeyword(String keyword, Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select n from Newspaper n where n.containsTaboo = true")
	Collection<Newspaper> findTabooed();

	// v1.0 - Implemented by Alicia
	@Query("select n from Newspaper n where n.containsTaboo = true")
	Page<Newspaper> findTabooed(Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select n from Newspaper n where n.containsTaboo = false")
	Collection<Newspaper> findNotTabooed();

	// v1.0 - Implemented by JA
	@Query("select n from Newspaper n")
	Page<Newspaper> findAllPag(Pageable pageable);

	// Acme-Newspaper 2.0 ------------------------------------------

	// v1.0 - Implemented by Alicia
	@Query("select distinct n from Newspaper n join n.advertisements e where e.agent.id = ?1")
	Collection<Newspaper> findAdvertised(int agentId);

	// v1.0 - Implemented by Alicia
	@Query("select distinct n from Newspaper n join n.advertisements e where e.agent.id = ?1")
	Page<Newspaper> findAdvertised(int agentId, Pageable pageable);

	// v1.0 - Implemented by JA
	@Query("select n from Newspaper n join n.volumes v where v.id = ?1")
	Page<Newspaper> findByVolume(int volumeId, Pageable pageable);

	// v1.0 - Implemented by JA
	@Query("select n2 from Newspaper n2 where n2 not in (select distinct n from Newspaper n join n.advertisements e where e.agent.id = ?1)")
	Collection<Newspaper> findNotAdvertised(int agentId);

	// v1.0 - Implemented by JA
	@Query("select n2 from Newspaper n2 where n2 not in (select distinct n from Newspaper n join n.advertisements e where e.agent.id = ?1)")
	Page<Newspaper> findNotAdvertised(int agentId, Pageable pageable);

	// v1.0 - Implemented by JA
	@Query("select n from Newspaper n join n.volumes v where v.id = ?1 and n.publicationDate <> null")
	Collection<Newspaper> findPublishedByVolume(int volumeId);

	// v1.0 - Implemented by JA
	@Query("select n from Newspaper n join n.volumes v where v.id = ?1 and n.publicationDate <> null")
	Page<Newspaper> findPublishedByVolume(int volumeId, Pageable pageable);

	/* v1.0 - josembell */
	// v2.0 - Modified by JA
	@Query("select n from Newspaper n, User u, Volume v where n member of u.newspapers and v.id=?1 and u.id=?2 and n not member of v.newspapers and n.publicationDate != null")
	Collection<Newspaper> findPublishedNewspapersYetToBeIncludedInVolume(int volumeId, int userId);

}
