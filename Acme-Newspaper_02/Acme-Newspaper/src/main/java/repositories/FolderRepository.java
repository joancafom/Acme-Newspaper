
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

	@Query("select f from Folder f where f.isSystem = true")
	Collection<Folder> findAllSystem();

	@Query("select f from Folder f where f.name = 'notification box' and f.actor.id = ?1")
	Collection<Folder> findAllNotificationBoxExceptMine(int adminId);

	@Query("select f from Folder f where f.actor.id = ?1")
	Collection<Folder> findAllByActorId(int actorId);

	@Query("select f from Folder f where f.actor.id = ?1 and f.isSystem=false")
	Collection<Folder> findAllNotSystemByActorId(int actorId);

	@Query("select f from Folder f where f.isSystem=false and f.actor.id = ?2 and f not in ?1")
	Collection<Folder> findCompatibleFoldersToMoveByExcludedAndActorId(Collection<Folder> excluded, int actorId);

	@Query("select f from Folder f where f.actor.id = ?1 and f.name = ?2")
	Folder findByActorIdAndName(int actorId, String name);

	// v1.0 - Implemented by Alicia
	@Query("select f from Folder f where f.id != ?1 and f.actor.id = ?2")
	Collection<Folder> findAllExceptOneForActor(int folderId, int actorId);

	@Query("select f from Folder f where f.actor.id=?1 and f.parentFolder = null")
	Page<Folder> findAllParentFoldersByPrincipalPaged(int actorId, Pageable pageRequest);

	@Query("select f from Folder f where f.parentFolder.id=?2 and f.actor.id=?1")
	Page<Folder> findChildFoldersOfFolderByPrincipalPaged(int actorId, int folderId, Pageable pageRequest);
}
