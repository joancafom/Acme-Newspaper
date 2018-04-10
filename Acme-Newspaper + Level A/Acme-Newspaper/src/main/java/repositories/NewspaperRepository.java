
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;

@Repository
public interface NewspaperRepository extends JpaRepository<Newspaper, Integer> {

	@Query("select n from Newspaper n where n.publicationDate!=null")
	Collection<Newspaper> findAllPublished();

	// v1.0 - Implemented by Alicia
	@Query("select n from Newspaper n where n.publicationDate = null")
	Collection<Newspaper> findAllUnpublished();

	// v1.0 - Implemented by Alicia
	@Query("select n from Newspaper n where (n.title like %?1% or n.description like %?1%) and n.publicationDate != null")
	Collection<Newspaper> findPublishedByKeyword(String keyword);

	// v1.0 - Implemented by Alicia
	@Query("select n from Newspaper n where n.containsTaboo = true")
	Collection<Newspaper> findTabooed();
}
