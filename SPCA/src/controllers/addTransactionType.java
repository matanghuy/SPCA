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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class addTransactionType implements Initializable{
	
	
	@FXML private ChoiceBox<String> category;
	@FXML private TextField operationName;
	DataContext layerFactory;
	
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
		public void search(ActionEvent event){
			try {
				layerFactory.addTransactionType(operationName.getText());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((Node)event.getSource()).getScene().getWindow().hide();
		}

}
