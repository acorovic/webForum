package beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class Report implements Serializable{

	public static enum ObjectClass {SUBFORUM,TOPIC,COMMENT};
	
	private int reportId;
	private String offended;
	private String reportText;
	private String reportDate;
	@JsonIgnore
	private Object reportObject;
	private boolean processed;
	private ObjectClass type;
	
	public Report() {
		super();
	}
	
	public Report(String offended, String reportText, Subforum reportObject,int id) {
		super();
		this.offended = offended;
		this.reportText = reportText;
		this.reportObject = reportObject;
		this.reportDate = (new Date()).toString();
		this.processed = false;
		this.reportId = id;
		this.type = ObjectClass.SUBFORUM;
	}
	
	public Report(String offended, String reportText, Topic reportObject,int id) {
		super();
		this.offended = offended;
		this.reportText = reportText;
		this.reportObject = reportObject;
		this.reportDate = (new Date()).toString();
		this.processed = false;
		this.reportId = id;
		this.type = ObjectClass.TOPIC;
	}
	
	public Report(String offended, String reportText, Comment reportObject,int id) {
		super();
		this.offended = offended;
		this.reportText = reportText;
		this.reportObject = reportObject;
		this.reportDate = (new Date()).toString();
		this.processed = false;
		this.reportId = id;
		this.type = ObjectClass.COMMENT;
	}

	public String getOffended() {
		return offended;
	}

	public void setOffended(String offended) {
		this.offended = offended;
	}

	public String getReportText() {
		return reportText;
	}

	public void setReportText(String reportText) {
		this.reportText = reportText;
	}

	public Object getReportObject() {
		return reportObject;
	}

	public void setReportObject(Object reportObject) {
		this.reportObject = reportObject;
	}

	@JsonIgnore
	public String getReportDate() {
		return reportDate;
	}

	@JsonProperty
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public int getId() {
		return this.reportId;
	}

	public void setId(int id) {
		this.reportId = id;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public ObjectClass getType() {
		return type;
	}

	public void setType(ObjectClass type) {
		this.type = type;
	}
}
