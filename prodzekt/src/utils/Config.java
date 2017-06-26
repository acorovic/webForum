package utils;

public class Config {
	public static enum Role {USER, MODERATOR, ADMIN;
	
	@Override
	public String toString() {
		switch(this) {
		case USER:
			return "user";
		case MODERATOR:
			return "moderator";
		case ADMIN:
			return "admin";
		default:
			return "unknown role";
			}
	}
	};
	
	public static String DEFAULT_SAVE_LOCATION = System.getProperty("user.home") + "/";
}
