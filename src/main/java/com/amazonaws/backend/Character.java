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
public class Character {

	// the individual stat tags for each stats
	// position in the array
	public enum StatTag {
		// enum for singular stats
		ID, CLASS, BASE_HP, BACKGROUND, LEVEL, ALIGNMENT, STR, DEX, INT, WIS, CON, CHA, NAME, RACE, AC, CURRENT_HP,
		
		//beginning of dependent stats
		STR_BON(STR), DEX_BON(DEX), INT_BON(INT), WIS_BON(WIS), CON_BON(CON), CHA_BON(CHA),
		ACROBATICS(DEX_BON), ANIMAL_HANDLING(WIS_BON), ARCANA(INT_BON),
		ATHLETICS(STR_BON), DECEPTION(CHA_BON), HISTORY(INT_BON),
		INSIGHT(WIS_BON), INTIMIDATION(CHA_BON), INVESTIGATION(INT_BON),
		MEDICINE(WIS_BON), NATURE(INT_BON), PERCEPTION(WIS_BON), PERFORMANCE(CHA_BON),
		PERSUASION(CHA_BON), RELIGION(INT_BON), SLEIGHT_OF_HAND(DEX_BON), STEALTH(DEX_BON),
		SURVIVAL(WIS_BON),
		TOTAL_HP, PROF_BON,

		END_SINGULAR_STAT_TAGS,

		// enum for listed stats
		FEAT, ITEM, WEAPON, PROF,

		END_LIST_STAT_TAGS,

		// enum for adding things to spellbook
		SPELL;
		
		public StatTag dependent;
		StatTag(){}
		StatTag(StatTag dependent){
			this.dependent = dependent;
		}
	}

	private Map<Integer, String> singularStatMap;
	private Map<Integer, ArrayList<String>> spellStatMap;

	private ArrayList<Weapon> weapons;
	private ArrayList<String> items;
	private ArrayList<String> feats;
	private ArrayList<String> profs;

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < StatTag.END_SINGULAR_STAT_TAGS.ordinal(); i++) {
			if (singularStatMap.get(i) != null) {
				sb.append("__");
				sb.append(StatTag.values()[i].toString());
				sb.append("__");
				sb.append(singularStatMap.get(i));
			}
		}

		for (Weapon w : weapons) {
			sb.append(w.toString());
		}

		for (String i : items) {
			sb.append("__ITEM__");
			sb.append(i);
		}

		for (String f : feats) {
			sb.append("__FEAT__");
			sb.append(f);
		}

		for (String p : profs) {
			sb.append("__PROF__");
			sb.append(p);
		}

		for (int i = 1; i < 10; i++) {
			for (String s : spellStatMap.get(i)) {
				sb.append("__SPELL__");
				sb.append(i);
				sb.append("_");
				sb.append(s);
			}
		}

		return sb.toString();
	}

	/*
	 * Creates an empty character class
	 */
	public Character() {
		init();
	}

	/*
	 * Creates a character class from a lot of info inputted as a string where you
	 * have __TAG__DATA
	 */
	public Character(String info) {
		this();
		
		setCharacter(info);
	}
	
	public void init() {
		singularStatMap = new HashMap<>();
		weapons = new ArrayList<>();
		items = new ArrayList<>();
		feats = new ArrayList<>();
		profs = new ArrayList<>();
		spellStatMap = new HashMap<>();

		for (int i = 0; i < 10; i++) {
			spellStatMap.put(i, new ArrayList<>());
		}
	}

	/*
	 * Updates the characters stats based on inputted data format the same as format
	 * for constructor
	 */
	public void setCharacter(String info) {
		init();
		
		// set scanner delim
		Scanner s = new Scanner(info);
		s.useDelimiter(Constants.LAMBDA_DELIMINATOR);

		while (s.hasNext()) {
			// get the tag and matching stat tag
			String tag = s.next();

			if (tag.trim().isEmpty())
				continue;
			if (tag.equals(Constants.NULL))
				continue;

			StatTag sTag;

			try {
				sTag = StatTag.valueOf(tag);
			} catch (IllegalArgumentException e) {
				System.err.println("Tag " + tag + " not recognized in Character:Character");
				continue;
			}

			// check which category it falls into
			if (sTag.ordinal() < StatTag.END_SINGULAR_STAT_TAGS.ordinal()) {
				// singular just set it
				singularStatMap.put(sTag.ordinal(), s.next());

				// add to list if list
			} else if (sTag.ordinal() < StatTag.END_LIST_STAT_TAGS.ordinal()) {
				switch (sTag) {
				case FEAT:
					feats.add(s.next());
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
				// get the data
				String data = s.next();

				// scan with new delim
				Scanner s2 = new Scanner(data);
				s2.useDelimiter("_");

				// get the spell level and add to list
				Integer val = Integer.parseInt(s2.next());
				String name = s2.next();

				spellStatMap.get(val).add(name);

				s2.close();
			}
		}

		s.close();
		
		if (Constants.DEBUG) {
			System.out.println("Character Set : " + toString());
		}
	}

	public String getStat(StatTag input) {
		return singularStatMap.get(input.ordinal());
	}

	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}

	public ArrayList<String> getItems() {
		return items;
	}

	public ArrayList<String> getFeats() {
		return feats;
	}

	public String getHitBonus(Weapon wep) {
		int val = 0;
		if (wep.finess.equals("TRUE")) {
			val += Integer.parseInt(getStat(StatTag.DEX_BON));
		} else {
			val += Integer.parseInt(getStat(StatTag.STR_BON));
		}
		val += Integer.parseInt(getStat(StatTag.PROF_BON));
		val += wep.bonus;
		return "+" + new Integer(val).toString();
	}

	public String getDamage(Weapon wep) {
		StringBuilder sb = new StringBuilder();
		sb.append(wep.dice);
		int val = wep.bonus;
		if (wep.finess.equals("TRUE")) {
			val += Integer.parseInt(getStat(StatTag.DEX_BON));
		} else {
			val += Integer.parseInt(getStat(StatTag.STR_BON));
		}
		sb.append("+" + val);
		return sb.toString();
	}

	public Map<Integer, ArrayList<String>> getSpellList() {
		return spellStatMap;
	}
}
