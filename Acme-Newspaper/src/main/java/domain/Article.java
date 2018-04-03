
package domain;

import java.beans.Transient;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Article extends DomainEntity {

	private String				title;
	private String				summary;
	private String				body;
	private Collection<String>	pictures;
	private boolean				isFinal;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	@Transient
	public Date getPublicationDate() {
		return null;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSummary() {
		return this.summary;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBody() {
		return this.body;
	}

	@ElementCollection
	@NotNull
	public Collection<String> getPictures() {
		return this.pictures;
	}

	@NotNull
	public boolean getIsFinal() {
		return this.isFinal;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setSummary(final String summary) {
		this.summary = summary;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public void setPictures(final Collection<String> pictures) {
		this.pictures = pictures;
	}

	public void setFinal(final boolean isFinal) {
		this.isFinal = isFinal;
	}


	// Relationships ---------------------------------------------

	private Newspaper			newspaper;
	private User				writer;
	private Article				mainArticle;
	private Collection<Article>	followUps;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Newspaper getNewspaper() {
		return this.newspaper;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public User getWriter() {
		return this.writer;
	}

	@Valid
	@ManyToOne(optional = true)
	public Article getMainArticle() {
		return this.mainArticle;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "mainArticle")
	public Collection<Article> getFollowUps() {
		return this.followUps;
	}

}
