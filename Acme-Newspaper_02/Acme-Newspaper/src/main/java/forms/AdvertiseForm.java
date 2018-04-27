
package forms;

import domain.Newspaper;

public class AdvertiseForm {

	//Form
	private Newspaper	newspaper;
	private int			advertisementId;


	public int getAdvertisementId() {
		return this.advertisementId;
	}
	public void setAdvertisementId(final int advertisementId) {
		this.advertisementId = advertisementId;
	}
	public Newspaper getNewspaper() {
		return this.newspaper;
	}
	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
	}

}
