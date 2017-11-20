package com.amazonaws.backend;

import java.util.ArrayList;
import java.util.Scanner;

import com.amazonaws.backend.Character.StatTag;
import com.amazonaws.gui.WindowController;
import com.amazonaws.lambdafunction.callers.DeleteGameInput;
import com.amazonaws.lambdafunction.callers.FetchCharacterInput;
import com.amazonaws.lambdafunction.callers.Output;
import com.amazonaws.util.Constants;
import com.amazonaws.util.LambdaServices;
import com.amazonaws.util.Messages;

/*
 * The static user class for the person using
 * this application and all of their information
 */
public class User {
	
	//the static reference to user
	private static User user = null;
	
	//username and password of the user
	private String username;
	private String password;
	
	//the client for communication with the
	//server
	private Client client;
	
	//the game that the user is currently in
	private Game game;
	//tag for whether the player is the dm of a game
	private boolean dm;
	
	//all characters that the user has access to.
	private ArrayList<Character> characters;
	//the activeCharacter of the user
	private Character activeCharacter;
	//all characters in the party, including the user.
	private ArrayList<Character> party;

	//the window the user has access to.
	private WindowController controller;
	
	private User() {
		characters = new ArrayList<>();
		party = new ArrayList<>();
	}
	
	public static User getInstance() {
		if (user == null) user = new User();
		return user;
	}
	
	/*
	 * Sends a string to the server to be handled
	 * adds tag to the string
	 */
	public void send(String s) {
		String res = s;

		if (dm) {
			if (s.charAt(0) != '@') {
				res = "DM: " + s;
			}
		} else {
			if (s.charAt(0) != '$') {
				res = activeCharacter.getStat(StatTag.NAME) +": " + s;
			}
		}
		
		client.send(res);
	}
	
	/*
	 * Handles an inputted string send from the
	 * server
	 */
	public void handle(String s) {
		if (Constants.DEBUG) {
			System.out.println("DM Status : " + dm);
			System.out.println("Handling : " + s);
		}
		
		if (s.charAt(0) == '#') {
			Scanner scan = new Scanner(s);
			switch(scan.next()) {
			
			//send character command if not dm, fetch character and send with code given
			case "#SENDCHAR":
				if (Constants.DEBUG) System.out.println("Sending character");
				sendCharacter(scan.next());
				break;
				
			//set a given character id in the party with the information
			case "#SETCHAR":
				updateCharacter(scan.next(), scan.nextLine());
				break;
				
			//add a character to the party
			case "#ADDCHAR":
				String character = scan.nextLine();
				party.add(new Character(character));
				break;
				
			case "#REMOVECHAR":
				String id = scan.next();
				for (int i = party.size()-1; i>= 0; i--) {
					Character c = party.get(i);
					if (c.getStat(StatTag.ID).equals(id)) {
						party.remove(i);
					}
				}
				break;
				
			//if its not recognized, print out that it cnat be handles
			default:
				System.err.println("Cannot handle Command in User.handle : " + s);
				break;
			}
			scan.close();
			
			if (controller != null) {
				controller.update();
			}
		} else if (controller != null) {
			controller.display(s + "\n");
		}
	}
	
	//sends the active character to server with the entered validation code
	public void sendCharacter(String code) {
		if (dm) return;
		
		String response = "$SETCHAR " + code + " " + activeCharacter.toString();
		client.send(response);
	}
	
	//update a given character who is in the party with the given id;
	public void updateCharacter(String id, String character) {
		if (activeCharacter != null && activeCharacter.getStat(StatTag.ID).equals(id)){
			activeCharacter.setCharacter(character);
		}
		for (Character c : party) {
			if (c.getStat(StatTag.ID).equals(id)){
				c.setCharacter(character);
			}
		}
	}
	
	//Set the credentials used for the user to login
	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	/*
	 * Sets the array of characters for all characters
	 * based on inputted ids for all characters
	 */
	public void setCharacters(String data) {
		//get the scanner and seperate by delim
		Scanner s = new Scanner(data);
		s.useDelimiter(Constants.LAMBDA_DELIMINATOR);
		
		//while theres another char id
		while(s.hasNext()) {
			//get the tag
			String tag = s.next();
			
			//make sure its a char id
			if (tag.equals(Constants.CHAR_ID_TAG)) {
				//get the id and settup input to fetch char
				String id = s.next();
			
				FetchCharacterInput in = new FetchCharacterInput();
				in.setId(id);
				
				//fetch the character
				Output out = LambdaServices.fetchCharacter(in);
				Scanner s2 = new Scanner(out.getMessage());
				s2.useDelimiter(Constants.LAMBDA_DELIMINATOR);
				
				switch(s2.next()) {
				//if there was an error print out error messages
				case Messages.ERROR_TAG:
					try {
						System.err.println("Error in Fetching Characters : " + 
								Messages.getErrorString(Messages.getErrorMessage(out.getMessage())));
					} catch(Exception e) {
						System.err.println("Error Message not recognized in User setCharacters.");
					}
					break;
				
				//if it was successful add the character with the inputted data
				case Messages.SUCCESS_TAG:
					characters.add(new Character(out.getData()));
					break;
				}
				
				s2.close();
			}
		}
		
		s.close();
	}

	public ArrayList<Character> getCharacters() {
		return characters;
	}

	public void reset() {
		close();
		user = new User();
	}

	/*
	 * Connects to the server and creates a game
	 * within the server in which this player is the dungeon
	 * master
	 */
	public boolean createGame(String text, String password) {
		client = new Client();
		if (client.isActive()) {
			client.send("@CREATEGAME " + text);
			game = new Game(text, password);
			dm = true;
		} else {
			System.err.println("Error Activiating Client in User.createGame");
		}
		return client.isActive();
	}

	/*
	 * A method for whenever the program is about to exit
	 * insures that all necessary actions are taken
	 */
	public void close() {
		//if user is dm of a game, need to close the game
		if (game != null && dm) {
			DeleteGameInput in = new DeleteGameInput();
			in.setName(game.getName());
			LambdaServices.deleteGame(in);
		}
	}
	
	public boolean isDm() {
		return dm;
	}

	public void setDm(boolean dm) {
		this.dm = dm;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	public Character getActiveCharacter() {
		return activeCharacter;
	}

	/*
	 * Joins a particular game given the inputted name,
	 * password, and the character to join the game with
	 */
	public boolean joinGame(String name, String password, Character chosenChar) {
		client = new Client();
		if (client.isActive()) {
			client.send("$JOINGAME " + name);
			game = new Game(name, password);
			dm = false;
			activeCharacter = chosenChar;
			System.out.println(activeCharacter.toString());
		} else {
			System.err.println("Error Activiating Client in User.joinGame");
		}
		return client.isActive();
	}

	public WindowController getController() {
		return controller;
	}

	public void setController(WindowController controller) {
		this.controller = controller;
	}

	public ArrayList<Character> getParty() {
		return party;
	}
}
