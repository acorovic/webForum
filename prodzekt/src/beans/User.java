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

	private List<Message> receivedMessages;
	
	private HashMap<Integer, String> likedTopics;
	private HashMap<Integer, String> dislikedTopics;
	private HashMap<Integer, String> likedComments;
	private HashMap<Integer, String> dislikedComments;
	private HashMap<Integer, String> savedSubforums;
	private HashMap<Integer, String> savedTopics;
	private HashMap<Integer, String> savedComments;
	
	public User() {
		super();
	}

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
		
		this.receivedMessages = new ArrayList<Message>();
		
		this.likedTopics = new HashMap<Integer, String>();	
		this.dislikedTopics = new HashMap<Integer, String>();	
		this.likedComments = new HashMap<Integer, String>();
		this.dislikedComments = new HashMap<Integer, String>();
		this.savedSubforums = new HashMap<Integer, String>();
		this.savedComments = new HashMap<Integer, String>();
		this.savedTopics = new HashMap<Integer, String>();
	}
	
	public void addDislike(int id, String name) {
		this.dislikedTopics.put(id, name);
	}
	
	public void addDislikedComment(int id, String name) {
		this.dislikedComments.put(id, name);
	}
	
	public void addLikedComment(int id, String name) {
		this.likedComments.put(id, name);
	}
	

	public HashMap<Integer, String> getSavedSubforums() {
		return savedSubforums;
	}

	public void setSavedSubforums(HashMap<Integer, String> savedSubforums) {
		this.savedSubforums = savedSubforums;
	}

	public HashMap<Integer, String> getSavedTopics() {
		return savedTopics;
	}

	public void setSavedTopics(HashMap<Integer, String> savedTopics) {
		this.savedTopics = savedTopics;
	}

	public HashMap<Integer, String> getSavedComments() {
		return savedComments;
	}

	public void setSavedComments(HashMap<Integer, String> savedComments) {
		this.savedComments = savedComments;
	}

	public HashMap<Integer, String> getLikedComments() {
		return likedComments;
	}

	public void setLikedComments(HashMap<Integer, String> likedComments) {
		this.likedComments = likedComments;
	}

	public HashMap<Integer, String> getDislikedComments() {
		return dislikedComments;
	}

	public void setDislikedComments(HashMap<Integer, String> dislikedComments) {
		this.dislikedComments = dislikedComments;
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
