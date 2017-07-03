package services;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Message;
import beans.User;
import database.Database;

@Path("/messages")
public class MessageService {
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;
	
	Database db = Database.getInstance();
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String sendMessage(@FormParam("username") String receiverId, @FormParam("content") String content) {
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			User receiver = db.searchUser(receiverId);
			
			if(receiver != null) {
				Message message = new Message(user.getUsername(), receiver.getUsername(), content);
				receiver.addMessage(message);
				db.saveDatabase();
				
				return "Message sent!";
			} else {
				return "Receiver doesn't exists!";
			}
		} else {
			return "Cannot send message! Not logged in!";
		}	
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMessages() {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			return user.getReceivedMessages();
		} else {
			return null;
		}
	}
	
	

}
