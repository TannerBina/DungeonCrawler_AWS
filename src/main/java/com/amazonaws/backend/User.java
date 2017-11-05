package com.amazonaws.backend;

import java.util.ArrayList;
import java.util.Scanner;

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
	
	private User() {
		characters = new ArrayList<>();
	}
	
	public static User getInstance() {
		if (user == null) user = new User();
		return user;
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
	public void createGame(String text, String password) {
		client = new Client();
		if (client.isActive()) {
			client.send("$CREATEGAME " + text);
			game = new Game(text, password);
			dm = true;
		} else {
			System.err.println("Error Activiating Client in User.createGame");
		}
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
}
