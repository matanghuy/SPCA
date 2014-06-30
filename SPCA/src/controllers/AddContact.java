package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;

public class AddContact  implements Initializable{

	
	@FXML TextField fullName;
	@FXML TextField phoneNumber;
	@FXML TextField email;
	@FXML TextField fullAddress;
	@FXML TextField country;
	@FXML Label validInput;
	@FXML Label lfullName;
	@FXML Label lfullAddress;
	@FXML Label lemail;
	@FXML Label lcountry;
	@FXML Label lphoneNumber;
	
	
	
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
		case "lfullName": 
		case "lcountry": checkString(name); break;
		case "lfullAddress" :checkAddress();break;
		case "lemail" :   checkMail(); break;
		case "lphoneNumber" : checkPhoneNumber(); break;
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
	private void checkPhoneNumber(){
		if(phoneNumber.getText().isEmpty()){
			lphoneNumber.setTextFill(Paint.valueOf("BLACK"));
			return;
		}
		else if(!( phoneNumber.getText().length()>2)){
			lphoneNumber.setTextFill(Paint.valueOf("RED"));
			return;
		}
		lphoneNumber.setTextFill(Paint.valueOf("GREEN"));
	}
	private void checkString(String event){
		TextField text = null;
		Label label = null;
		switch(event){
		case "lfullName": text = fullName; label = lfullName; break;
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
			//add contact to db
			System.out.println("add to db");
			validInput.setText("");
		}
		else{
			validInput.setText("נתונים לא נכונים!\nבבקשה לתקן");
		}
	}
	
	private boolean isValid(){
		Label[] labels = {lfullName,lfullAddress,lemail,lcountry,lphoneNumber};
		int size = labels.length;
		for(int i=0;i<size;i++){
			if(labels[i].getText().isEmpty() || !(labels[i].getTextFill().equals(Paint.valueOf("GREEN"))))
				return false;
		}
		return true;
	}
	

}
