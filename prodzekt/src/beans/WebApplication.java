package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utils.Config.Role;

@SuppressWarnings("serial")
public class WebApplication implements Serializable {
	private List<User> users;
	private List<Subforum> subforums;
	
	
	
	public WebApplication() {
		super();
		this.users = new ArrayList<User>();
		this.subforums = new ArrayList<Subforum>();
		
		loadTestData();
	}
	
	private void loadTestData() {
		User user1 = new User("user1", "user1", "name", "surname", "email", "phoneNumber");
		User admin = new User("admin", "admin", "name", "surname", "email", "phoneNumber");
		
		admin.setRole(Role.ADMIN);	
		
	}
	
	
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<Subforum> getSubforums() {
		return subforums;
	}
	public void setSubforums(List<Subforum> subforums) {
		this.subforums = subforums;
	}
	
	

}
