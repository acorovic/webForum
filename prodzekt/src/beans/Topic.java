package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class Topic implements Serializable{
	private String name;
	@JsonIgnore
	private User author;
	private String type;
	private String content;
	private String date;
	
	private int likes;
	private int dislikes;
	
	private String parentSubforum;
	
	private List<Comment> comments;
	
	private int topicId;
	
	public Topic() {
		super();
	}
	
	public Topic(String name, User author, String content, String parentSubforum) {
		super();
		this.name = name;
		this.author = author;
		this.content = content;
		this.parentSubforum = parentSubforum;
		
		this.likes = 0;
		this.dislikes = 0;
		
		this.date = (new Date()).toString();
		this.type = "text";
		
		this.topicId = hashCode();
		
		this.comments = new ArrayList<Comment>();
	}
	
	public void deleteComment(int commentId) {
		for(Comment comment : this.comments) {
			if(comment.getCommentId() == commentId) {
				this.comments.remove(comment);
				break;
			}
		}
	}
	
	public void addComent(Comment com) {
		comments.add(com);
	}
	
	public int getTopicId() {
		return this.topicId;
	}
	
	public void like() {
		this.likes++;
	}

	public void dislike() {
		this.dislikes++;
	}

	@Override
	public String toString() {
		return "Topic [name=" + name + ", author=" + author;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public User getAuthor() {
		return author;
	}

	@JsonProperty
	public void setAuthor(User author) {
		this.author = author;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getDislikes() {
		return dislikes;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}

	public String getParentSubforum() {
		return parentSubforum;
	}

	public void setParentSubforum(String parentSubforum) {
		this.parentSubforum = parentSubforum;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	
}
