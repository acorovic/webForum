package utils;

public class Config {
	public static enum Role {USER, MODERATOR, ADMIN};
	
	public static String DEFAULT_SAVE_LOCATION = System.getProperty("user.home") + "/";
}
