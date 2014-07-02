package beans;

import javafx.beans.property.SimpleStringProperty;

public class StatusByMonth {
	private SimpleStringProperty Subject;
	private SimpleStringProperty Total;
	private SimpleStringProperty Destination;
	private SimpleStringProperty Comment;
	
	public StatusByMonth(){
		this.Comment = new SimpleStringProperty();
		this.Destination = new SimpleStringProperty();
		this.Subject = new SimpleStringProperty();
		this.Total = new SimpleStringProperty();
	}


	public String getSubject() {
		return Subject.get();
	}

	public void setSubject(String subject) {
		this.Subject.set(subject);
	}

	public String getTotal() {
		return Total.get();
	}

	public void setTotal(String total) {
		this.Total.set(total);
	}

	public String getDestination() {
		return Destination.get();
	}

	public void setDestination(String destination) {
		this.Destination.set(destination);
	}

	public String getComment() {
		return Comment.get();
	}

	public void setComment(String comment) {
		this.Comment.set(comment);
	}
	

	@Override
	public String toString() {
		return "StatusByMonth [Subject=" + Subject + ", Total=" + Total
				+ ", Destination=" + Destination + ", Comment=" + Comment + "]";
	}
	
	
}
