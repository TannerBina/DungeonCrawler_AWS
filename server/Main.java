/*
The main class of the server. Simply creates the server
and starts it, and then waits for others to connect to
games
 */
public class Main {
	public static void main(String[] args){
		Server.getInstance().startServer();
	}
}