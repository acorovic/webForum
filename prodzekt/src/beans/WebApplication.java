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
		Comment com1 = new Comment(users.get(0), "ne valja", null, null);
		(subforums.get(0).getTopics()).get(0).addComent(com1);
		
		
	}
	
	private synchronized void loadTestData() {
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
		
		this.subforums.add(sub1);
		this.subforums.add(sub2);
		
	}
	
	
	public synchronized List<User> getUsers() {
		return this.users;
	}
	public synchronized void setUsers(List<User> users) {
		this.users = users;
	}
	public synchronized List<Subforum> getSubforums() {
		return this.subforums;
	}
	public synchronized void setSubforums(List<Subforum> subforums) {
		this.subforums = subforums;
	}
	
	

}
