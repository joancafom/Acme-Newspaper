
package forms;

public class CustomerRegistrationForm {

	//UserAccount
	private String	username;
	private String	password;
	private String	passwordConfirmation;

	//Customer
	private String	name;
	private String	surnames;
	private String	postalAddress;
	private String	phoneNumber;
	private String	email;

	//Form
	private boolean	acceptedTerms;


	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getPasswordConfirmation() {
		return this.passwordConfirmation;
	}

	public String getName() {
		return this.name;
	}

	public String getSurnames() {
		return this.surnames;
	}

	public String getPostalAddress() {
		return this.postalAddress;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getEmail() {
		return this.email;
	}

	public boolean getAcceptedTerms() {
		return this.acceptedTerms;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setPasswordConfirmation(final String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
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

	public void setAcceptedTerms(final boolean acceptedTerms) {
		this.acceptedTerms = acceptedTerms;
	}

}
