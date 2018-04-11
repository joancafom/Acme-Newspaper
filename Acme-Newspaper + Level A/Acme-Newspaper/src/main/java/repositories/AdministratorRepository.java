
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

	// B-Level Requirements  ---------------------------------------------

	// v1.0 - Implemented by Alicia
	@Query("select avg(a.followUps.size) from Article a")
	Double avgFollowUpsPerArticle();

	// v1.0 - Implemented by Alicia
	@Query("select count(a1) * 1.0 / (select count(a2) * 1.0 from Article a2) from Article a1 where a1.mainArticle != null and datediff(a1.newspaper.publicationDate, a1.mainArticle.newspaper.publicationDate) <= 7")
	Double avgFollowUpsPerArticleOneWeek();

	// v1.0 - Implemented by Alicia
	@Query("select count(a1) * 1.0 / (select count(a2) * 1.0 from Article a2) from Article a1 where a1.mainArticle != null and datediff(a1.newspaper.publicationDate, a1.mainArticle.newspaper.publicationDate) <= 14")
	Double avgFollowUpsPerArticleTwoWeeks();

	// v1.0 - Implemented by Alicia
	@Query("select avg(u.chirps.size) from User u")
	Double avgChirpsPerUser();

	// v1.0 - Implemented by Alicia
	@Query("select sqrt(sum(u.chirps.size * u.chirps.size) / count(u.chirps.size) - avg(u.chirps.size) * avg(u.chirps.size)) from User u")
	Double stdChirpsPerUser();

	// v1.0 - Implemented by Alicia
	@Query("select count(u1) * 1.0 / (select count(u3) * 1.0 from User u3) from User u1 where u1.chirps.size > (select avg(u2.chirps.size) * 1.75 from User u2)")
	Double ratioUsersAbove75AvgChirps();

	// A-Level Requirements  ---------------------------------------------

	// v1.0 - Implemented by Alicia
	@Query("select count(n1)*1.0 / (select count(n2)*1.0 from Newspaper n2 where n2.isPublic = false) from Newspaper n1 where n1.isPublic = true")
	Double ratioPublicVSPrivateNewspapers();

	// v1.0 - Implemented by Alicia
	@Query("select sum(n1.articles.size)*1.0 / (select count(n2)*1.0 from Newspaper n2 where n2.isPublic = false) from Newspaper n1 where n1.isPublic = false")
	Double avgArticlesPerPrivateNewspaper();

	// v1.0 - Implemented by Alicia
	@Query("select sum(n1.articles.size)*1.0 / (select count(n2)*1.0 from Newspaper n2 where n2.isPublic = true) from Newspaper n1 where n1.isPublic = true")
	Double avgArticlesPerPublicNewspaper();

	// v1.0 - Implemented by Alicia
	//	@Query("select count(c1)*1.0 / (select count(c2)*1.0 from Customer c2) from Customer c1 where c1.newspapers.size > 0")
	//	Double ratioSubscribersVSTotalNumberCustomers();

	// v1.0 - Implemented by JA
	@Query("select (u.newspapers.size-count(n)*1.0)/count(n) from Newspaper n, User u where n member u.newspapers and n.isPublic = true group by u")
	Collection<Long> ratiosPrivateVSPublicNewspapersPerPublisher();
}
