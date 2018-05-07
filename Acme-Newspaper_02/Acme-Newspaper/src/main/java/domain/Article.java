
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "title, isFinal, containsTaboo")
})
public class Article extends DomainEntity {

	private String				title;
	private Date				publicationDate;
	private String				summary;
	private String				body;
	private Collection<String>	pictures;
	private boolean				isFinal;
	private boolean				containsTaboo;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getPublicationDate() {
		return this.publicationDate;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Column(columnDefinition = "TEXT")
	public String getSummary() {
		return this.summary;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Column(columnDefinition = "TEXT")
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

	@NotNull
	public boolean getContainsTaboo() {
		return this.containsTaboo;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
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

	public void setIsFinal(final boolean isFinal) {
		this.isFinal = isFinal;
	}

	public void setContainsTaboo(final boolean containsTaboo) {
		this.containsTaboo = containsTaboo;
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

	@Valid
	@OneToMany(mappedBy = "mainArticle")
	public Collection<Article> getFollowUps() {
		return this.followUps;
	}

	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
	}

	public void setWriter(final User writer) {
		this.writer = writer;
	}

	public void setMainArticle(final Article mainArticle) {
		this.mainArticle = mainArticle;
	}

	public void setFollowUps(final Collection<Article> followUps) {
		this.followUps = followUps;
	}

}
