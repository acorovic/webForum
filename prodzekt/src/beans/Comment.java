package beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class Comment implements Serializable{
	private User author;
	private String date;
	private String text;
	private int likes;
	private int dislikes;
	private boolean modified;
	
	private String modifiedData;
	
	@JsonIgnore
	private Comment parentComment;
	//private List<Comment> childComments;
	
	@JsonBackReference
	private Topic parentTopic;
	
	private int commentId;
	
	public Comment() {
		super();
	}
	
	public Comment(User author, String text, Topic parentTopic, Comment parentComment) {
		super();
		this.author = author;
		this.text = text;
		this.parentTopic = parentTopic;
		this.parentComment = parentComment;
		
		this.date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		this.likes = 0;
		this.dislikes = 0;
		this.modified = false;
		
		//this.childComments = new ArrayList<Comment>();
		
		this.commentId = hashCode();
		this.modifiedData = "";
	}
	
	
	
	public String getModifiedData() {
		return modifiedData;
	}

	public void setModifiedData(String modifiedData) {
		this.modifiedData = modifiedData;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getCommentId() {
		return this.commentId;
	}
	
	public void like() {
		this.likes++;
	}
	
	public void dislike() {
		this.dislikes--;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	@JsonIgnore
	public Comment getParentComment() {
		return parentComment;
	}

	@JsonProperty
	public void setParentComment(Comment parentComment) {
		this.parentComment = parentComment;
	}
/*
	public List<Comment> getChildComments() {
		return childComments;
	}

	public void setChildComments(List<Comment> childComments) {
		this.childComments = childComments;
	}
*/	
	public Topic getParentTopic() {
		return parentTopic;
	}
	
	public void setParentTopic(Topic parentTopic) {
		this.parentTopic = parentTopic;
	}

	@Override
	public String toString() {
		return "Comment [author=" + author + ", date=" + date + ", text=" + text + ", likes=" + likes + ", dislikes="
				+ dislikes + ", modified=" + modified + "]";
	}
	
	
}
