package database;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import utils.Config;
import beans.Subforum;
import beans.User;
import beans.WebApplication;

public class Database {
	
	public String DATABASE_FILENAME = "webAppDb.bin";
	
	private WebApplication application;
	
	private static Database instance = null;
	
	private Database() {
		super();
		File dbFile = new File(Config.DEFAULT_SAVE_LOCATION + DATABASE_FILENAME);
		if(!dbFile.exists()) {
			application = new WebApplication();
			saveDatabase();
		} else {
			loadDatabase();
		}
	}
	
	public synchronized static Database getInstance() {
		if(instance == null) {
			instance = new Database();
		}
		
		return instance;
	}
	
	public synchronized void saveDatabase() {
		ObjectOutputStream out = null;
		
		try {
			out = new ObjectOutputStream(
						new BufferedOutputStream(
								new FileOutputStream(Config.DEFAULT_SAVE_LOCATION + DATABASE_FILENAME)));
			
			out.writeObject(application);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public synchronized void loadDatabase() {
		ObjectInputStream in = null;
		
		try {
			in = new ObjectInputStream(
						new BufferedInputStream(
								new FileInputStream(Config.DEFAULT_SAVE_LOCATION + DATABASE_FILENAME)));
		
			application = ((WebApplication) in.readObject());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	public synchronized List<Subforum> getSubforums() {
		return application.getSubforums();
	}
	
	public synchronized void setSubforums(List<Subforum> subforums) {
		application.setSubforums(subforums);
	}
	
	public synchronized List<User> getUsers() {
		return application.getUsers();
	} 
	
	public synchronized void setUsers(List<User> users) {
		application.setUsers(users);
	}
	
	public synchronized User searchUser(String username) {
		for(User user : application.getUsers()) {
			if(user.getUsername().equals(username)) {
				return user;
			}
		}
		
		return null;
	}
	
	public synchronized void addUser(User user) {
		application.getUsers().add(user);
		saveDatabase();
	}
	
	public synchronized boolean changeUserRole(String username, Config.Role role) {
		for(User user : application.getUsers()) {
			if(user.getUsername().equals(username)) {
				user.setRole(role);
				return true;
			}
		}
		
		return false;
	}
	
	
	
}
