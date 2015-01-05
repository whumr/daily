package com.test.demo.domain;

public class App {

	private String picture;
	private int score;
	private String name;
	private String author;
	private String description;
	private double price;
	private String type;
	private long viewNum;
	
	public App() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public App(String picture, int score, String name, String author,
			String description, double price, String type, long viewNum) {
		super();
		this.picture = picture;
		this.score = score;
		this.name = name;
		this.author = author;
		this.description = description;
		this.price = price;
		this.type = type;
		this.viewNum = viewNum;
	}

	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getViewNum() {
		return viewNum;
	}
	public void setViewNum(long viewNum) {
		this.viewNum = viewNum;
	}
}
