
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	// Relationships ---------------------------------------------

	private Collection<Newspaper>	newspapers;
	private Collection<Article>		articles;
	private Collection<Chirp>		chirps;
	private Collection<User>		followers;
	private Collection<User>		followees;


	@NotNull
	@Valid
	@OneToMany()
	public Collection<Newspaper> getNewspapers() {
		return this.newspapers;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "writer")
	public Collection<Article> getArticles() {
		return this.articles;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "user")
	public Collection<Chirp> getChirps() {
		return this.chirps;
	}

	@NotNull
	@Valid
	@ManyToMany(mappedBy = "followee")
	public Collection<User> getFollowers() {
		return this.followers;
	}

	@NotNull
	@Valid
	@ManyToMany()
	public Collection<User> getFollowees() {
		return this.followees;
	}

}
