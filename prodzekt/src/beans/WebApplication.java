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
		users.add(user1);
		users.add(admin);
		
		Subforum sub1 = new Subforum("kuinja", "kuvanje", "nema ikonica", "nista", admin);
		Subforum sub2 = new Subforum("bicikli", "razni", "newm", "nema", admin);
		
		Topic top1 = new Topic("pasulj", admin, "kako napraviti", sub1.getName());
		Topic top2 = new Topic("riza", admin, "ne valja", sub1.getName());
		
		sub1.addTopic(top1);
		sub1.addTopic(top2);
		
		subforums.add(sub1);
		subforums.add(sub2);
		
		
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
