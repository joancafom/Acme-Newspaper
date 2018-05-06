
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;
import domain.User;
import domain.Volume;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	//v1.0 - Implemented by JA
	@Query("select u from User u where u.userAccount.id = ?1")
	User findByUserAccountId(final int userAccountId);

	// v1.0 - Implemented by JA
	@Query("select u from User u where ?1 member u.newspapers")
	User findPublisherByNewspaper(final Newspaper newspaper);

	// v1.0 - Implemented by Alicia
	@Query("select follower from User follower, User u where u.id = ?1 and follower member of u.followers")
	Page<User> followersByUserId(int userId, Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select followee from User followee, User u where u.id = ?1 and followee member of u.followees")
	Page<User> followeesByUserId(int userId, Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select u from User u")
	Page<User> findAllPag(Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select u from User u, Newspaper n where n.id = ?1 and n member of u.newspapers")
	User findUserByNewspaperId(int newspaperId);

	//v1.0 - Implemented by JA
	@Query("select u from User u where ?1 member of u.volumes")
	User findByVolume(Volume volume);

}
