package com.amazonaws.gui;

import javafx.beans.property.SimpleStringProperty;

public class GameDisplayer {
	private SimpleStringProperty name;
	private SimpleStringProperty username;
	
	public GameDisplayer(String name, String username) {
		this.name = new SimpleStringProperty(name);
		this.username = new SimpleStringProperty(username);
	}

	public String getName() {
		return name.get();
	}

	public String getUsername() {
		return username.get();
	}
	
	
}
