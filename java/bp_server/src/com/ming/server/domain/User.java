package com.ming.server.domain;

import java.sql.Types;

import org.json.JSONException;
import org.json.JSONObject;

import com.ming.server.common.domain.BaseEntity;

public class User extends BaseEntity {

	private static final long serialVersionUID = -8487003619637468575L;
	
	protected static final String NAME = "name", PASS = "pass", AGE = "age",
		GENDER = "gender", NICKNAME = "nickname";
	
	protected long id;
	protected String name;
	protected String pass;
	protected int age;
	protected int gender;
	protected String nickname;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getTableName() {
		return "users";
	}
	
	public String[] getPrimaryKey() {
		return new String[]{"id"};
	}

	public User(String name, String pass, String age, String gender, String nickname) {
		super();
		this.name = name;
		this.pass = pass;
		this.nickname = nickname;
		if(age != null && !age.trim().equals(""))
			this.age = Integer.parseInt(age);
		if(gender != null && !gender.trim().equals(""))
			this.gender = Integer.parseInt(gender);
	}
	
	public String toString() {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
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
		addProperty("name", name, Types.VARCHAR);
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
		addProperty("pass", pass, Types.VARCHAR);
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
		addProperty("age", age, Types.INTEGER);
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
		addProperty("gender", gender, Types.INTEGER);
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
		addProperty("nickname", nickname, Types.VARCHAR);
	}
}
