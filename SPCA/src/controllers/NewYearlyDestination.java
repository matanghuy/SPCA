package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import spca.datalayer.DataContext;
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

    private static final Logger logger = Logger.getLogger(NewYearlyDestination.class);
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
		ArrayList<String> type = fillTypesFromDb();
		ObservableList<String> types = FXCollections.observableArrayList(type);
		category.setItems(types);
		category.setValue(category.getItems().get(0));
	}
	private ArrayList<String> fillTypesFromDb(){
		ArrayList<String> typeName = null;
		typeName = new ArrayList<String>();
		try {
			layerFactory = SpcaDataLayerFactory.getDataContext();
			DataRow[] data = layerFactory.getTransactionType().getRows();
			int size = data.length;
			for(int i=0;i<size;i++){
				typeName.add(data[i].getString(1));
			}
		} catch (SQLException | IOException e) {
			logger.error("Error occurred while getting data from database", e);
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
				try {
					java.util.Date javaDate = formatter.parse(startDateString);
					startDate = new Timestamp(javaDate.getTime());
					javaDate = formatter.parse(endDateString);
					endDate = new Timestamp(javaDate.getTime());
					destinationInt = Integer.parseInt(destination.getText());
					categoeyFinalName = getCategoryFinalName();
					writeToDb();
					wrongInput.setText("");
					((Node)event.getSource()).getScene().getWindow().hide();
				} catch (ParseException e) {
					logger.error("Error occurred while parsing dates", e);
				}
			}
		}
	}
	private void writeToDb(){
		try {
			layerFactory.addBalanceTarget(startDate, endDate, destinationInt, categoeyFinalName);
		} catch (SQLException e) {
            logger.error("Error occurred while getting data from database", e);
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
