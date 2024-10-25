package cse360project.utils;

/**
 * Enum for the different levels an article can have
 */
public enum Level{
	BEGINNER,
	INTERMEDIATE,
	ADVANCED,
	EXPERT;

	  /**
	   * convert an enum level into a string specifying the level
	   * @param level the level of the article
	   * @return a string specifying the level of the article
	   */
	public static String levelToString(Level level) {
		if (level == Level.BEGINNER) {
			return "BEGINNER";
		}
		if (level == Level.INTERMEDIATE) {
			return "INTERMEDIATE";
		}
		if (level == Level.ADVANCED) {
			return "ADVANCED";
		}
		if (level == Level.EXPERT) {
			return "EXPERT";
		}
		return null;
		
	}
	
	  /**
	   * convert an string specifying a level into a level
	   * @param level a string that specifies level of the article
	   * @return a Level of the article
	   */
	public static Level stringToLevel(String level) {
		if (level.toUpperCase().equals("BEGINNER")) {
			return Level.BEGINNER;
		}
		if (level.toUpperCase().equals("INTERMEDIATE")) {
			return Level.INTERMEDIATE;
		}
		if (level.toUpperCase().equals("ADVANCED")) {
			return Level.ADVANCED;
		}
		if (level.toUpperCase().equals("EXPERT")) {
			return Level.EXPERT;
		}
		return null;
	}
}


