package services;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
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
	
}
