package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import spca.datalayer.DataContext;
import spca.datalayer.SpcaDataLayerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class incomingReport  implements Initializable{
	
	
	@FXML ComboBox category;
	@FXML Button submit;
	@FXML ComboBox yearStart;
	@FXML ComboBox monthStart;
	@FXML ComboBox dayStart;
	@FXML ComboBox yearEnd;
	@FXML ComboBox monthEnd;
	@FXML ComboBox dayEnd;
	
	DataContext layerFactory;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			layerFactory = SpcaDataLayerFactory.getDataContext();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		for(int i=10;i<24;i++){
			year.add("20"+i);
		}
		ObservableList<String> yearObserver = FXCollections.observableArrayList(year);
		ObservableList<String> monthObserver = FXCollections.observableArrayList(month);
		ObservableList<String> dayObserver = FXCollections.observableArrayList(day);
		yearStart.setItems(yearObserver);
		yearEnd.setItems(yearObserver);
		monthStart.setItems(monthObserver);
		monthEnd.setItems(monthObserver);
		dayStart.setItems(dayObserver);
		dayEnd.setItems(dayObserver);
		yearStart.setValue(yearStart.getItems().get(0));
		yearEnd.setValue(yearEnd.getItems().get(0));
		monthStart.setValue(monthStart.getItems().get(0));
		monthEnd.setValue(monthEnd.getItems().get(0));
		dayStart.setValue(dayStart.getItems().get(0));
		dayEnd.setValue(dayEnd.getItems().get(0));
	
		
	}
	private void fillExpansesTypes() {
		//TODO:here we need to get types from database
		ArrayList<String> type = fillTypersFromDb();
		ObservableList<String> types = FXCollections.observableArrayList(type);
		category.setItems(types);
		category.setValue(category.getItems().get(0));
	}
	private ArrayList<String> fillTypersFromDb(){
		ArrayList<String> typeName = null;
		typeName = new ArrayList<String>();
		typeName.add("הכול");
		try {
			int size = layerFactory.getTransactionType().getRows().length;
			for(int i=0;i<size;i++){
				typeName.add(layerFactory.getTransactionType().getRows()[i].getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return typeName;
	}
	@FXML
	public void handleSearch(ActionEvent event){
		String syear = yearStart.getValue().toString();
		String smonth = monthStart.getValue().toString();
		String sday = dayStart.getValue().toString();
		System.out.println("year: "+syear+" month: "+smonth+" day: "+sday);
		
		String eyear = yearEnd.getValue().toString();
		String emonth = monthEnd.getValue().toString();
		String eday = dayEnd.getValue().toString();
		System.out.println("year: "+eyear+" month: "+emonth+" day: "+eday);
		
		String show = category.getValue().toString();
		System.out.println("category: "+show);
		
	/*	try {
			layerFactory.addTransaction(48, 22, 1, null, null, null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
