
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class ANMessage extends DomainEntity {

	private Date	sentMoment;
	private String	subject;
	private String	body;
	private String	priority;


	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getSentMoment() {
		return this.sentMoment;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSubject() {
		return this.subject;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBody() {
		return this.body;
	}

	@NotNull
	@Pattern(regexp = "(^(HIGH|NEUTRAL|LOW)$)")
	public String getPriority() {
		return this.priority;
	}

	public void setSentMoment(final Date sentMoment) {
		this.sentMoment = sentMoment;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public void setPriority(final String priority) {
		this.priority = priority;
	}


	// Relationships ---------------------------------------------

	private Folder				folder;
	private Collection<Actor>	recipients;
	private Actor				sender;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Folder getFolder() {
		return this.folder;
	}

	@NotEmpty
	@Valid
	@ManyToMany()
	public Collection<Actor> getRecipients() {
		return this.recipients;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Actor getSender() {
		return this.sender;
	}

	public void setFolder(final Folder folder) {
		this.folder = folder;
	}

	public void setRecipients(final Collection<Actor> recipients) {
		this.recipients = recipients;
	}

	public void setSender(final Actor sender) {
		this.sender = sender;
	}

}
