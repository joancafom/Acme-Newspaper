
package forms;

import javax.validation.constraints.NotNull;

import domain.Advertisement;
import domain.Newspaper;

public class AdvertiseForm {

	//Form
	private Newspaper		newspaper;
	private Advertisement	advertisement;


	@NotNull
	public Advertisement getAdvertisement() {
		return this.advertisement;
	}
	public void setAdvertisement(final Advertisement advertisement) {
		this.advertisement = advertisement;
	}

	@NotNull
	public Newspaper getNewspaper() {
		return this.newspaper;
	}
	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
	}

}
