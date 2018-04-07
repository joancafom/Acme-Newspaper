
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class SystemConfiguration extends DomainEntity {

	private String	tabooWords;


	@NotNull
	public String getTabooWords() {
		return this.tabooWords;
	}

	public void setTabooWords(final String tabooWords) {
		this.tabooWords = tabooWords;
	}

}
