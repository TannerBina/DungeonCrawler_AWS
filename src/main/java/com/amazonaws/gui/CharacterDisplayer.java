package com.amazonaws.gui;

import javafx.beans.property.SimpleStringProperty;

public class CharacterDisplayer {
	private SimpleStringProperty name;
	private SimpleStringProperty race;
	private SimpleStringProperty pcClass;
	private SimpleStringProperty level;
	
	public CharacterDisplayer(String name, String race, 
			String pcClass, String level) {
		this.name = new SimpleStringProperty(name);
		this.race = new SimpleStringProperty(race);
		this.pcClass = new SimpleStringProperty(pcClass);
		this.level = new SimpleStringProperty(level);
	}

	public String getName() {
		return name.get();
	}

	public String getRace() {
		return race.get();
	}

	public String getPcClass() {
		return pcClass.get();
	}

	public String getLevel() {
		return level.get();
	}
	
	
}
