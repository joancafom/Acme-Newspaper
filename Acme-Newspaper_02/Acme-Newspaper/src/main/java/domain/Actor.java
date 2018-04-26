
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import security.UserAccount;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Actor extends DomainEntity {

	private String	name;
	private String	surnames;
	private String	postalAddress;
	private String	phoneNumber;
	private String	email;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSurnames() {
		return this.surnames;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPostalAddress() {
		return this.postalAddress;
	}

	@Pattern(regexp = "(^(\\+?[0-9]{9,})|$)")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	@NotBlank
	@Email
	public String getEmail() {
		return this.email;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setSurnames(final String surnames) {
		this.surnames = surnames;
	}

	public void setPostalAddress(final String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(final String email) {
		this.email = email;
	}


	// Relationships -------------------------------------------

	private UserAccount				userAccount;
	private Collection<ANMessage>	receivedMessages;
	private Collection<ANMessage>	sentMessages;


	@NotNull
	@Valid
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "recipient")
	public Collection<ANMessage> getReceivedMessages() {
		return this.receivedMessages;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "sender")
	public Collection<ANMessage> getSentMessages() {
		return this.sentMessages;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public void setReceivedMessages(final Collection<ANMessage> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}

	public void setSentMessages(final Collection<ANMessage> sentMessages) {
		this.sentMessages = sentMessages;
	}

}
