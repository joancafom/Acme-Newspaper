
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ANMessage;

@Repository
public interface ANMessageRepository extends JpaRepository<ANMessage, Integer> {

	@Query("select m from ANMessage m where m.folder.id=?1")
	Page<ANMessage> findMessagesByFolderPaged(int folderId, Pageable pageRequest);

}
