package beans;

import javafx.beans.property.SimpleStringProperty;

public class StatusByYear {
	private SimpleStringProperty category;
	private SimpleStringProperty january;
	private SimpleStringProperty february;
	private SimpleStringProperty march;
	private SimpleStringProperty april;
	private SimpleStringProperty may;
	private SimpleStringProperty june;
	private SimpleStringProperty july;
	private SimpleStringProperty august;
	private SimpleStringProperty september;
	private SimpleStringProperty october;
	private SimpleStringProperty november;
	private SimpleStringProperty december;
	private SimpleStringProperty counter;
	private SimpleStringProperty destination;
	
	public StatusByYear(){
		this.category = new SimpleStringProperty();
		this.january = new SimpleStringProperty();
		this.february = new SimpleStringProperty();
		this.march = new SimpleStringProperty();
		this.april = new SimpleStringProperty();
		this.may = new SimpleStringProperty();
		this.june = new SimpleStringProperty();
		this.july = new SimpleStringProperty();
		this.august = new SimpleStringProperty();
		this.september = new SimpleStringProperty();
		this.october = new SimpleStringProperty();
		this.november = new SimpleStringProperty();
		this.december = new SimpleStringProperty();
		counter = new SimpleStringProperty();
		destination = new SimpleStringProperty();
	}
	public String getCounter() {
		return counter.get() ;
	}
	public void setCounter(String counter) {
		this.counter.set(counter);
	}
	public String getDestination() {
		return destination.get() .equals("null") ? "0" : destination.get();
	}
	public void setDestination(String destination) {
		this.destination.set(destination);
	}
	
	
	public String getCategory() {
		return category.get() ;
	}
	public void setCategory(String category) {
		this.category.set(category);
	}
	
	public String getJanuary() {
		return january.get().equals("null") ? "0" : january.get();
	}
	public void setJanuary(String january) {
		this.january.set(january);
	}
	public String getFebruary() {
		return february.get().equals("null") ? "0" : february.get();
	}
	public void setFebruary(String february) {
		this.february.set(february);
	}
	public String getMarch() {
		return march.get().equals("null") ? "0" :march.get();
	}
	public void setMarch(String march) {
		this.march.set(march);
	}
	public String getApril() {
		return april.get().equals("null") ? "0" : april.get();
	}
	public void setApril(String april) {
		this.april.set(april);
	}
	public String getMay() {
		return may.get().equals("null") ? "0" : may.get();
	}
	public void setMay(String may) {
		this.may.set(may);
	}
	public String getJune() {
		return june.get().equals("null") ? "0" : june.get();
	}
	public void setJune(String june) {
		this.june.set(june);
	}
	public String getJuly() {
		return july.get().equals("null") ? "0" : july.get();
	}
	public void setJuly(String july) {
		this.july.set(july);
	}
	public String getAugust() {
		return august.get().equals("null") ? "0" : august.get();
	}
	public void setAugust(String august) {
		this.august.set(august);
	}
	public String getSeptember() {
		return september.get().equals("null") ? "0" : september.get();
	}
	public void setSeptember(String september) {
		this.september.set(september);
	}
	public String getOctober() {
		return october.get().equals("null") ? "0" : october.get();
	}
	public void setOctober(String october) {
		this.october.set(october);
	}
	public String getNovember() {
		return november.get().equals("null") ? "0" : november.get();
	}
	public void setNovember(String november) {
		this.november.set(november);
	}
	public String getDecember() {
		return december.get().equals("null") ? "0" : december.get();
	}
	public void setDecember(String december) {
		this.december.set(december);
	}
	
}
