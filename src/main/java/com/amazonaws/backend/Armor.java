package com.amazonaws.backend;

import java.util.Scanner;

/*
 * Struct that stores an armor and its information.
 * All public as it is a struct. Can be created
 * from empty, all data, or string of data.
 */
public class Armor {
	public String name;
	public int ac;
	public int dexMax;
	public String shield;
	public String equipped;
	
	public Armor() {}
	public Armor(String name, int ac, int dexMax,
			String shield, String equipped) {
		this.name = name;
		this.ac = ac;
		this.dexMax = dexMax;
		this.shield = shield;
		this.equipped = equipped;
	}
	
	/*
	 * Creates a piece of armor from the inputted
	 * data string. Of the form NAME_AC_DEXMAX_SHIELD_EQUIPPED
	 */
	public Armor(String data) {
		Scanner s = new Scanner(data);
		s.useDelimiter("_");
		
		try {
			name = s.next();
			ac = Integer.parseInt(s.next());
			dexMax = Integer.parseInt(s.next());
			shield = s.next();
			equipped = s.next();
		} catch (Exception e) {
			System.err.println("CANNOT CREATE ARMOR. ERROR IN INPUTTED DATA : " + data);
		}
		
		s.close();
	}
}
