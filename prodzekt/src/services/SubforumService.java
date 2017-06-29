package services;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import database.Database;
import beans.Subforum;

@Path("/subforums")
public class SubforumService {
	
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;
	
	Database db = Database.getInstance();
	
	@GET
	@Path("/getSubforums")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Subforum> getSubforums() {
		return db.getSubforums();
	}
	
}
