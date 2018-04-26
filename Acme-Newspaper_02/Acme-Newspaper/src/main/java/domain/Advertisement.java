
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Advertisement extends DomainEntity {

	private String		title;
	private String		bannerURL;
	private String		targetURL;
	private CreditCard	creditCard;
	private boolean		containsTaboo;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	@URL
	@NotBlank
	public String getBannerURL() {
		return this.bannerURL;
	}

	@URL
	@NotBlank
	public String getTargetURL() {
		return this.targetURL;
	}

	@NotNull
	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	@NotNull
	public boolean getContainsTaboo() {
		return this.containsTaboo;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setBannerURL(final String bannerURL) {
		this.bannerURL = bannerURL;
	}

	public void setTargetURL(final String targetURL) {
		this.targetURL = targetURL;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public void setContainsTaboo(final boolean containsTaboo) {
		this.containsTaboo = containsTaboo;
	}


	// Relationships ---------------------------------------------

	private Collection<Newspaper>	newspapers;
	private Agent					agent;


	@NotEmpty
	@Valid
	@ManyToMany(mappedBy = "advertisements")
	public Collection<Newspaper> getNewspapers() {
		return this.newspapers;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Agent getAgent() {
		return this.agent;
	}

	public void setNewspapers(final Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}

	public void setAgent(final Agent agent) {
		this.agent = agent;
	}

}
