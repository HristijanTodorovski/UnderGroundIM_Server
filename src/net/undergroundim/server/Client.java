package net.undergroundim.server;

/**
 * 
 * @author Troy
 *
 */
public class Client {
	private int user_id;
	private String username;
	private String screen_name;
	private String email;
	private byte age;
	private byte sex;
	private String location;
	private String about;
	private String made_date;
	private int status;
	private boolean online;
	
	/**
	 * Construct a new client.
	 * 
	 * @param user_id
	 * @param username
	 * @param screen_name
	 * @param email
	 * @param age
	 * @param sex
	 * @param location
	 * @param about
	 * @param made_date
	 * @param status
	 * @param online
	 */
	public Client(int user_id, String username, String screen_name, String email, byte age, byte sex, String location, String about, String made_date, int status, boolean online){
		this.user_id = user_id;
		this.username = username;
		this.screen_name = screen_name;
		this.email = email;
		this.age = age;
		this.sex = sex;
		this.location = location;
		this.about = about;
		this.made_date = made_date;
		this.status = status;
		this.online = online;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte getAge() {
		return age;
	}

	public void setAge(byte age) {
		this.age = age;
	}

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getMade_date() {
		return made_date;
	}

	public void setMade_date(String made_date) {
		this.made_date = made_date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}


}
