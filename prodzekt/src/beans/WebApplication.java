package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utils.Config.Role;

@SuppressWarnings("serial")
public class WebApplication implements Serializable {
	private List<User> users;
	private List<Subforum> subforums;
	private List<Report> reports;
	
	
	
	public WebApplication() {
		super();
		this.users = new ArrayList<User>();
		this.subforums = new ArrayList<Subforum>();
		this.reports = new ArrayList<Report>();
		
		loadTestData();			
	}
	
	private synchronized void loadTestData() {
		User user1 = new User("user1", "user1", "name", "surname", "email", "phoneNumber");
		User admin = new User("admin", "admin", "name", "surname", "email", "phoneNumber");
		User mod1 = new User("mod1","mod1","name", "surname", "email", "phoneNumber");
		
		admin.setRole(Role.ADMIN);	
		mod1.setRole(Role.MODERATOR);
		users.add(user1);
		users.add(admin);
		users.add(mod1);
		
		Subforum sub1 = new Subforum("kuinja", "kuvanje", "slicica.png", "nista", admin);
		Subforum sub2 = new Subforum("bicikli", "razni", "slicica.png", "nema", mod1);
		
		Topic top1 = new Topic("pasulj", user1, "kako napraviti", Integer.toString(sub1.getSubforumId()));
		Topic top2 = new Topic("riza", admin, "ne valja", Integer.toString(sub1.getSubforumId()));
		
		Comment com1 = new Comment(admin, "testKomentar", top1, null);
		Comment com2 = new Comment(user1,"drugi teszt komentar poy",top1,null);
		
		Report rep1 = new Report(user1.getUsername(), "Test report", top1,top1.getTopicId());
		reports.add(rep1);
		
		top1.addComent(com1);
		top1.addComent(com2);
		
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

	public List<Report> getReports() {
		return this.reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}
}
