package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import spca.datalayer.DataContext;
import spca.datalayer.SpcaDataLayerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NewDestination implements Initializable{
	
	private final String day = "1";
	private final String datePattern = "dd/MM/yyyy";
	private SimpleDateFormat formatter;
	private String startDateString,endDateString;
	private Timestamp startDate,endDate;
	private int destinationInt;
	@FXML Button send;
	@FXML ComboBox<String> month;
	@FXML TextField year;
	@FXML TextField destination;
	@FXML TextField category;
	@FXML Label wrongInput;
	DataContext layerFactory;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		intializeMonth();
		
	}
	private void intializeMonth(){
		ArrayList<String> monthArray = new ArrayList<String>();

		for (int i = 1; i < 13; i++) {
			monthArray.add(i + "");
		}
		ObservableList<String> monthObserver = FXCollections.observableArrayList(monthArray);
		month.setItems(monthObserver);
		month.setValue(month.getItems().get(0));
	}
	
	@FXML
	public void send(ActionEvent event){
		boolean isOk = false;
		isOk = checkValidtation();
		if(isOk){
			int endMonth = Integer.parseInt(month.getValue());
			int endYear = Integer.parseInt(year.getText()); 
			if(endMonth != 12){
				endMonth++;
			}
			else{
				endMonth = 1;
				endYear ++;
			}
			String endMonthString = endMonth + ""; 
			String endYearString = endYear + "";
			formatter = new SimpleDateFormat(datePattern);
			startDateString = day + "/" + month.getValue() + "/" + year.getText();
			endDateString =  day + "/" + endMonthString+ "/" + endYearString ;
			try {
				java.util.Date javaDate = formatter.parse(startDateString);
				startDate = new Timestamp(javaDate.getTime());
				javaDate = formatter.parse(endDateString);
				endDate = new Timestamp(javaDate.getTime());
				System.out.println("start time: "+ startDate + " end time : "+ endDate);
				destinationInt = Integer.parseInt(destination.getText());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			wirteToDb();
			System.out.println("send the data to the db year: "+year.getText()
					+" month "+month.getValue()+" destination "+destination.getText());
			wrongInput.setText("");
		}
		else{
			wrongInput.setText("הזנת נתונים לא חוקית!\n הנה נסה שנית");
		}
		
	}
	
	private void wirteToDb() {
			try {
		layerFactory = SpcaDataLayerFactory.getDataContext();
		layerFactory.addBalanceTarget(startDate, endDate, destinationInt, category.getText());
		int size = layerFactory.getBalanceTarget(null, null, null).getColumnNames().length;
		int size2 = layerFactory.getBalanceTarget(null, null, null).getRows().length;
		
		for(int i=0;i<size2;i++){
			for(int j=0;j<size;j++)
				System.out.println(layerFactory.getBalanceTarget(null, null, null).getRows()[i].getString(j));
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}
	private boolean checkValidtation(){
		String yearText = year.getText();
		String destinationText = destination.getText();
		if (!(Pattern.matches("[a-zA-Z]+", yearText)) && yearText.length()== 4){
			   if(!(Pattern.matches("[a-zA-Z]+", destinationText)) && destinationText.length() > 1)
				   return true;
		}
		return false;
		
	}

}
