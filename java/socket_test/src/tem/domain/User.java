package tem.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	private long id;
	private String name;
	private String pass;
	private int age;
	private int gender;
	private String nickname;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String name, String pass, String age, int gender, String nickname) {
		super();
		this.name = name;
		this.pass = pass;
		this.nickname = nickname;
		if(age != null && !age.trim().equals(""))
			this.age = Integer.parseInt(age);
		this.gender = gender;
	}
	
	public String toString() {
		JSONObject json = new JSONObject();
		try {
			json.put("name", name);
			json.put("pass", pass);
			json.put("age", age);
			json.put("gender", gender);
			json.put("nickname", nickname);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}
	
	public String toJSONString() {
		return toString();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
