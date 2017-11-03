/*
A test class used for testing different code sections
 */
public class Test {
	public static void main(String[] args){
		System.out.println("Testing Util FullCommand Command Fetch");
		System.out.println();
		String[] tests = new String[]{"$CREATEGAME", "$JOINGAME", "$JONGOAME", "$joingame", "$ join game", "$cretegame", "$joing ame", "$join gam", "$crejoingame"};
		for (String s : tests){
			System.out.printf("Testing : %s\n", s);
			Util.FullCommand res = new Util.FullCommand(s, null);
			System.out.printf("Result : %s\n\n", res.command.name());
		}
		System.out.println("Finished Testing FullCommand Command Fetch");
		System.out.println();
		System.out.println();
	}
}