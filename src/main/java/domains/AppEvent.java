package domains;

public class AppEvent {
	private String eventId;
	private long timestamp;
	private String subject;
	private String message;
	private AppEventStatus status = AppEventStatus.Query;
	
	public AppEvent() { }

	public AppEvent(String eventId, long timestamp, String subject, String message, AppEventStatus status) {
		this.eventId = eventId;
		this.timestamp = timestamp;
		this.subject = subject;
		this.message = message;
		this.status = status;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public AppEventStatus getStatus() {
		return status;
	}

	public void setStatus(AppEventStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "AppEvent [eventId=" + eventId + ", timestamp=" + timestamp + ", subject=" + subject + ", message="
				+ message + ", status=" + status + "]";
	}

}
