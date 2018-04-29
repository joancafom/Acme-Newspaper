
package forms;

public class ANMessageForm {

	// ANMessage
	private String	subject;
	private String	body;
	private String	priority;

	private int		id	= 0;

	// Relationships
	private String	recipients;


	public String getSubject() {
		return this.subject;
	}

	public String getBody() {
		return this.body;
	}

	public String getPriority() {
		return this.priority;
	}

	public String getRecipients() {
		return this.recipients;
	}

	public int getId() {
		return this.id;
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

	public void setRecipients(final String recipients) {
		this.recipients = recipients;
	}

	public void setId(final int id) {
		this.id = id;
	}

}
