package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import spca.datalayer.DataContext;
import spca.datalayer.SpcaDataLayerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;

public class AddContact  implements Initializable{

	
	@FXML TextField firstName;
	@FXML TextField lastName;
	@FXML TextField phoneNumber;
	@FXML TextField bDate;
	@FXML TextField email;
	@FXML TextField lbDate;
	@FXML TextField fullAddress;
	@FXML TextField country;
	@FXML TextField identityNumber;
	@FXML Label validInput;
	@FXML Label lfirstName;
	@FXML Label llastName;
	@FXML Label lfullAddress;
	@FXML Label lemail;
	@FXML Label lcountry;
	@FXML Label lphoneNumber;
	@FXML Label lidentityNumber;
	DataContext layerFactory;
	private final String datePattern = "dd/MM/yyyy";
	private SimpleDateFormat formatter;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
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
		case "lcountry": checkString(name); break;
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
		case "lcountry": text = country; label = lcountry; break;
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
	public void save() {
		if(isValid()){
			writeUserToDb();
			System.out.println("add to db");
			validInput.setText("");
		}
		else{
			validInput.setText("נתונים לא נכונים!\nבבקשה לתקן");
		}
	}
	
	private boolean isValid(){
		Label[] labels = {lfirstName,llastName,lfullAddress,lemail,lcountry,lphoneNumber};
		int size = labels.length;
		for(int i=0;i<size;i++){
			if(labels[i].getText().isEmpty() || !(labels[i].getTextFill().equals(Paint.valueOf("GREEN"))))
				return false;
		}
		return true;
	}
	
	private void writeUserToDb(){
		try {
			layerFactory = SpcaDataLayerFactory.getDataContext();
			int size = layerFactory.getContactTypes().getColumnNames().length;
			int size2 = layerFactory.getContactTypes().getRows().length;
			int size3 = layerFactory.getContactTypeGroups().getColumnNames().length;
			int size4 = layerFactory.getContactTypeGroups().getRows().length;
			Integer[] aray = {1,2,3}; 
			int size5 = layerFactory.getContacts(null,null, null, null, null).getColumnNames().length;
			int size6 = layerFactory.getContacts(null,null, null, null, null).getRows().length;
			
			formatter = new SimpleDateFormat(datePattern);
			java.util.Date javaDate = formatter.parse(bDate.getText());
			Timestamp startDate = new Timestamp(javaDate.getTime());
			
			/*layerFactory.addOrUpdateContact(firstName.getText(), lastName.getText(), phoneNumber.getText()
					, null, email.getText(), null , fullAddress.getText(),
					1023, null, identityNumber.getText(), startDate);
		*/
			
	/*		for(int i=0;i<layerFactory.getCities().getRows().length;i++){
				for(int j=0;j<layerFactory.getCities().getColumnNames().length;j++)
					System.out.println(layerFactory.getCities().getRows()[i].getString(j));
			}*/
				//System.out.println(layerFactory.getCities().getColumnNames().);
			
			for(int i=0;i<size2;i++){
					System.out.println(layerFactory.getContactTypes().getRows()[i].getObject("Name"));
					
			}
		/*	
			for(int i=0;i<size4;i++){
				for(int j=0;j<size3;j++)
					System.out.println(layerFactory.getContactTypeGroups().getRows()[i].getString(j));
			}
			*/
			for(int i=0;i<size6;i++){
				for(int j=0;j<size5;j++)
					System.out.println(layerFactory.getContacts(null,null, null, "בלסיאנו", null).getRows()[i].getString(j));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
