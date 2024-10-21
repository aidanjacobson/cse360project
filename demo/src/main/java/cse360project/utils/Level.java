package cse360project.utils;

/**
 * Enum for the different levels a user can have
 */
public enum Level{
	BEGINNER,
	INTERMEDIATE,
	ADVANCED,
	EXPERT;

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
	
	public static Level stringToLevel(String level) {
		if (level.toUpperCase() == "BEGINNER") {
			return Level.BEGINNER;
		}
		if (level.toUpperCase() == "INTERMEDIATE") {
			return Level.INTERMEDIATE;
		}
		if (level.toUpperCase() == "ADVANCED") {
			return Level.ADVANCED;
		}
		if (level.toUpperCase() == "EXPERT") {
			return Level.EXPERT;
		}
		return null;
	}
}


