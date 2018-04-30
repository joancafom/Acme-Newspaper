
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "isSystem, name")
})
public class Folder extends DomainEntity {

	private boolean	isSystem;
	private String	name;


	@NotNull
	public boolean getIsSystem() {
		return this.isSystem;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setIsSystem(final boolean isSystem) {
		this.isSystem = isSystem;
	}

	public void setName(final String name) {
		this.name = name;
	}


	// Relationships ---------------------------------------------

	private Collection<Folder>		childFolders;
	private Folder					parentFolder;
	private Collection<ANMessage>	anMessages;
	private Actor					actor;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "parentFolder")
	public Collection<Folder> getChildFolders() {
		return this.childFolders;
	}

	@Valid
	@ManyToOne(optional = true)
	public Folder getParentFolder() {
		return this.parentFolder;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "folder")
	public Collection<ANMessage> getAnMessages() {
		return this.anMessages;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Actor getActor() {
		return this.actor;
	}

	public void setChildFolders(final Collection<Folder> childFolders) {
		this.childFolders = childFolders;
	}

	public void setParentFolder(final Folder parentFolder) {
		this.parentFolder = parentFolder;
	}

	public void setAnMessages(final Collection<ANMessage> anMessages) {
		this.anMessages = anMessages;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

}
