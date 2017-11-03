import java.util.*;

/*
Utils class for all stand alone methods such as interpretting,
converting, and handling commands.

Holds all singular static methods
 */
public class Util{

	/*
	A list of different parameter types
	for an inputted command, used for the language
	interpretter
	 */
	public enum ParamType{
		SYSTEM,

		NONE
	};

	/*
	A command and the inputted number of 
	parameters that should follow the command,
	0 represents an infinite number
	 */
	public enum Command {
		NULL(0, ParamType.NONE),
		CREATEGAME(1, ParamType.SYSTEM),
		JOINGAME(1, ParamType.SYSTEM);

		public final int numParams;
		public final ParamType type;
		Command(int numParams, ParamType type){
			this.numParams = numParams;
			this.type = type;
		}
	};

	/*
	A structure class that holds a command and the parameters
	that are inputted to it.
	 */
	public static class FullCommand{
		//the percentage a command has to be within a set one to be recognized
		//rounds up
		private static final double DIST_THRESHHOLD = 0.2;

		public Command command;
		public ArrayList<String> params;
		public FullCommand(){}
		public FullCommand(Command command, ArrayList<String> params){
			this.command = command;
			this.params = params;
		}
		//creates a full command off the given data and context,
		//returns a NULL command if command is unrecognized
		public FullCommand(String data, Context context){
			//set data to uppercase and remove spaces
			data = data.toUpperCase().replace(" ", "");
			//build the string from substring without $
			StringBuilder sb = new StringBuilder(data.substring(1, data.length()));

			command = getCommand(sb);
		}

		//gets the command part of the inputted data
		//removes the command from the inputted string as well
		//returns NULL if command not recognized
		private static Command getCommand(StringBuilder data){
			int endIndex = 0;
			int minDif = Integer.MAX_VALUE;
			Command min = Command.NULL;
			//settup invalid command array
			ArrayList<Command> invalid = new ArrayList<>();
			//loops through each possible size of a command
			for (int i = 1; i < data.length()+1; i++){
				//get the substring of the current size
				String sub = data.toString().substring(0, i);
				//calc the max difference possible for two strings to be valud
				int maxDif = (int)(sub.length() * DIST_THRESHHOLD +.99);
				//loop through commands
				for (Command c : Command.values()){
					if (c == Command.NULL || invalid.contains(c)) continue;
					//get difference in length
					int lengthDif = Math.abs(c.name().length() - sub.length());
					//dont check if lengthdif auto disqualifies
					if (lengthDif < minDif && lengthDif <= maxDif){
						//get distance
						int dif = distance(c.name(), sub);
						//if perfect match return immediately.
						if (dif == 0){ 
							data.delete(0, i);
							return c;
						}
						//otherwise if it falls within accepted bound
						//set values
						if (dif < minDif && dif <= maxDif){
							endIndex = i;
							minDif = dif;
							min = c;
						}

						//if the dif is ever greater than max dif + lengthDif,
						//set as invalid as cannot be command
						if (dif > maxDif+lengthDif){
							invalid.add(c);
						}
					}
				}
			}

			//delete section and return
			data.delete(0, endIndex);
			return min;
		}
	}

	/*
	Finds the distance between two inputted strings
	 */
	private static int distance(String s1, String s2){
		//init distance array
		int[][] values = new int[s1.length()+1][s2.length()+1];
		//set edge values to dist
		for (int i = 0; i < s1.length(); i++){
			values[i][0] = i;
		}
		for (int i = 0; i < s2.length(); i++){
			values[0][i] = i;
		}

		//loop through and populate array
		for (int i = 0; i < s1.length(); i++){
			for (int j = 0; j < s2.length(); j++){
				//if char is equal, inherit diagnal, no gain
				if (s1.charAt(i) == s2.charAt(j)){
					values[i+1][j+1] = values[i][j];
				//otherwise inherit with +1 from sides
				} else {
					values[i+1][j+1] = min(values[i][j]+1, values[i][j+1]+1,
						values[i+1][j]+1);
				}
			}
		}

		//return the min value
		return values[s1.length()][s2.length()];
	}

	 /*
    Return the min of three numbers
     */
    private static int min(int one, int two, int three){
        if (one <= two && one <= three){
            return one;
        }
        if (two <= one && two <= three){
            return two;
        }
        if (three <= one && three <= two){
            return three;
        }
        System.err.println("Could not find minimum of three numbers");
        return 0;
    }
}