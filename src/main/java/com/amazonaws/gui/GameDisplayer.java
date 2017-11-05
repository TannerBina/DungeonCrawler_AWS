package com.amazonaws.gui;

import javafx.beans.property.SimpleStringProperty;

public class GameDisplayer {
	private SimpleStringProperty name;
	private SimpleStringProperty username;
	private String password;
	
	public GameDisplayer(String name, String username, String password) {
		this.name = new SimpleStringProperty(name);
		this.username = new SimpleStringProperty(username);
		this.password = password;
	}

	public String getName() {
		return name.get();
	}

	public String getUsername() {
		return username.get();
	}
	
	public String getPassword() {
		return password;
	}
	
	
}
