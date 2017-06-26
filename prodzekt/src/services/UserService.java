package services;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.User;
import database.Database;

@Path("/users")
public class UserService {
	
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;
	
	Database db = Database.getInstance();
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String register(@FormParam("username") String username,
							@FormParam("password") String password,
							@FormParam("email") String email,
							@FormParam("name") String name,
							@FormParam("surname") String surname,
							@FormParam("phoneNumber") String phoneNumber) {
		
		if(username == null || password == null || email == null 
				|| username == "" || password == "" || email == "") {
			return "Register form not completed. User cannot be registered!";
		} 
		if(db.searchUser(username) != null) {
			return "Username already taken. User cannot be registered!";
		} else {
			User user = new User(username, password, name, surname, email, phoneNumber);
			db.addUser(user);
			db.saveDatabase();
			return "Succesfully registered!";
		}
		
	}
	
}
