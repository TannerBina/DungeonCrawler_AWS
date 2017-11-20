/*
The main class of the server. Simply creates the server
and starts it, and then waits for others to connect to
games
 */
public class Main {

	public static boolean DEBUG;

	public static void main(String[] args){
		DEBUG = false;

		if (args.length > 0){
			if (args[0].equals("debug")){
				System.out.println("DEBUG ON");
				DEBUG = true;
			}
		}

		Server.getInstance().startServer();
	}
}