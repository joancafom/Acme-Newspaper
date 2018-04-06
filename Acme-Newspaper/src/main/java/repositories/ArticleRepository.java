
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
	
	/* v1.0 - josembell */
	@Query("select a from Article a where (a.title like %?1% or a.summary like %?1% or a.body like %?1%) and a.newspaper.publicationDate!=null")
	Collection<Article> findPublishedByKeyword(String keyword);

}