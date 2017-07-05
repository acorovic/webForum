package services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import utils.Config;
import utils.Config.Role;
import database.Database;
import beans.Report;
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
						Report report;
						if((report=db.searchReport(subforumId))!=null){
							db.getReports().remove(report);
						}
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
	
	@POST
	@Path("/search/{keyWord}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public List<Subforum> searchName(@PathParam("keyWord") String keyWord, @FormParam("subforumCriteriaName") Boolean name,
			@FormParam("subforumCriteriaDescription") Boolean description, @FormParam("subforumCriteriaModerator") Boolean mod) {
		List<Subforum> returnVal = new ArrayList<Subforum>();
		// Setup unchecked fields
		if(description == null) {
			description = false;
		}
		if(name == null) {
			name = false;
		}
		if(mod == null) {
			mod = false;
		}

		for(Subforum subforum : db.getSubforums()) {
			if((subforum.getName().contains(keyWord) && name) || 
					(subforum.getDescription().contains(keyWord) && description) ||
					(subforum.getModerators().get(0).getUsername().contains(keyWord) && mod)) {
				returnVal.add(subforum);
			}
		}		
		
		if(returnVal.isEmpty()) {
			return null;
		} else {
			return returnVal;
		}
	}
	
	@POST
    @Path("/icon")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadIcon(InputStream uploadedInputStream) {
        String fileLocation = context.getRealPath("") + "\\";
        String imageId = UUID.randomUUID().toString()  + ".png";
        
        fileLocation += imageId;
        try {
        	File file = new File(fileLocation);
        	file.createNewFile();
            FileOutputStream out = new FileOutputStream(file, false);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageId;
    }
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String create(Subforum subforumToAdd) {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if(user != null) {
			if(user.getRole().equals(Role.MODERATOR) || user.getRole().equals(Role.ADMIN)) {

				if(!(subforumToAdd.getName().equals("") || subforumToAdd.getDescription().equals("") || subforumToAdd.getRules().equals(""))) {
					Subforum subforum = new Subforum(subforumToAdd.getName(), 
							subforumToAdd.getDescription(), 
							subforumToAdd.getIcon(),
							subforumToAdd.getRules(),  
							user);

					db.getSubforums().add(subforum);
					db.saveDatabase();
					return "Added a forum " + subforumToAdd.getName();
				}
				else {
					return "Name, description and rules are required fileds!";
				}
			}
			else {
				return "You do not have permission to create subforum!";
			}

		}
		else {
			return "Must be logged in to add subforum!";
		}
	}
	
	@POST
	@Path("/save/{subforumId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String saveSubforum(@PathParam("subforumId") int subforumId) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			if(user.getSavedSubforums().containsKey(subforumId)) {
				return "Already saved subforum!";
			}
			for(Subforum subforum : db.getSubforums()) {
				if(subforum.getSubforumId() == subforumId) {
					user.getSavedSubforums().put(subforumId, subforum.getName());
					db.saveDatabase();
					
					return "Subforum saved!";
				}
			}
		}
		
		return "Must be logged in to save the subforum!";
		
	}
	
	
}
