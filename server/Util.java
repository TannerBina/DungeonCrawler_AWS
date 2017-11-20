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

	public enum ResponseCommand {
		SETCHAR, SENDCHAR, ADDCHAR, REMOVECHAR
	}

	/*
	A command and the inputted number of 
	parameters that should follow the command,
	0 represents an infinite number
	 */
	public enum Command {
		NULL(0, new ParamType[]{}, false),
		//note that empty paramtypes imply only 1 param
		CREATEGAME(1, new ParamType[]{}, true),
		JOINGAME(1, new ParamType[]{ParamType.SYSTEM}, false),
		SETCHAR(2, new ParamType[]{ParamType.SYSTEM}, false);

		public final int numParams;
		public final ParamType[] types;
		public final boolean dmOnly;
		Command(int numParams, ParamType[] types, boolean dmOnly){
			this.numParams = numParams;
			this.types = types;
			this.dmOnly = dmOnly;
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
			params = new ArrayList<>();
			//set data to uppercase and remove spaces
			data = data.toUpperCase().replace(" ", "");
			//build the string from substring without $ or @
			StringBuilder sb = new StringBuilder(data.substring(1, data.length()));

			command = getCommand(sb);

			//if the command is a dmOnly command and does not
			//come from a dm, return null command
			if (command.dmOnly){
				if (data.charAt(0) != '@'){
					command = Command.NULL;
					return;
				}
			}

			//if the command is null or has no parameters return
			if (command == Command.NULL || command.numParams == 0){
				return;
			}

			//if there are no param types, assume 1 param
			//and fill with all remaining info
			if (command.types.length == 0 || context == null){
				params.add(sb.toString());
				return;
			}

			//loop through each parameter, adding it to the list
			for (int i = 0; i < command.numParams; i++){
				//if there is no more types for paramters, assume the rest of the string
				//is a final parameters
				if (i >= command.types.length){
					params.add(sb.toString());
					return;
				} else {
					params.add(getParam(sb, context.get(command.types[i])));
				}
			}
		}

		//gets the parameter from the inputted list of params
		//removes the parameter from data string
		//return "NULL" string if no parameter
		private static String getParam(StringBuilder data, ArrayList<String> params){
			int endIndex = 0;
			int minDif = Integer.MAX_VALUE;
			String min = "NULL";
			//settup invalid string array
			ArrayList<String> invalid = new ArrayList<>();
			//loops through each possible size of a string
			for (int i = 1; i < data.length()+1; i++){
				//get substring of current size
				String sub = data.toString().substring(0, i);
				//calc the max dif possible for two strings to be valid
				int maxDif = (int)(sub.length() * DIST_THRESHHOLD + .99);
				//loop through strings
				for (String s : params){
					if (s == "NULL" || invalid.contains(s)) continue;
					//get difference in length
					int lengthDif = Math.abs(s.length() - sub.length());
					//check if falls within possible bounds
					if (lengthDif < minDif && lengthDif <= maxDif){
						//get difference
						int dif = distance(s, sub);
						//return if perfect match
						if (dif == 0){
							data.delete(0, i);
							return s;
						}

						//otherwise if it falls within accepted bound
						//set values
						if (dif <= minDif && dif <= maxDif){
							endIndex = i;
							minDif = dif;
							min = s;
						}

						//if the dif is ever greater than max dif + lengthDif,
						//set as invalid as cannot be command
						if (dif > maxDif+lengthDif){
							invalid.add(s);
						}
					}
				}
			}

			//delete section and return
			data.delete(0, endIndex);
			return min;
		}

		//gets the command part of the inputted data
		//removes the command from the inputted string as well
		//returns NULL if command not recognized
		private static Command getCommand(StringBuilder data){
			ArrayList<String> commandOpts = new ArrayList<>();
			for (Command c : Command.values()){
				commandOpts.add(c.name());
			}
			String res = getParam(data, commandOpts);
			return Command.valueOf(res);
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

    /*
    Gets the proficiency bonus of a class
    given an entered string containing the level
    of a character
     */
    public static int getProfBonus(String level){
    	try{
    		int lvl = Integer.parseInt(level);
    		return (lvl-1)/4 + 2;
    	} catch(Exception e){
    		System.out.println("ERROR : Not int in level for Util.getProfBonus : level");
    		return -1;
    	}
    }
}