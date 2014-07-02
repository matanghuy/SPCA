package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import spca.datalayer.DataContext;
import spca.datalayer.DataRow;
import spca.datalayer.SpcaDataLayerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.util.Callback;

public class expensesReport  implements Initializable {
	

	@FXML ComboBox category;
	@FXML Button submit;
	@FXML ComboBox yearStart;
	@FXML ComboBox monthStart;
	@FXML ComboBox dayStart;
	@FXML ComboBox yearEnd;
	@FXML ComboBox monthEnd;
	@FXML ComboBox dayEnd;
	final String name = "Name";
	
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
		fillCategorys();
		
	}

	private void fillCategorys(){
		ArrayList<String> categories = new ArrayList<String>();
		try {
			
		DataRow[] data= layerFactory.getTransactionType().getRows();
		int size = data.length;
		
		for(int i=0;i<size;i++){
			categories.add((String)data[i].getObject(name));
		}
		}catch(Exception e){
			e.printStackTrace();
		}

		ObservableList<String> categoryObserver = FXCollections.observableArrayList(categories);
		category.setItems(categoryObserver);
		category.setValue(category.getItems().get(0));
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
	
	public void fillExpansesTypes(){
		
	}
	
	@FXML 
	public void handleSearch(){
		
	}

}
