
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.ANMessage;

@Repository
public interface ANMessageRepository extends JpaRepository<ANMessage, Integer> {

}
