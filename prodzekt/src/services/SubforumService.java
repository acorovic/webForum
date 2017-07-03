package services;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import utils.Config.Role;
import database.Database;
import beans.Subforum;
import beans.User;

@Path("/subforums")
public class SubforumService {
	
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;
	
	Database db = Database.getInstance();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Subforum> getSubforums() {
		return db.getSubforums();
	}
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String addSubforum(@FormParam("subforumName") String name, @FormParam("subforumDescription") String description,
								@FormParam("subforumRules") String rules) {
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		if(name == "" || name == null || description == "" || description == null
				|| rules == "" || rules == null) {
			return "All fields must be filled!";
		}
			
		
		if(user != null) {
			if(user.getRole() == Role.ADMIN || user.getRole() == Role.MODERATOR) {
				Subforum newSub = new Subforum(name, description, "none", rules, user);
				db.getSubforums().add(newSub);
				db.saveDatabase();
				
				return "New subforum succesfully created!";
			} else {
				return "Must be admin or moderator to create new subforum!";
			}
		}
		
		return "Must be logged in to create new subforum!";
	}

	@DELETE
	@Path("/{subforumId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteSubforum(@PathParam("subforumId") int subforumId) {
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			if(user.getRole() == Role.ADMIN || user.getRole() == Role.MODERATOR) {
				for(Subforum subforum : db.getSubforums()) {
					if(subforum.getSubforumId() == subforumId) {
						db.getSubforums().remove(subforum);
						break;
					}
				}
				db.saveDatabase();
				
				return "Subforum succesfully deleted!";
			} else {
				return "Must be admin or moderator to delete subforum!";
			}
		}
		
		return "Must be logged in to delete subforum!";
		
	}
	
	
}
