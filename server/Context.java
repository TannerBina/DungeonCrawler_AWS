import java.util.*;

/*
A context that includes all data
and param possiblities based on the current
context that commands are being processed in.

Created for every scenario people are in in a game,
changes dynamically throughout.
 */
public class Context {

	private Map<Integer, ArrayList<String>> contextMap;

	//empty constructor for a context
	public Context(){
		//init the context map
		for (int i = 0; i < Util.ParamType.NONE.ordinal(); i++){
			contextMap.put(i, new ArrayList<>());
		}
	}

	//gets a specified context list of the param type
	public ArrayList<String> get(Util.ParamType type){
		return contextMap.get(type.ordinal());
	}

	//places a specified id string in the context list
	public void put(Util.ParamType type, String data){
		contextMap.get(type.ordinal()).add(data);
	}
}