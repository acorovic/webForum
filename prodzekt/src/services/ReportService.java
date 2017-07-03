package services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

import beans.*;
import beans.Report.ObjectClass;
import database.Database;
import utils.Config.Role;

@Path("/reports")
public class ReportService {
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;
	
	Database db = Database.getInstance();
	
	@POST
	@Path("/{reportId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String sendReport(@FormParam("content") String content,@PathParam("reportId") int id) {
		
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		if(user != null) {
			Report toSend = null;
			Subforum subforum;
			Topic topic;
			Comment comment;
			String text = "";
			if((subforum = db.searchSubforumById(id))!=null){
				toSend = new Report(user.getUsername(),content,subforum,id);
				text = "Subforum reported successfully!";
			}else if((topic = db.searchTopicById(id))!=null){
				toSend = new Report(user.getUsername(),content,topic,id);
				text = "Topic reported successfully!";
			}else if((comment = db.searchCommentById(id))!=null){
				toSend = new Report(user.getUsername(),content,comment,id);
				text = "Comment reported successfully!";
			}
			
			db.addReport(toSend);
			
			return text;
		} else {
			return "Cannot file a report! Not logged in!";
		}	
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Report> getReports() {
		
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");

		List<Report> reports = new ArrayList<Report>();
		if(user != null) {
			if(user.getRole() == Role.ADMIN){
				reports = db.getReports();
			}else if(user.getRole() == Role.MODERATOR){
				for(Report report:db.getReports()){
					if(report.getType()==ObjectClass.COMMENT){
						if(db.searchSubforumById(Integer.parseInt(((Comment)report.getReportObject()).getParentTopic().getParentSubforum())).getResponsibleModerator().equals(user)){
							reports.add(report);
						}
					}else if(report.getType()==ObjectClass.TOPIC){
						if(db.searchSubforumById(Integer.parseInt(((Topic)report.getReportObject()).getParentSubforum())).getResponsibleModerator().equals(user)){
							reports.add(report);
						}
					}
				}
			}
		}
		return reports;
	}
	
	@DELETE
	@Path("/{reportId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteReportedObject(@PathParam("reportId") int id){
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Report report = db.searchReport(id);
		
		if(report.getType()==ObjectClass.SUBFORUM){
			User responsible = ((Subforum)report.getReportObject()).getResponsibleModerator();
			String text = "User " + report.getOffended() + " reported your subforum ( " + ((Subforum)report.getReportObject()).getName() + " ). It is violating forum rules and is being deleted.";
			Message message = new Message(user.getUsername(),responsible.getUsername(),text);
			responsible.addMessage(message);
			db.getSubforums().remove(db.searchSubforumById(id));
			db.saveDatabase();
			String text1 = "Subforum you reported ( " + ((Subforum)report.getReportObject()).getName() + " ) was found violating forum rules and was removed. Thank you for your support.";
			Message message1 = new Message(user.getUsername(),report.getOffended(),text1);
			db.searchUser(report.getOffended()).addMessage(message1);
			report.setProcessed(true);
			return "Subforum " + ((Subforum)report.getReportObject()).getName() + " deleted!";
		}else if(report.getType()==ObjectClass.TOPIC){
			User responsible = ((Topic)report.getReportObject()).getAuthor();
			String text = "User " + report.getOffended() + " reported your topic (" + ((Topic)report.getReportObject()).getName() + "). It is violating forum rules and is being deleted.";
			Message message = new Message(user.getUsername(), responsible.getUsername(), text);
			responsible.addMessage(message);
			db.searchSubforumById(Integer.parseInt(((Topic)report.getReportObject()).getParentSubforum())).getTopics().remove(((Topic)report.getReportObject()));
			db.saveDatabase();
			String text1 = "Topic you reported (" + ((Topic)report.getReportObject()).getName() + ") was found violating forum rules and was removed. Thank you for your support.";
			Message message1 = new Message(user.getUsername(),report.getOffended(),text1);
			db.searchUser(report.getOffended()).addMessage(message1);
			report.setProcessed(true);
			return "Topic " + ((Topic)report.getReportObject()).getName() + " deleted!";
		}else{
			User responsible = ((Comment)report.getReportObject()).getAuthor();
			String text = "User " + report.getOffended() + " reported your comment on topic " + ((Comment)report.getReportObject()).getParentTopic().getName() + ". It is violating forum rules and is being deleted.";
			Message message = new Message(user.getUsername(), responsible.getUsername(), text);
			responsible.addMessage(message);
			((Comment)report.getReportObject()).getParentTopic().getComments().remove(((Comment)report.getReportObject()));
			db.saveDatabase();
			String text1 = "Comment you reported ( Topic : " + ((Comment)report.getReportObject()).getParentTopic().getName() + ") was found violating forum rules and was removed. Thank you for your support.";
			Message message1 = new Message(user.getUsername(),report.getOffended(),text1);
			db.searchUser(report.getOffended()).addMessage(message1);
			report.setProcessed(true);
			return "Comment deleted!";
		}
	}
	
	@POST
	@Path("/{reportId}/warn")
	@Produces(MediaType.TEXT_PLAIN)
	public String warnObjectAuthor(@PathParam("reportId")int id){
		
		HttpSession session = request.getSession();	
		User user = (User) session.getAttribute("user");
		Report report = db.searchReport(id);
		String text1;
		
		if(report.getType()==ObjectClass.SUBFORUM){
			System.out.println("SUBFORUM");
			User toBeWarned = ((Subforum)report.getReportObject()).getResponsibleModerator();
			String text = "User " + report.getOffended() + " reported your subforum ( " + ((Subforum)report.getReportObject()).getName() + " ). Make sure you are not violating forum rules.";
			Message message = new Message(user.getUsername(),toBeWarned.getUsername(),text);
			toBeWarned.addMessage(message);
			text1 = "Author of subforum you reported ( " + ((Subforum)report.getReportObject()).getName() + " ) was warned about violating forum rules. Thank you for your support.";
		}else if(report.getType()==ObjectClass.TOPIC){
			System.out.println("TOPIC");
			User toBeWarned = ((Topic)report.getReportObject()).getAuthor();
			String text = "User " + report.getOffended() + " reported your topic ( " + ((Topic)report.getReportObject()).getName() + " ). Make sure you are not violating forum rules.";
			Message message = new Message(user.getUsername(),toBeWarned.getUsername(),text);
			toBeWarned.addMessage(message);
			text1 = "Author of topic you reported ( " + ((Topic)report.getReportObject()).getName() + " ) was warned about violating forum rules. Thank you for your support.";
		}else{
			System.out.println("COMMENT");
			User toBeWarned = ((Comment)report.getReportObject()).getAuthor();
			String text = "User " + report.getOffended() + " reported your comment on topic " + ((Comment)report.getReportObject()).getParentTopic().getName() + ". Make sure you are not violating forum rules.";
			Message message = new Message(user.getUsername(),toBeWarned.getUsername(),text);
			toBeWarned.addMessage(message);
			text1 = "Author of comment you reported ( Topic : " + ((Comment)report.getReportObject()).getParentTopic().getName() + " ) was warned about violating forum rules. Thank you for your support.";	
		}
		Message message1 = new Message(user.getUsername(),report.getOffended(),text1);
		db.searchUser(report.getOffended()).addMessage(message1);
		report.setProcessed(true);
		return "Author warned!";
	}
	
	@POST
	@Path("/{reportId}/inform")
	@Produces(MediaType.TEXT_PLAIN)
	public String informOffended(@PathParam("reportId") int id){
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Report report = db.searchReport(id);
		String text;
		if(report.getType()==ObjectClass.SUBFORUM){
			text="Subforum you reported ( " + ((Subforum)report.getReportObject()).getName() + " ) is not violating forum rules. Thank you for your support.";
		}else if(report.getType()==ObjectClass.TOPIC){
			text="Topic you reported ( " + ((Topic)report.getReportObject()).getName() + " ) is not violating forum rules. Thank you for your support.";
		}else{
			text="Comment you reported on topic " + ((Topic)report.getReportObject()).getName() + " is not violating forum rules. Thank you for your support.";
		}
		Message message = new Message(user.getUsername(),report.getOffended(),text);
		db.searchUser(report.getOffended()).addMessage(message);
		report.setProcessed(true);
		return "Offended user informed!";
	}
}
