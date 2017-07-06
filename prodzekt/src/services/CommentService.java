package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import utils.Config.Role;
import beans.Comment;
import beans.Report;
import beans.Subforum;
import beans.Topic;
import beans.User;
import database.Database;

@Path("/comments")
public class CommentService {
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;
	
	Database db = Database.getInstance();
	
	@GET
	@Path("/{commentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Topic getParentTopic(@PathParam("commentId") int id){
		return db.searchCommentById(id).getParentTopic();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getAllComments(){
		List<Topic> topics = new ArrayList<Topic>();
		for(Subforum subforum:db.getSubforums()){
			topics.addAll(subforum.getTopics());
		}
		List<Comment> comments = new ArrayList<Comment>();
		for(Topic topic: topics){
			comments.addAll(topic.getComments());
		}
		return comments;
	}
	
	@POST
	@Path("/{topicId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String addComment(@PathParam("topicId") int topicId, @FormParam("commentContent") String commentContent) {
		ArrayList<Subforum> subforums = (ArrayList<Subforum>) db.getSubforums();
		
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		if(user == null) {
			return "Must be logged in to comment topic!";
		}
		
		for(Subforum subforum : subforums) {
			for(Topic topic : subforum.getTopics()) {
				if(topic.getTopicId() == topicId) {
					Comment com = new Comment(user, commentContent, topic, null);
					topic.addComent(com);
					db.saveDatabase();
					
					return "Comment added!";
				}
			}
		}
		
		return "Error!";
		
	}
	
	@DELETE
	@Path("/{topicId}/{commentId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteComment(@PathParam("topicId") int topicId, @PathParam("commentId") int commentId) {
		ArrayList<Subforum> subforums = (ArrayList<Subforum>) db.getSubforums();
		
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			if(user.getRole() == Role.ADMIN || user.getRole() == Role.MODERATOR) {
				for(Subforum subforum : subforums) {
					for(Topic topic : subforum.getTopics()) {
						if(topic.getTopicId() == topicId) {
							
							Report report;
							if((report=db.searchReport(commentId))!=null){
								db.getReports().remove(report);
							}
							topic.deleteComment(commentId);
							
							db.saveDatabase();
							
							return "Comment deleted!";
						}
					}
				}
			}
		}
		
		return "Must be logged in to delete comment!";	
	}
	
	@POST
	@Path("/like/{subforumId}/{topicId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String likeComment(@PathParam("subforumId") int subforumId, @PathParam("topicId") int topicId, Comment com) {		
		HttpSession session = request.getSession();
		ArrayList<Subforum> subforums = (ArrayList<Subforum>) db.getSubforums();
		
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			for(Subforum subforum : subforums) {
				if(subforum.getSubforumId() == subforumId) {
					for(Topic topic : subforum.getTopics()) {
						if(topic.getTopicId() == topicId) {
							for(Comment comment : topic.getComments()) {
								if(com.getCommentId() == comment.getCommentId()) {
									if(user.getLikedComments().containsKey(comment.getCommentId())) {
										return "Comment already liked!";
									}
									comment.like();
									user.addLikedComment(comment.getCommentId(), comment.getText());
									if(user.getDislikedComments().containsKey(comment.getCommentId())) {
										user.getDislikedComments().remove(comment.getCommentId());
									}
									db.saveDatabase();
									return "Comment liked!";
								}
							}
						}
					}
				}
			}
		}
			
		return "Must be logged in to like the comment!";
	}
	
	@POST
	@Path("/save/{subforumId}/{topicId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String saveComment(@PathParam("subforumId") int subforumId, @PathParam("topicId") int topicId, Comment com) {		
		HttpSession session = request.getSession();
		ArrayList<Subforum> subforums = (ArrayList<Subforum>) db.getSubforums();
		
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			for(Subforum subforum : subforums) {
				if(subforum.getSubforumId() == subforumId) {
					for(Topic topic : subforum.getTopics()) {
						if(topic.getTopicId() == topicId) {
							for(Comment comment : topic.getComments()) {
								if(com.getCommentId() == comment.getCommentId()) {
									if(user.getSavedComments().containsKey(comment.getCommentId())) {
										return "Comment already saved!";
									}
									user.getSavedComments().put(com.getCommentId(), comment.getText());								
									
									db.saveDatabase();
									return "Comment saved!";
								}
							}
						}
					}
				}
			}
		}
			
		return "Must be logged in to save the comment!";
	}
	
	@POST
	@Path("/dislike/{subforumId}/{topicId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String dislikeComment(@PathParam("subforumId") int subforumId, @PathParam("topicId") int topicId, Comment com) {		
		HttpSession session = request.getSession();
		ArrayList<Subforum> subforums = (ArrayList<Subforum>) db.getSubforums();
		
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			for(Subforum subforum : subforums) {
				if(subforum.getSubforumId() == subforumId) {
					for(Topic topic : subforum.getTopics()) {
						if(topic.getTopicId() == topicId) {
							for(Comment comment : topic.getComments()) {
								if(com.getCommentId() == comment.getCommentId()) {
									if(user.getDislikedComments().containsKey(comment.getCommentId())) {
										return "Comment already disliked!";
									}
									comment.dislike();
									user.addDislikedComment(comment.getCommentId(), comment.getText());
									if(user.getLikedComments().containsKey(comment.getCommentId())) {
										user.getLikedComments().remove(comment.getCommentId());
									}
									db.saveDatabase();
									return "Comment disliked!";
								}
							}
						}
					}
				}
			}
		}
			
		return "Must be logged in to dislike the comment!";
	}
	
	@PUT
	@Path("/{subforumId}/{topicId}/{commentId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String editComment(@PathParam ("subforumId") int subforumId, @PathParam ("topicId") int topicId,
			@PathParam ("commentId") int commentId, @FormParam("editCommentContent") String editContent) {
		
		HttpSession session = request.getSession();
		ArrayList<Subforum> subforums = (ArrayList<Subforum>) db.getSubforums();
		
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			for(Subforum subforum : subforums) {
				if(subforum.getSubforumId() == subforumId) {
					for(Topic topic : subforum.getTopics()) {
						if(topic.getTopicId() == topicId) {
							for(Comment comment : topic.getComments()) {
								if(comment.getCommentId() == commentId) {
									if(comment.getText().equals(editContent)) {
										return "New comment is same as the old one!";
									}
									
									if (subforum.getResponsibleModerator().getUsername().equals(user.getUsername())) {
										// responsible moderator changed comment
										comment.setModified(false);
										comment.setModifiedData("");
										comment.setText(editContent);
										db.saveDatabase();
										return "Comment edited!";
									} else if(comment.getAuthor().getUsername().equals(user.getUsername())) {
										// User edited comment
										comment.setModified(true);
										comment.setModifiedData(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
										comment.setText(editContent);
										db.saveDatabase();
										return "Comment edited!";									
									} else {
										return "Must be creator of comment or responsible subforum moderator to edit comment!";
									}
																	
								}
							}
						}
					}
				}
			}
		}
		
		
		
		return "Must be logged in to change the comment";
	}
	
	
	
}
