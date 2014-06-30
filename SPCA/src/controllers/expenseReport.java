package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class expenseReport  implements Initializable{
	
	
	@FXML ComboBox category;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		fillExpansesTypes();
	}
	
	
	private void fillExpansesTypes() {
		//TODO:here we need to get types from database
		ObservableList<String> types = FXCollections.observableArrayList("תשלום דמי חבר ","רכישת ציוד ","רכישת חיה ","תשלום לרופא","תשלום לספק");
		category.setItems(types);
		category.setValue(category.getItems().get(0));
	}

}
