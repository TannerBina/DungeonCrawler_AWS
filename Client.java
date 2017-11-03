import java.io.*;
import java.net.*;

/*
Client class for sending messages from server
to the client and server receiving messages
 */
public class Client extends Thread implements Comparable<Client> {
	//a given clients id
	private String id;
	//the socket for communication
	private Socket socket;
	//reference to the server from the client
	private Server server;
	//reader and sender
	private BufferedReader reader;
	private PrintWriter sender;

	//the game this client is currently in
	private Game game;

	//flag for whether or not the client is a dm
	private boolean dm;

	/*
	Creates the client from the socket
	that is inputted
	 */
	public Client(Socket socket, Server server, String id){
		super();
		//set the socket and attempt to open it
		this.socket = socket;
		this.server = server;
		this.id = id;
		dm = false;
		try{
			open();
			start();
		} catch (IOException e){
			System.err.println("Failed to open client stream.");
		}
	}

	public void run(){
		while(true){
			try{
				server.handle(reader.readLine(), this);
			} catch(IOException e){
				System.err.println("Error reading, removing client.");
				server.remove(this);
			}
		}
	}

	//checks if the client is a dm
	public boolean isDM(){return dm;}

	//sets the game this client is in
	public void setGame(Game g){
		game = g;
	}

	//returns the game the client is in
	public Game getGame(){return game;}

	/*
	Sends the inputted string across the writer to the client
	 */
	public void send(String s){
		sender.write(s + '\n');
		sender.flush();
	}

	/*
	Attempts to open the reader and sender
	 */
	private void open() throws IOException{
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        sender = new PrintWriter(socket.getOutputStream());
	}

	/*
	Close the reader, sender and socket
	 */
	public void close() throws IOException{
		reader.close();
		sender.close();
		socket.close();
	}

	public int compareTo(Client other){
		return id.compareTo(other.id);
	}

	public boolean equals(Client other){
		return id.equals(other.id);
	}
}