import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/*
A game currently being played.
Has a set of players and a dungeon master
as well as an name and password to access it
 */
public class Game implements Runnable, Comparable<Game>{

	//a simple class to store a game message from someone
	public static class GameMessage{
		public String message;
		public Client client;
		public GameMessage(String message, Client client){
			this.message = message.trim();
			this.client = client;
		}
	}

	//list of all messages for the queue to handle
	private BlockingQueue<GameMessage> messageQueue;
	//controls whether the game is running or not
	private boolean running;
	//the name of the game
	private String name;
	//the game thread
	private Thread thread;

	//the vector of clients (includes the dm)
	private Vector<Client> clients;

	//the context of the entire game
	private Context gameContext;

	/*
	Constructs a game with the entered name
	 */
	public Game(String name, BlockingQueue<GameMessage> q){
		gameContext = new Context();
		this.name = name;
		clients = new Vector<>();
		messageQueue = q;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	/*
	The run loop for a game to handle messages
	that have been placed in their message queues
	 */
	public void run(){
		while(running){
			if (Main.DEBUG && messageQueue.size() > 0){
				System.out.printf("Game %s has %d messages.", name, messageQueue.size());
			}
			try{
				handle(messageQueue.take());
			} catch(InterruptedException e){
				System.out.printf("Unexpected interrupt in %s\n", name);
			}
		}
	}

	//handles an inputted game message
	public void handle(GameMessage m){
		if (Main.DEBUG){
			System.out.printf("Game %s Handling : %s \n", name, m.message);
		}

		if (m.message.startsWith("$SETCHAR")){
			if (Main.DEBUG){
				System.out.println("Attempting to SETCHAR in Game");
			}

			ArrayList<String> params = new ArrayList<>();
			Scanner s = new Scanner(m.message);

			try{
				s.next();
				params.add(s.next());
				params.add(s.nextLine());
				handle(new Util.FullCommand(Util.Command.SETCHAR, params), m.client);
				s.close();
				return;
			} catch(Exception e){
				e.printStackTrace();
				System.out.println("Mismatched character input while handling $SETCHAR in Game");
			}

			s.close();
		}
		//if it is command form parse and handle it
		if (m.message.charAt(0) == '$' || m.message.charAt(0) == '@'){
			Util.FullCommand command = new Util.FullCommand(m.message, gameContext);
			//if its null say that it cannot be handled
			if (command.command == Util.Command.NULL){
				sendToAll("Cannot understand command : " + m.message);
			} else
				handle(command, m.client);
		} 
		//else send it to everyone
		else {
			sendToAll(m.message);
		}
	}


	public void handle(Util.FullCommand command, Client client){
		if (Main.DEBUG){
			System.out.printf("Game handling command : %s\n", command.command);
			for (int i = 0; i < command.params.size(); i++){
				String s = command.params.get(i);
				System.out.printf("Param %d : %s\n", i, s);
			}
		}
		switch(command.command){
			case SETCHAR:
				setChar(command, client);
				break;

			default:
				sendToAll("ERROR : No handler for command : " + command.command);
				System.out.printf("No handler for command in game %s : %s\n", name, command.command);
				break;
		}
	}

	/*
	Sends an inputted character to everyone,
	updating it to everyone connected ot the game
	 */
	public void sendChar(Character c){
		sendToAll(String.format("#%s %s %s", Util.ResponseCommand.SETCHAR, 
			c.getStat(Character.StatTag.ID), c.toString()));
	}

	/*
	Called when a setcharacter command is called, attempts to set
	the clients character given a correct validation id
	 */
	public void setChar(Util.FullCommand command, Client client){
		if (command.params.get(0).equals(client.getID())){
			System.out.printf("Character set for %s\n", client.getID());
			client.setCharacter(new Character(command.params.get(1)));
			
			client.getCharacter().validate();
			
			if (Main.DEBUG){
				System.out.println(client.getCharacter().toString());
			}

			//set the validated character for the client, add to everyones
			client.send(String.format("#%s %s %s", Util.ResponseCommand.SETCHAR, 
				client.getCharacter().getStat(Character.StatTag.ID), client.getCharacter().toString()));
			sendToAll(String.format("#%s %s", Util.ResponseCommand.ADDCHAR, client.getCharacter().toString()));
		} else {
			System.out.printf("Cannot set char for %s : incorrect id : %s\n", client.getID(), command.params.get(0));
		}	
	}

	//Send a message to every client who is linked
	//to the game
	public void sendToAll(String m){
		for (Client c : clients){
			if (c.isActive())
				c.send(m);
		}
	}

	//adds a entered client to this game
	public synchronized void addClient(Client c){
		//send every character already in the game to the client
		for (Client client : clients){
			if (!client.isDM()){
				client.send(String.format("#%s %s", Util.ResponseCommand.ADDCHAR, client.getCharacter().toString()));
			}
		}

		//set the clients game and add the client the client list
		c.setGame(this);
		clients.add(c);

		//put the clients id in the gamecontext
		gameContext.put(Util.ParamType.SYSTEM, c.getID());

		//send send character request and validation token
		if (!c.isDM()){
			c.send(String.format("#%s %s", Util.ResponseCommand.SENDCHAR, c.getID()));
		}
	}

	/*
	Removes a given client and his character from this game 
	when the client is disconnected
	 */
	public synchronized void remove(Client c){
		if (Main.DEBUG){
			System.out.printf("Removing Client %s \n", c.getID());
		}
		clients.remove(c);
		sendToAll(String.format("#%s %s", Util.ResponseCommand.REMOVECHAR, c.getCharacter().getStat(Character.StatTag.ID)));
	}

	public synchronized void closeGame(){
		if (Main.DEBUG){
			System.out.printf("Closing Game %s \n", name);
		}

		for (Client c : clients){
			if (c.isActive()){
				c.send("Game closing due to DM disconnect.");
				Server.getInstance().remove(c);
				clients.remove(c);

				try{
					c.close();
				} catch (Exception e){
					System.out.printf("Error closing client %s in closeGame.\n", c.getID());
				}
			}
		}
		running = false;
	}

	public String getName(){return name;}

	public boolean equals(Game other){
		return other.name.equals(name);
	}

	public int compareTo(Game other){
		return other.name.compareTo(name);
	}
}