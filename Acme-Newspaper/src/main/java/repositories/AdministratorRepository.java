
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.Newspaper;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	@Query("select a from Administrator a where a.userAccount.id=?1")
	Administrator findByUserAccount(int userAccountId);

	// C-Level Requirements  ---------------------------------------------

	// v1.0 - Implemented by Alicia
	@Query("select avg(u.newspapers.size) from User u")
	Double avgNewspapersPerUser();

	// v1.0 - Implemented by Alicia
	@Query("select sqrt(sum(u.newspapers.size * u.newspapers.size) / count(u.newspapers.size) - avg(u.newspapers.size) * avg(u.newspapers.size)) from User u")
	Double stdNewspapersPerUser();

	// v1.0 - Implemented by Alicia
	@Query("select avg(u.articles.size) from User u")
	Double avgArticlesPerWriter();

	// v1.0 - Implemented by Alicia
	@Query("select sqrt(sum(u.articles.size * u.articles.size) / count(u.articles.size) - avg(u.articles.size) * avg(u.articles.size)) from User u")
	Double stdArticlesPerWriter();

	// v1.0 - Implemented by Alicia
	@Query("select avg(n.articles.size) from Newspaper n")
	Double avgArticlesPerNewspaper();

	// v1.0 - Implemented by Alicia
	@Query("select sqrt(sum(n.articles.size * n.articles.size) / count(n.articles.size) - avg(n.articles.size) * avg(n.articles.size)) from Newspaper n")
	Double stdArticlesPerNewspaper();

	// v1.0 - Implemented by Alicia
	@Query("select n1 from Newspaper n1 where n1.articles.size > (select avg(n2.articles.size) + (select avg(n3.articles.size) * 0.1 from Newspaper n3) from Newspaper n2)")
	Collection<Newspaper> getNewspapers10MoreArticlesThanAverage();

	// v1.0 - Implemented by Alicia
	@Query("select n1 from Newspaper n1 where n1.articles.size < (select avg(n2.articles.size) - (select avg(n3.articles.size) * 0.1 from Newspaper n3) from Newspaper n2)")
	Collection<Newspaper> getNewspapers10FewerArticlesThanAverage();

	// v1.0 - Implemented by Alicia
	@Query("select count(u1)*1.0 / (select count(u2)*1.0 from User u2) from User u1 where u1.newspapers.size > 0")
	Double ratioUsersHaveCreatedANewspaper();

	// v1.0 - Implemented by Alicia
	@Query("select count(u1)*1.0 / (select count(u2)*1.0 from User u2) from User u1 where u1.articles.size > 0")
	Double ratioUsersHaveWrittenAnArticle();

}
