package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import beans.Contact;

import spca.datalayer.DataContext;
import spca.datalayer.DataResult;
import spca.datalayer.DataRow;
import spca.datalayer.SpcaDataLayerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;

public class AddContact  implements Initializable{


	@FXML TextField firstName;
	@FXML TextField lastName;
	@FXML TextField phoneNumber;

	@FXML TextField email;
	@FXML TextField lbDate;
	@FXML TextField fullAddress;
	@FXML TextField city;
	@FXML TextField identityNumber;
	@FXML ChoiceBox bYear;
	@FXML ChoiceBox bMonth;
	@FXML ChoiceBox bDay;
	@FXML ChoiceBox category;
	@FXML Label validInput;
	@FXML Label lfirstName;
	@FXML Label llastName;
	@FXML Label lfullAddress;
	@FXML Label lemail;
	@FXML Label lcity;
	@FXML Label lphoneNumber;
	@FXML Label lidentityNumber;
	DataContext database;
	private final String datePattern = "dd/MM/yyyy";
	private SimpleDateFormat formatter;
	HashMap<String,Integer> categoryMap = new HashMap<String, Integer>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		fillExpansesTypes();
		fillTimeBoxes();
	}

	private void fillTimeBoxes(){
		ArrayList<String> year = new ArrayList<String>();
		ArrayList<String> month = new ArrayList<String>();
		ArrayList<String> day = new ArrayList<String>();

		for(int i=1;i<31;i++){
			day.add(i+"");
		}
		for(int i=1;i<13;i++){
			month.add(i+"");
		}
		for(int i=50;i<100;i++){
			year.add("19"+i);
		}
		for(int i=0;i<10;i++){
			year.add("20"+i+"0");
		}
		for(int i=10;i<50;i++){
			year.add("20"+i);
		}
		ObservableList<String> yearObserver = FXCollections.observableArrayList(year);
		ObservableList<String> monthObserver = FXCollections.observableArrayList(month);
		ObservableList<String> dayObserver = FXCollections.observableArrayList(day);
		bYear.setItems(yearObserver);
		bMonth.setItems(monthObserver);
		bDay.setItems(dayObserver);
		bYear.setValue(bYear.getItems().get(0));
		bMonth.setValue(bMonth.getItems().get(0));
		bDay.setValue(bDay.getItems().get(0));


	}
	private void fillExpansesTypes() {
		ArrayList<String> type = fillTypesFromDb();
		ObservableList<String> types = FXCollections.observableArrayList(type);
		category.setItems(types);
		category.setValue(category.getItems().get(0));
	}

private ArrayList<String> fillTypesFromDb(){
	ArrayList<String> typeName = null;
	typeName = new ArrayList<String>();
	try {
		database = SpcaDataLayerFactory.getDataContext();
		DataRow[] data = database.getContactTypes().getRows();
		int size = data.length;
		for(int i=0;i<size;i++){
			typeName.add(data[i].getString(1));
			categoryMap.put(data[i].getString(1), i+1);
		}
	} catch (SQLException | IOException e) {
		e.printStackTrace();
	}
	return typeName;
}


	@FXML
	private void statusChange(KeyEvent event){

		System.out.print(event.getSource());
		StringBuilder st = new StringBuilder();
		st.append(event.getSource());
		int start = st.indexOf("id")+3;
		int stop = st.lastIndexOf(",");
		String name ="l" + st.substring(start,stop);
		System.out.print(name);
		switch(name){
		case "llastName":
		case "lfirstName":
		case "lcity": checkString(name); break;
		case "lfullAddress" :checkAddress();break;
		case "lemail" :   checkMail(); break;
		case "lidentityNumber":
		case "lphoneNumber" : checkPhoneNumber(name); break;
		}
	}
	private void checkAddress(){

		if(fullAddress.getText().isEmpty()) {
			lfullAddress.setTextFill(Paint.valueOf("BLACK"));
			return;
		}
		else if(fullAddress.getText().length() > 2){
			lfullAddress.setTextFill(Paint.valueOf("GREEN"));
			return;
		}
		lfullAddress.setTextFill(Paint.valueOf("RED"));
	}
	private void checkPhoneNumber(String event){
		TextField text = null;
		Label label = null;

		switch(event){
		case "lidentityNumber": text = identityNumber; label = lidentityNumber; break;
		case "lphoneNumber": text = phoneNumber; label = lphoneNumber; break;
		}

		if(text.getText().isEmpty()){
			label.setTextFill(Paint.valueOf("BLACK"));
			return;
		}
		else if(!( text.getText().length()>2)){
			label.setTextFill(Paint.valueOf("RED"));
			return;
		}
		label.setTextFill(Paint.valueOf("GREEN"));
	}


	private void checkString(String event){
		TextField text = null;
		Label label = null;
		switch(event){
		case "lfirstName": text = firstName; label = lfirstName; break;
		case "llastName": text = lastName; label = llastName; break;
		case "lcity": text = city; label = lcity; break;
		}


		if(text.getText().isEmpty()) {
			label.setTextFill(Paint.valueOf("BLACK"));
			return;
		}
		else if(text.getText().matches("^([^0-9]*)$")){
			label.setTextFill(Paint.valueOf("GREEN"));
			return;
		}
		label.setTextFill(Paint.valueOf("RED"));
	}
	private void checkMail(){
		if(email.getText().isEmpty()){
			lemail.setTextFill(Paint.valueOf("BLACK"));
			return;
		}
		else if(!(email.getText().contains("@") && email.getText().contains("."))){
			lemail.setTextFill(Paint.valueOf("RED"));
			return;
		}
		lemail.setTextFill(Paint.valueOf("GREEN"));
	}
	@FXML
	public void cancel(ActionEvent event) {
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	public void save(ActionEvent event) {
		if(isValid()){
            Contact contact = createContact();
			writeUserToDb(contact);
            CommonUtils.contact = contact;
			System.out.println("add to db");
			validInput.setText("");
            ((Node)event.getSource()).getScene().getWindow().hide();
		}
		else{
			validInput.setText("נתונים לא נכונים!\nבבקשה לתקן");
		}
	}

    private Contact createContact() {
        Contact contact = new Contact();
        contact.setFirstName(firstName.getText());
        contact.setLastName(lastName.getText());
        contact.setPhone1(phoneNumber.getText());
        contact.setEmail1(email.getText());
        contact.setCity(city.getText());
        System.out.println(bDay.getValue() + "/" + bMonth.getValue() +"/" +bYear.getValue());
        contact.setBirthDay(bDay.getValue() + "/" + bMonth.getValue() +"/" +bYear.getValue());
        return contact;
    }

    private boolean isValid(){
		Label[] labels = {lfirstName,llastName,lfullAddress,lemail, lcity,lphoneNumber};
		int size = labels.length;
		for(int i=0;i<size;i++){
			if(labels[i].getText().isEmpty() || !(labels[i].getTextFill().equals(Paint.valueOf("GREEN"))))
				return false;
		}
		return true;
	}

	private void writeUserToDb(Contact contact){
		try {
			database = SpcaDataLayerFactory.getDataContext();
			formatter = new SimpleDateFormat(datePattern);
			java.util.Date javaDate = formatter.parse(contact.getBirthDay());

            Timestamp startDate = new Timestamp(javaDate.getTime());
			Integer cityNumber = CommonUtils.citiesMap.get(contact.getCity());



			DataResult data = database.addOrUpdateContact(contact.getFirstName(), contact.getLastName(), contact.getPhone1()
					, null, contact.getEmail1(), null , contact.getAddress(), cityNumber, null, identityNumber.getText(), startDate);
			Integer returnValue = (Integer)data.getReturnValue();

            database.addTypeForContact(returnValue, categoryMap.get(category.getValue()));

		} catch (IOException | ParseException | SQLException e) {
			e.printStackTrace();
		}
	}


}
