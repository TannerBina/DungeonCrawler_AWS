import java.net.*;
import java.io.*;
import java.util.*;

/*
A game currently being played.
Has a set of players and a dungeon master
as well as an name and password to access it
 */
public class Game implements Runnable, Comparable<Game>{

	//a simple class to store a game message from someone
	private static class GameMessage{
		public String message;
		public Client client;
		public GameMessage(String message, Client client){
			this.message = message;
			this.client = client;
		}
	}

	//list of all messages for the queue to handle
	private Queue<GameMessage> messageQueue;
	//controls whether the game is running or not
	private boolean running;
	//the name of the game
	private String name;
	//the game thread
	private Thread thread;

	//the vector of clients (includes the dm)
	private Vector<Client> clients;

	/*
	Constructs a game with the entered name
	 */
	public Game(String name){
		this.name = name;
		clients = new Vector<>();
		thread = new Thread(this);
		thread.start();
	}

	/*
	The run loop for a game to handle messages
	that have been placed in their message queues
	 */
	public void run(){
		while(running){
			if (!messageQueue.isEmpty()){
				handle(messageQueue.remove());
			}
		}
	}

	//TODO implement handle method
	public void handle(GameMessage m){

	}

	//adds a entered client to this game
	public synchronized void addClient(Client c){
		c.setGame(this);
		clients.add(c);
	}

	//sends a message to the game object that is placed into the queue and handled.
	public synchronized void sendMessage(String s, Client c){
		messageQueue.add(new GameMessage(s, c));
	}

	public String getName(){return name;}

	public boolean equals(Game other){
		return other.name.equals(name);
	}

	public int compareTo(Game other){
		return other.name.compareTo(name);
	}
}