
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	// v1.0 - Implemented by JA
	@Query("select a from Article a where a.newspaper.publicationDate != null and a.writer.id = ?1")
	Collection<Article> publishedArticlesByWriterId(final int userId);

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a where a.newspaper.publicationDate != null and a.writer.id = ?1")
	Page<Article> publishedArticlesByWriterId(final int userId, Pageable pageable);

	// v2.0 - Implemented by JA
	@Query("select a from Article a where a.newspaper.publicationDate = null and a.writer.id = ?1 and a.newspaper.id = ?2")
	Collection<Article> unpublishedArticlesByWriterNewspaperId(final int userId, final int newspaperId);

	// v2.0 - Implemented by JA
	@Query("select a from Article a where (a.newspaper.publicationDate = null and a.writer.id = ?1 or a.isFinal = true) and  a.newspaper.id = ?2")
	Page<Article> unpublishedAndFinalArticlesByWriterNewspaperId(final int userId, final int newspaperId, Pageable pageable);

	/* v1.0 - josembell */
	@Query("select a from Article a where (a.title like %?1% or a.summary like %?1% or a.body like %?1%) and a.newspaper.publicationDate!=null")
	Collection<Article> findPublishedByKeyword(String keyword);

	// v1.0 - Implemented by JA
	@Query("select a from Article a where (a.title like %?1% or a.summary like %?1% or a.body like %?1%) and a.newspaper.publicationDate!=null")
	Page<Article> findPublishedByKeyword(String keyword, Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a where a.isFinal = true and a.newspaper.id = ?1")
	Collection<Article> getAllFinalByNewspaperId(int newspaperId);

	// v1.0 - Implemented by JA
	@Query("select count(a) from Article a where a.isFinal = true and a.newspaper.id = ?1")
	Integer getAllFinalByNewspaperIdSize(int newspaperId);

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a where a.isFinal = true and a.newspaper.id = ?1")
	Page<Article> getAllFinalByNewspaperId(int newspaperId, Pageable pageable);

	// v1.0 - Implemented by JA
	@Query("select a from Article a where a.newspaper.id = ?1")
	Page<Article> getAllByNewspaperId(int newspaperId, Pageable pageable);

	// v1.0 - Implemented by JA
	@Query("select a from Article a where a.containsTaboo = true")
	Collection<Article> findTabooedArticles();

	// v1.0 - Implemented by JA
	@Query("select a from Article a where a.containsTaboo = true")
	Page<Article> findTabooedArticles(Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a where a.containsTaboo = false")
	Collection<Article> findNotTabooedArticles();

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a where a.newspaper.publicationDate != null and a.newspaper.isPublic = true and a.writer.id = ?1")
	Collection<Article> publishedAndPublicByWriterId(int userId);

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a where a.newspaper.publicationDate != null and a.newspaper.isPublic = true and a.writer.id = ?1")
	Page<Article> publishedAndPublicByWriterId(int userId, Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a where (a.title like %?1% or a.summary like %?1% or a.body like %?1%) and a.newspaper.publicationDate != null and a.newspaper.isPublic = true")
	Collection<Article> findPublicAndPublishedByKeyword(String keyword);

	// v1.0 - Implemented by JA
	@Query("select a from Article a where (a.title like %?1% or a.summary like %?1% or a.body like %?1%) and a.newspaper.publicationDate != null and a.newspaper.isPublic = true")
	Page<Article> findPublicAndPublishedByKeyword(String keyword, Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a, Subscription s where a.writer.id = ?1 and a.newspaper = s.newspaper and s.subscriber.id = ?2")
	Collection<Article> suscribedByWriterAndCustomerId(int userId, int customerId);

	// v1.0 - Implemented by Alicia
	@Query("select a from Article a, Subscription s where a.writer.id = ?1 and a.newspaper = s.newspaper and s.subscriber.id = ?2")
	Page<Article> suscribedByWriterAndCustomerId(int userId, int customerId, Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select distinct a from Article a, Subscription s where (a.title like %?1% or a.summary like %?1% or a.body like %?1%) and ((a.newspaper.publicationDate != null and a.newspaper.isPublic = true) or (s.subscriber.id = ?2 and a.newspaper = s.newspaper))")
	Collection<Article> customerSearchResults(String keyword, int customerId);

	// v1.0 - Implemented by JA
	@Query("select distinct a from Article a, Subscription s where (a.title like %?1% or a.summary like %?1% or a.body like %?1%) and ((a.newspaper.publicationDate != null and a.newspaper.isPublic = true) or (s.subscriber.id = ?2 and a.newspaper = s.newspaper))")
	Page<Article> customerSearchResults(String keyword, int customerId, Pageable pageable);

}
