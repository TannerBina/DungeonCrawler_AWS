package com.amazonaws.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.amazonaws.util.Constants;

/*
 * A given character
 * Contains all information for a character
 * including all singular info as well
 * as list info for weapons, armors, spells,
 * and feats
 */
public class Character{

	//the individual stat tags for each stats
	//position in the array
	public enum StatTag{
		//enum for singular stats
		ID, CLASS, BASE_HP, BACKGROUND, LEVEL, ALIGNMENT,
		STR, DEX, INT, WIS, CON, CHA, NAME, RACE,
		
		END_SINGULAR_STAT_TAGS,
		
		//enum for listed stats
		FEAT, ARMOR, ITEM, WEAPON, PROF,
		
		END_LIST_STAT_TAGS,
		
		//enum for adding things to spellbook
		SPELL
	}
	
	private Map<Integer, String> singularStatMap;
	private Map<Integer, ArrayList<String>> spellStatMap;
	
	private ArrayList<Weapon> weapons;
	private ArrayList<Armor> armors;
	private ArrayList<String> items;
	private ArrayList<String> feats;
	private ArrayList<String> profs;
	
	/*
	 * Creates an empty character class
	 */
	public Character() {
		singularStatMap = new HashMap<>();
		weapons = new ArrayList<>();
		armors = new ArrayList<>();
		items = new ArrayList<>();
		feats = new ArrayList<>();
		profs = new ArrayList<>();
		spellStatMap = new HashMap<>();
		
		for (int i = 1; i < 10; i++) {
			spellStatMap.put(i, new ArrayList<>());
		}
	}
	
	/*
	 * Creates a character class from a lot of info
	 * inputted as a string where you have
	 * __TAG__DATA
	 */
	public Character(String info) {
		this();
		
		//set scanner delim
		Scanner s = new Scanner(info);
		s.useDelimiter(Constants.LAMBDA_DELIMINATOR);
		
		while(s.hasNext()) {
			//get the tag and matching stat tag
			String tag = s.next();
			
			if (tag.trim().isEmpty()) continue;
			if (tag.equals(Constants.NULL)) continue;
			
			StatTag sTag;
			
			try {
				sTag = StatTag.valueOf(tag);
			} catch(IllegalArgumentException e) {
				System.err.println("Tag " + tag + " not recognized in Character:Character");
				continue;
			}
			
			//check which category it falls into
			if (sTag.ordinal() < StatTag.END_SINGULAR_STAT_TAGS.ordinal()) {
				//singular just set it
				singularStatMap.put(sTag.ordinal(), s.next());
				
			//add to list if list
			} else if (sTag.ordinal() < StatTag.END_LIST_STAT_TAGS.ordinal()) {
				switch(sTag) {
				case FEAT:
					feats.add(s.next());
					break;
				case ARMOR:
					armors.add(new Armor(s.next()));
					break;
				case WEAPON:
					weapons.add(new Weapon(s.next()));
					break;
				case ITEM:
					items.add(s.next());
					break;
				case PROF:
					profs.add(s.next());
				default:
					break;
				}
			} else {
				//get the data
				String data = s.next();
				
				//scan with new delim
				Scanner s2 = new Scanner(data);
				s2.useDelimiter("_");
				
				//get the spell level and add to list
				Integer val = Integer.parseInt(s2.next());
				String name = s2.next();
				
				spellStatMap.get(val).add(name);
				
				s2.close();
			}
		}
		
		s.close();
	}

	public String getStat(StatTag input) {
		return singularStatMap.get(input.ordinal());
	}
	
	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}

	public ArrayList<Armor> getArmors() {
		return armors;
	}

	public ArrayList<String> getItems() {
		return items;
	}

	public ArrayList<String> getFeats() {
		return feats;
	}
	
	
}
