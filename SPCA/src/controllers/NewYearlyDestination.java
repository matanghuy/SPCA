package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

public class NewYearlyDestination implements Initializable{
	@FXML private ChoiceBox<String> category;
	@FXML private TextField year;
	@FXML private TextField destination;
	@FXML private Label wrongInput;
	DataContext layerFactory;
	private final String nameForamt = "תקציב יעד שנתי ";
	private String categoeyFinalName;
	private final String smonth = "1";
	private final String emonth = "12";
	private final String day = "1";
	private final String datePattern = "dd/MM/yyyy";
	private SimpleDateFormat formatter;
	private int destinationInt;
	Timestamp startDate, endDate;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillExpensesTypes();
		
	}
	private void fillExpensesTypes(){
		ArrayList<String> type = fillTypersFromDb();
		ObservableList<String> types = FXCollections.observableArrayList(type);
		category.setItems(types);
		category.setValue(category.getItems().get(0));
	}
	private ArrayList<String> fillTypersFromDb(){
		ArrayList<String> typeName = null;
		typeName = new ArrayList<String>();
		try {
			layerFactory = SpcaDataLayerFactory.getDataContext();
			DataRow[] data = layerFactory.getTransactionType().getRows();
			int size = data.length;
			for(int i=0;i<size;i++){
				typeName.add(data[i].getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return typeName;
	}
	
	
	@FXML
	public void send(ActionEvent event){
		String startDateString, endDateString;
		
		
		boolean isOk = checkYearValidation(year);
		if(!isOk){
			wrongInput.setText("נתון אינו תקין! הנה הכנס שנית");
		}
		else{
			isOk = false;
			isOk = checkYearValidation(destination);
			if(!isOk){
				wrongInput.setText("נתון אינו תקין! הנה הכנס שנית");
			}
			else
 {
				wrongInput.setText("");
				startDateString = day + "/" + smonth + "/" + year.getText();
				endDateString = day + "/" + emonth + "/" + year.getText();
				formatter = new SimpleDateFormat(datePattern);
				System.out.println(startDateString);
				System.out.println(endDateString);
				try {
					java.util.Date javaDate = formatter.parse(startDateString);
					startDate = new Timestamp(javaDate.getTime());
					javaDate = formatter.parse(endDateString);
					endDate = new Timestamp(javaDate.getTime());
					System.out.println("start time: " + startDate
							+ " end time : " + endDate);
					destinationInt = Integer.parseInt(destination.getText());
					categoeyFinalName = getCategoryFinalName();
				//	System.out.println(categoeyFinalName);
					wirteToDb();
					
					/*DataResult data3 = layerFactory.getBalanceTarget(null, startDate,endDate);
					for(int i=0;i<data3.getRows().length;i++){
						System.out.println(data3.getRows()[i].getObject("Name"));
						if(((String)(data3.getRows()[i].getObject("Name"))).startsWith("תקציב")){
							System.out.println(data3.getRows()[i].getObject("Name"));
						}
						System.out.println(data3.getRows()[i].getObject("Amount"));
					}*/
					
					
					wrongInput.setText("");
					((Node)event.getSource()).getScene().getWindow().hide();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private void wirteToDb(){
		try {
			layerFactory.addBalanceTarget(startDate, endDate, destinationInt, categoeyFinalName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String getCategoryFinalName(){
		return nameForamt + category.getValue() +" "+ year.getText();
	}
	private boolean checkYearValidation(TextField field){
		try{
			Integer yearInt = Integer.parseInt(year.getText());
			if(yearInt  > 1990 && yearInt < 3000)
				return true;
			return false;
		}catch(Exception e){
			return false;
		}
	
	}

}
