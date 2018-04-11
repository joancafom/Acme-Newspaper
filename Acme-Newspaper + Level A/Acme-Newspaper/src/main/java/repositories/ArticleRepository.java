
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	// v1.0 - Implemented by JA
	@Query("select a from Article a where a.newspaper.publicationDate != null and a.writer.id = ?1")
	Collection<Article> publishedArticlesByWriterId(final int userId);

	// v2.0 - Implemented by JA
	@Query("select a from Article a where a.newspaper.publicationDate = null and a.writer.id = ?1 and a.newspaper.id = ?2")
	Collection<Article> unpublishedArticlesByWriterNewspaperId(final int userId, final int newspaperId);

	/* v1.0 - josembell */
	@Query("select a from Article a where (a.title like %?1% or a.summary like %?1% or a.body like %?1%) and a.newspaper.publicationDate!=null")
	Collection<Article> findPublishedByKeyword(String keyword);

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a where a.isFinal = true and a.newspaper.id = ?1")
	Collection<Article> getAllFinalByNewspaperId(int newspaperId);

	// v1.0 - Implemented by JA
	@Query("select a from Article a where a.containsTaboo = true")
	Collection<Article> findTabooedArticles();

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a where a.containsTaboo = false")
	Collection<Article> findNotTabooedArticles();

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a where a.newspaper.publicationDate != null and a.newspaper.isPublic = true and a.writer.id = ?1")
	Collection<Article> publishedAndPublicByWriterId(int userId);
	/*
	 * // v1.0 - Implemented by Alicia
	 * 
	 * @Query("select a from Article a, Customer c where a.newspaper.isPublic = false and a.writer.id = ?1 and c.id = ?2 and a.newspaper member of c.newspapers")
	 * Collection<Article> suscribedByWriterAndCustomerId(int userId, int customerId);
	 * 
	 * // v1.0 - Implemented by Alicia
	 * 
	 * @Query("select distinct a from Article a, Customer c where (a.title like %?1% or a.summary like %?1% or a.body like %?1%) and ((a.newspaper.publicationDate != null and a.newspaper.isPublic = true) or (c.id = ?2 and a.newspaper member of c.newspapers))"
	 * )
	 * Collection<Article> customerSearchResults(String keyword, int customerId);
	 */

}
