package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import utils.Config.Role;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String username;
	private String password;
	private String name;
	private String email;
	private String surname;
	private String phoneNumber;
	private String registrationDate;
	
	private Role role;
	
	private List<Subforum> followedSubforums;
	private List<Topic> savedTopics;
	private List<Comment> savedComments;
	private List<Message> receivedMessages;
	
	private HashMap<Integer, String> likedTopics;
	private HashMap<Integer, String> dislikedTopics;

	public User(String username, String password, String name, String surname, String email, String phoneNumber) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.surname = surname;
		this.phoneNumber = phoneNumber;
		
		this.registrationDate = (new Date()).toString();
		this.role = Role.USER;
		
		this.followedSubforums = new ArrayList<Subforum>();
		this.savedTopics = new ArrayList<Topic>();
		this.savedComments = new ArrayList<Comment>();
		this.receivedMessages = new ArrayList<Message>();
		
		this.likedTopics = new HashMap<Integer, String>();	
		this.dislikedTopics = new HashMap<Integer, String>();	
	}
	
	public void addDislike(int id, String name) {
		this.dislikedTopics.put(id, name);
	}
	

	public HashMap<Integer, String> getDislikedTopics() {
		return dislikedTopics;
	}



	public void setDislikedTopics(HashMap<Integer, String> dislikedTopics) {
		this.dislikedTopics = dislikedTopics;
	}



	public void addLike(int id, String name) {
		this.likedTopics.put(id, name);
	}


	public HashMap<Integer, String> getLikedTopics() {
		return likedTopics;
	}

	public void setLikedTopics(HashMap<Integer, String> likedTopics) {
		this.likedTopics = likedTopics;
	}


	public void addMessage(Message message) {
		this.receivedMessages.add(message);
	}


	
	public List<Message> getReceivedMessages() {
		return receivedMessages;
	}

	public void setReceivedMessages(List<Message> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}

	public List<Subforum> getFollowedSubforums() {
		return followedSubforums;
	}

	public void setFollowedSubforums(List<Subforum> followedSubforums) {
		this.followedSubforums = followedSubforums;
	}

	public List<Topic> getSavedTopics() {
		return savedTopics;
	}

	public void setSavedTopics(List<Topic> savedTopics) {
		this.savedTopics = savedTopics;
	}

	public List<Comment> getSavedComments() {
		return savedComments;
	}

	public void setSavedComments(List<Comment> savedComments) {
		this.savedComments = savedComments;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password
				+ ", name=" + name + ", email=" + email + ", surname="
				+ surname + ", phoneNumber=" + phoneNumber + ", role=" + role
				+ "]";
	}
	
	
	
}
