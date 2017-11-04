import java.net.*;
import java.io.*;
/*
A test class used for testing different code sections
 */
public class Test {
	public static void main(String[] args){
		System.out.println("Testing Util FullCommand Command Fetch");
		System.out.println();
		String[] tests1 = new String[]{"$CREATEGAME", "$JOINGAME", "$JONGOAME", "$joingame", "$ join game", "$cretegame", "$joing ame", "$join gam", "$crejoingame"};
		for (String s : tests1){
			System.out.printf("Testing : %s\n", s);
			Util.FullCommand res = new Util.FullCommand(s, null);
			System.out.printf("Result : %s\n\n", res.command.name());
		}
		System.out.println("Finished Testing FullCommand Command Fetch");
		System.out.println();
		System.out.println();
		System.out.println("Testing Util FullCommand Param Fetch");
		System.out.println();
		Context context = new Context();
		context.put(Util.ParamType.SYSTEM, "GAMEXYZ");
		context.put(Util.ParamType.SYSTEM, "gamexyw");
		context.put(Util.ParamType.SYSTEM, "lods of Games");
		String[] tests2 = new String[]{"$JOINGAME gamexyz", "$jingame gamexyw", "$joingame gamexyq", "$join game lodf games", "crete game googlygame"};
		for (String s : tests2){
			System.out.printf("Testing : %s\n", s);
			Util.FullCommand res = new Util.FullCommand(s, context);
			System.out.printf("Result : \t Command : %s \t Parameters : ", res.command.name());
			for (String p : res.params){
				System.out.printf("%s,", p);
			}
			System.out.println();
			System.out.println();
		}
		System.out.println("Finished Testing Util FullCommand Param Fetch");

		System.out.println();
		System.out.println();

		System.out.println("Testing Server Connections");
		Server.getInstance().startServer();
		try{
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println();
			System.out.printf("Host address : %s\n", hostAddress);

			Socket s = new Socket(hostAddress, 5000);

			PrintWriter pw = new PrintWriter(s.getOutputStream());
			System.out.println();
			pw.write("This string input\n");
			pw.flush();
			pw.write("$joingame thatOneGame\n");
			pw.flush();
			pw.write("$creategame supergame\n");
			pw.flush();
		} catch (IOException e){
			System.out.println("Connection Failure");
		}
		try{
			Thread.sleep(1000);
		} catch (Exception e){
			System.err.println("Failed to sleep in test.");
		}

		System.out.println();

		System.out.println("Finished Testing Server Connections");
	}
}