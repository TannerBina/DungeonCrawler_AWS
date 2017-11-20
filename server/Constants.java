import java.util.*;

public class Constants {
	public static final boolean DEBUG = true;
	
	public static final String LAMBDA_DELIMINATOR = "__";
	public static final String CHAR_ID_TAG = "CHAR_ID";
	public static final String NULL = "NULL";

	public enum  Background{
		ACOLYTE(new Character.StatTag[]{Character.StatTag.INSIGHT, Character.StatTag.RELIGION}),
		CHARLATAN(new Character.StatTag[]{Character.StatTag.DECEPTION, Character.StatTag.SLEIGHT_OF_HAND}),
		CRIMINAL(new Character.StatTag[]{Character.StatTag.DECEPTION, Character.StatTag.STEALTH}),
		ENTERTAINER(new Character.StatTag[]{Character.StatTag.ACROBATICS, Character.StatTag.PERFORMANCE}),
		FOLK_HERO(new Character.StatTag[]{Character.StatTag.ANIMAL_HANDLING, Character.StatTag.SURVIVAL}),
		GUILD_ARTISAN(new Character.StatTag[]{Character.StatTag.INSIGHT, Character.StatTag.PERSUASION}),
		HERMIT(new Character.StatTag[]{Character.StatTag.MEDICINE, Character.StatTag.RELIGION}),
		NOBLE(new Character.StatTag[]{Character.StatTag.HISTORY, Character.StatTag.PERSUASION}),
		OUTLANDER(new Character.StatTag[]{Character.StatTag.ATHLETICS, Character.StatTag.SURVIVAL}),
		SAGE(new Character.StatTag[]{Character.StatTag.ARCANA, Character.StatTag.HISTORY}),
		SAILOR(new Character.StatTag[]{Character.StatTag.ATHLETICS, Character.StatTag.PERCEPTION}),
		SOLDIER(new Character.StatTag[]{Character.StatTag.ATHLETICS, Character.StatTag.INTIMIDATION}),
		URCHIN(new Character.StatTag[]{Character.StatTag.SLEIGHT_OF_HAND, Character.StatTag.STEALTH});


		public final ArrayList<Character.StatTag> bonusList;
		Background(Character.StatTag[] bonuses){
			bonusList = new ArrayList<>();
			for (Character.StatTag s : bonuses){
				bonusList.add(s);
			}
		}
		public int getBonus(Character.StatTag tag){
			if (bonusList.contains(tag)) return 1;
			return 0;
		}
	};
}
