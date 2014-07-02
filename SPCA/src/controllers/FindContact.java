package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.tracing.dtrace.ArgsAttributes;

import spca.datalayer.DataContext;
import spca.datalayer.SpcaDataLayerFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class FindContact implements Initializable{
	
	@FXML TextField findName;
	@FXML ChoiceBox findAccording;
	@FXML ChoiceBox changeView;
	DataContext layerFactory;
	private ArrayList<String> type;
	private ArrayList<String> typeGroup;
	private final String nameHebraw = "שם";
	private final String typeHebraw = "קבוצה";
	private final String typeGroupHebraw = "מחלקה";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("intialize");
	
		intializeDataFromDb();
		initializeSelectOption();
		
		
		
		
	
	}
	private void intializeDataFromDb(){
		type = new ArrayList<String>();
		typeGroup = new ArrayList<String>();
		try {
			layerFactory = SpcaDataLayerFactory.getDataContext();
		
		int size = layerFactory.getContactTypes().getColumnNames().length;
		int size2 = layerFactory.getContactTypes().getRows().length;
		int size3 = layerFactory.getContactTypeGroups().getColumnNames().length;
		int size4 = layerFactory.getContactTypeGroups().getRows().length;
		
		for(int i=0;i<size2;i++){
				type.add((String)layerFactory.getContactTypes().getRows()[i].getObject("Name"));
		}
		
		for(int i=0;i<size4;i++){
				typeGroup.add((String)layerFactory.getContactTypeGroups().getRows()[i].getObject("Name"));
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void initializeSelectOption(){
		ArrayList<String> array = new ArrayList<String>();
		array.add(typeGroupHebraw);
		array.add(typeHebraw);
		array.add(nameHebraw);
		ObservableList<String> categoryToSearch = FXCollections.observableArrayList(array);
		findAccording.setItems(categoryToSearch);
		findAccording.setValue(findAccording.getItems().get(0));
		findAccording.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				String newName = (String)(findAccording.getItems().get(((int)arg2)) );
				System.out.println(newName);
				
				  checkView(newName);
			}
		    });
		checkView(findAccording.getValue().toString());
		
		
		
		try {
			System.out.println(layerFactory.getContactTypes());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void checkView(String newName){
		System.out.println("inside it");
		if(newName.equals(nameHebraw)){
			System.out.println("inside name");
			findName.setVisible(true);
			changeView.setVisible(false);
		}
		else if(newName.equals(typeHebraw)){
			findName.setVisible(false);
			changeView.setVisible(true);
			changeCheckBoxView(type);
			
		}
		else{
			findName.setVisible(false);
			changeView.setVisible(true);
			changeCheckBoxView(typeGroup);
		}
	}
	@FXML
	public void search(){
		
	}
	
	private void changeCheckBoxView(ArrayList<String> types){
		ObservableList<String> categoryToSearch = FXCollections.observableArrayList(types);
		changeView.setItems(categoryToSearch);
		changeView.setValue(changeView.getItems().get(0));
	}

}
