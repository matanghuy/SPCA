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
	}
	public String getCategory() {
		return category.get();
	}
	public void setCategory(String category) {
		this.category.set(category);
	}
	
	public String getJanuary() {
		return january.get();
	}
	public void setJanuary(String january) {
		this.january.set(january);
	}
	public String getFebruary() {
		return february.get();
	}
	public void setFebruary(String february) {
		this.february.set(february);
	}
	public String getMarch() {
		return march.get();
	}
	public void setMarch(String march) {
		this.march.set(march);
	}
	public String getApril() {
		return april.get();
	}
	public void setApril(String april) {
		this.april.set(april);
	}
	public String getMay() {
		return may.get();
	}
	public void setMay(String may) {
		this.may.set(may);
	}
	public String getJune() {
		return june.get();
	}
	public void setJune(String june) {
		this.june.set(june);
	}
	public String getJuly() {
		return july.get();
	}
	public void setJuly(String july) {
		this.july.set(july);
	}
	public String getAugust() {
		return august.get();
	}
	public void setAugust(String august) {
		this.august.set(august);
	}
	public String getSeptember() {
		return september.get();
	}
	public void setSeptember(String september) {
		this.september.set(september);
	}
	public String getOctober() {
		return october.get();
	}
	public void setOctober(String october) {
		this.october.set(october);
	}
	public String getNovember() {
		return november.get();
	}
	public void setNovember(String november) {
		this.november.set(november);
	}
	public String getDecember() {
		return december.get();
	}
	public void setDecember(String december) {
		this.december.set(december);
	}
	
}
