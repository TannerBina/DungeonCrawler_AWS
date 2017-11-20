package com.amazonaws.gui;

import com.amazonaws.backend.Character;
import com.amazonaws.backend.Character.StatTag;

import javafx.beans.property.SimpleStringProperty;

public class CharacterDisplayer {
	private SimpleStringProperty name;
	private SimpleStringProperty race;
	private SimpleStringProperty pcClass;
	private SimpleStringProperty level;
	private SimpleStringProperty hp;
	
	public CharacterDisplayer(String name, String race, 
			String pcClass, String level) {
		this.name = new SimpleStringProperty(name);
		this.race = new SimpleStringProperty(race);
		this.pcClass = new SimpleStringProperty(pcClass);
		this.level = new SimpleStringProperty(level);
	}
	
	public CharacterDisplayer(Character c) {
		this.name = new SimpleStringProperty(c.getStat(StatTag.NAME));
		this.race = new SimpleStringProperty(c.getStat(StatTag.RACE));
		this.pcClass = new SimpleStringProperty(c.getStat(StatTag.CLASS));
		this.level = new SimpleStringProperty(c.getStat(StatTag.LEVEL));
		this.hp = new SimpleStringProperty(c.getStat(StatTag.CURRENT_HP));
	}
	
	public String getHp() {
		return hp.get();
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
