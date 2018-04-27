
package forms;

import domain.Volume;

public class ManageVolumeForm {

	//Form
	private Volume	volume;
	private int		newspaperId;


	public Volume getVolume() {
		return this.volume;
	}

	public void setVolume(final Volume volume) {
		this.volume = volume;
	}

	public int getNewspaperId() {
		return this.newspaperId;
	}

	public void setNewspaperId(final int newspaperId) {
		this.newspaperId = newspaperId;
	}

}
