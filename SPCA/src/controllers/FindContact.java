package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import spca.datalayer.DataContext;
import spca.datalayer.SpcaDataLayerFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

public class FindContact implements Initializable{
	
	@FXML ChoiceBox textbox;
	DataContext layerFactory;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			layerFactory = SpcaDataLayerFactory.getDataContext();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initializeSelectOption();
		
		
	}
	
	private void initializeSelectOption(){
		try {
			System.out.println(layerFactory.getContactTypes());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
