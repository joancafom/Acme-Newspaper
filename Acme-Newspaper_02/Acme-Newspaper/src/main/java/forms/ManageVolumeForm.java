
package forms;

import javax.validation.constraints.NotNull;

import domain.Newspaper;
import domain.Volume;

public class ManageVolumeForm {

	//Form
	private Volume		volume;
	private Newspaper	newspaper;


	@NotNull
	public Volume getVolume() {
		return this.volume;
	}

	public void setVolume(final Volume volume) {
		this.volume = volume;
	}

	@NotNull
	public Newspaper getNewspaper() {
		return this.newspaper;
	}

	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
	}

}
