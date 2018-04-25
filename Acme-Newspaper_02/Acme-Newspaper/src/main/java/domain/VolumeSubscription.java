
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class VolumeSubscription extends DomainEntity {

	private CreditCard	creditCard;


	@NotNull
	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}


	// Relationships ---------------------------------------------

	private Customer	subscriber;
	private Volume		volume;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Customer getSubscriber() {
		return this.subscriber;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Volume getVolume() {
		return this.volume;
	}

	public void setSubscriber(final Customer subscriber) {
		this.subscriber = subscriber;
	}

	public void setVolume(final Volume volume) {
		this.volume = volume;
	}

}
