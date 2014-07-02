package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import sun.util.resources.CalendarData;
import controllers.TransController.PurchaseItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StatusController implements Initializable{
	
	@FXML private Button start;
	@FXML private ComboBox<String> date;
	@FXML private ComboBox<String> year;
	@FXML private TableView<PurchaseItem> tableView;
	@FXML private TableColumn<PurchaseItem, String> colStatus;
	@FXML private TableColumn<PurchaseItem, String> colDestination;
	@FXML private TableColumn<PurchaseItem, String> colSubject;
	@FXML private TableColumn<PurchaseItem, String> colSum;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initComboBox();
		
		
	}
	private void initComboBox(){
		ArrayList<String> monthArray = new ArrayList<String>();
		ArrayList<String> yearArray = new ArrayList<String>();
		for(int i=1;i<13;i++){
			monthArray.add(i+"");
		}
		for(int i=10;i<31;i++){
			yearArray.add("20"+i);
		}
		final ObservableList<String> dates = FXCollections.observableArrayList(monthArray);
		final ObservableList<String> years = FXCollections.observableArrayList(yearArray);

		date.setItems(dates);
		year.setItems(years);
		String cuurentDate = (Calendar.getInstance().get(Calendar.MONTH))+1+""; 
		date.setValue(cuurentDate);
		String currentYear = Calendar.getInstance().get(Calendar.YEAR) + "";
		year.setValue(currentYear);
		
	}

}
