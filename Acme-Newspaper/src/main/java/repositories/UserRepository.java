
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	//v1.0 - Implemented by JA
	@Query("select u from User u where u.userAccount.id = ?1")
	User findByUserAccountId(final int userAccountId);

}
