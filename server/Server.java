import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/*
The whole server class. Allows creation and joining
of games, listens to new socket connects.

Holds all games and controls based on inputs.
 */
public class Server implements Runnable{

	//static reference to server
	private static Server server;
	//basePort for creating new games
	private static final int basePort = 5000;
	//the server socket on base port
	private ServerSocket serverSocket;
	//the server thread
	private Thread thread;

	//context for holding list of all games
	private Context gamesContext;
	private Vector<Game> gameList;
	private Map<Game, BlockingQueue<Game.GameMessage>> qMap;
	private Set<String> idSet;

	//list of all clients
	private Vector<Client> clients;

	//private constructor
	private Server(){
		clients = new Vector<>();
		idSet = new HashSet<>();
		gameList = new Vector<>();
		gamesContext = new Context();
		qMap = new HashMap<>();
	}

	//static instance fetcher for the server
	public static Server getInstance(){
		if (server == null) server = new Server();
		return server;
	}

	//start method for the server, begins to accept connections
	//all connections accepted on basePort
	public void startServer(){
		try{
			//attempt to bind to base port
			System.out.println("Binding to port, please wait...");
			serverSocket = new ServerSocket(basePort);
			System.out.println("Port successfully bound. Server open.");
			//start the thread
			start();
		} catch(IOException e){
			System.err.println("Failure to bind to basePort : " + basePort);
		}
	}

	/*
	The run command for the thread,
	attempts to add new clients
	 */
	public void run(){
		while(thread != null){
			try{
				addClient(serverSocket.accept());
				System.out.println("Client succesfully Added");
			} catch (IOException e){
				System.err.println("Server accept error.");
				stop();
			}
		}
	}

	/*
	Handles a sent string from a client
	The only strings this should handle are create
	and join game commands from clients
	all other strings are sent to the specific
	clients game for it to handle
	 */
	public synchronized void handle(String s, Client client){
		if (Main.DEBUG) System.out.printf("Server Recieved : %s \n", s);
		//if client is in a game, send to game to handle
		if (client.getGame() != null){
			
			if (Main.DEBUG){
				System.out.printf("Sending to Game %s\n", client.getGame().getName());
			}
			Game.GameMessage m = new Game.GameMessage(s, client);
			try{	
				qMap.get(client.getGame()).put(m);
			} catch (InterruptedException e){
				System.out.println("Interruped in handle of server.");
			}
			return;
		}

		if (Main.DEBUG){
			System.out.printf("Client not in Game, Handling\n");
		}

		//otherwise get the command
		Util.FullCommand cmd = new Util.FullCommand(s, gamesContext);

		//switch off command
		switch(cmd.command){
			case NULL:
			System.err.println("NULL Command passed into Server : " + s);
			break;

			//if create game, add to game context, make game and add client
			case CREATEGAME:
			gamesContext.put(Util.ParamType.SYSTEM, cmd.params.get(0));
			//settup blocking queue
			BlockingQueue<Game.GameMessage> q = new LinkedBlockingQueue<>();
			//create game
			gameList.add(new Game(cmd.params.get(0), q));
			//add q to qMap
			qMap.put(gameList.get(gameList.size()-1), q);
			//settup client as dm in gamew
			client.setDM(true);
			gameList.get(gameList.size()-1).addClient(client);
			System.out.println("Game created for : " + client.getID());
			break;

			//if join just add client
			case JOINGAME:
			Game g = findGame(cmd.params.get(0));
			if (g != null) g.addClient(client);
			else System.err.println("Could not find Game : " + s);
			break;

			default:
			System.err.println("Command not recognized : " + s);
			break;
		}
	}

	//finds the given game in the gamelist
	private Game findGame(String s) {
		for (Game g : gameList){
			if (g.getName().equals(s)){
				return g;
			}
		}
		return null;
	}

	/*
	Adds a client with the given socket to the list
	 */
	private synchronized void addClient(Socket socket){
		clients.add(new Client(socket, this, genID()));
	}

	/*
	Generates an id that has not been used
	and adds it to the lsit of ids
	 */
	private synchronized String genID(){
		String check = UUID.randomUUID().toString().toUpperCase();
		while(idSet.contains(check)){
			check = UUID.randomUUID().toString().toUpperCase();
		}
		idSet.add(check);
		return check;
	}

	/*
	Start this thread to check for connections
	 */
	private void start(){
		if (thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}

	/*
	Attempts to remove a client from the server
	 */
	public synchronized void remove(Client client){
		try{
			client.close();
			clients.remove(client);

			if (client.isDM()){
				client.getGame().closeGame();
				gameList.remove(client.getGame());
				gamesContext.remove(Util.ParamType.SYSTEM, client.getGame().getName());
			} else {
				client.getGame().remove(client);
			}
		} catch (IOException e){
			System.err.println("Error removing client.");
		}
	}

	//stop the thread
	public void stop(){
		thread = null;
	}
}