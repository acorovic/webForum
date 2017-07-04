package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Subforum implements Serializable {
	private String name;
	
	private String description;
	private String icon;
	private String rules;
	
	private User responsibleModerator;
	private List<User> moderators;
	
	private List<Topic> topics;
	
	private int subforumId;
	
	public Subforum() {
		super();
	}
	
	public Subforum(String name, String description, String icon, String rules, User responsibleModerator) {
		super();
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.rules = rules;
		this.responsibleModerator = responsibleModerator;
		
		this.topics = new ArrayList<Topic>();
		
		this.moderators = new ArrayList<User>();
		this.subforumId = hashCode();
		moderators.add(responsibleModerator);
	}
	
	public int getSubforumId() {
		return this.subforumId;
	}

	public void addTopic(Topic topic) {
		topics.add(topic);
	}
	
	@Override
	public String toString() {
		return "Subforum [name=" + name + ", description=" + description + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public User getResponsibleModerator() {
		return responsibleModerator;
	}

	public void setResponsibleModerator(User responsibleModerator) {
		this.responsibleModerator = responsibleModerator;
	}

	public List<User> getModerators() {
		return moderators;
	}

	public void setModerators(List<User> moderators) {
		this.moderators = moderators;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}
	
	
	

}
