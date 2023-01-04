package domains;

public enum AppEventStatus {
	
	Creation ('C', "Creation"), 
	Update ('U', "Update"), 
	Deletion ('D', "Deletion"), 
	Query ('Q', "Query");
	
	private char code;
	private String name;
	
	private AppEventStatus(char code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public char getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public static AppEventStatus fromCode(char code) {
		switch (code) {
			case 'C': return Creation;
			case 'U': return Update;
			case 'D': return Deletion;
			case 'Q': return Query;
			default: return null;
		}
	}
	

}
