package services;

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
import beans.Subforum;
import beans.Topic;
import beans.User;
import database.Database;

@Path("/topics")
public class TopicService {

	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;
	
	Database db = Database.getInstance();
	
	@POST
	@Path("/{subforumId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String addTopic(@PathParam("subforumId") int subforumId, @FormParam("topicName") String topicName,
							@FormParam("topicContent") String topicContent) {
		
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		if(topicName == "" || topicName == null || topicContent == "" || topicContent == null) {
			return "All fields must be filled!";
		}
		
		if(user != null) {
			for(Subforum subforum : db.getSubforums()) {
				if(subforum.getSubforumId() == subforumId) {
					Topic topic = new Topic(topicName, user, topicContent, Integer.toString(subforumId));
					subforum.addTopic(topic);
					db.saveDatabase();
					
					return "Topic created!";
				}
			}
		}
		
		return "Must be logged in to create topic!";
	}
	
	@DELETE
	@Path("/{subforumId}/{topicId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteTopic(@PathParam("subforumId") int subforumId, @PathParam("topicId") int topicId) {
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			if(user.getRole() == Role.ADMIN || user.getRole() == Role.MODERATOR) {
				for(Subforum subforum : db.getSubforums()) {
					if(subforum.getSubforumId() == subforumId) {
						for(Topic topic : subforum.getTopics()) {
							if(topic.getTopicId() == topicId) {
								subforum.getTopics().remove(topic);
								db.saveDatabase();
								
								return "Topic deleted!";
							}
						}
					}
				}
				
			} else {
				return "Must be admin or moderator to delete topic";
			}
		}
		
		
		return "Must be logged in to delete topic!";
	}
	
}
