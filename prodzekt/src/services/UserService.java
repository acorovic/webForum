package services;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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
			return "Successfully registered!";
		}	
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@FormParam("username") String username, @FormParam("password") String password) {
		HttpSession session = request.getSession();
		
		if(session.getAttribute("user") != null) {
			return "Already logged in!";
		}
		
		if(username == null || password == null || username == "" || password == "") {
			return "Login form not completed. User cannot be logged in!";
		}	
		
		User user;
		
		if((user = db.searchUser(username)) != null) {
			if(user.getPassword().equals(password)) {
				session.setAttribute("user", user);
				//db.saveDatabase();
				return "Successfully logged in!";
			} else {
				return "Login failed!";
			}			
		}
		
		return "Something went wrong. User cannot be logged in!";
	}
	
	@GET
	@Path("/logout")
	@Produces(MediaType.TEXT_PLAIN)
	public String logut() {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			session.invalidate();
			return "Logged out!";
		} else {
			return "Already logged out!";
		}
	}
	
	
	@GET
	@Path("/test")
	public String test() {
		return "test";
	}
	
}
