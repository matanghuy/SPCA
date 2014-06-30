package controllers;

import java.net.URL;
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
	@FXML private ComboBox<Integer> date;
	@FXML private ComboBox<Integer> year;
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
		final ObservableList<Integer> dates = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12);
		final ObservableList<Integer> years = FXCollections.observableArrayList(2011,2012,2013,2014,2015);

		date.setItems(dates);
		year.setItems(years);
		int cuurentDate = (Calendar.getInstance().get(Calendar.MONTH))+1; 
		date.setValue(cuurentDate);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		year.setValue(currentYear);
		
	}

}
