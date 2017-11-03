import java.net.*;
import java.io.*;
import java.util.*;

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

	private Vector<Client> clients;

	//private constructor
	private Server(){clients = new Vector<>();}

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
			} catch (IOException e){
				System.err.println("Server accept error.");
				stop();
			}
		}
	}

	/*
	Handles a sent string from a client
	 */
	public synchronized void handle(String s, Client client){
		
	}

	/*
	Adds a client with the given socket to the list
	 */
	private void addClient(Socket socket){
		clients.add(new Client(socket, this));
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
		} catch (IOException e){
			System.err.println("Error removing client.");
		}
	}

	//stop the thread
	public void stop(){
		thread = null;
	}
}