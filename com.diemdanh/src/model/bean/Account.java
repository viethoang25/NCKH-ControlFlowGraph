package model.bean;

public class Account {

	private String username;
	private String password;
	private String id;
	private String authorization;
	
	public Account() {
		
	}

	public Account(String username, String password, String id,
			String authorization) {
		super();
		this.username = username;
		this.password = password;
		this.id = id;
		this.authorization = authorization;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	
	
}
