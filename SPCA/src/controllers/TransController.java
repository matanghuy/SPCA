package controllers;
import spca.datalayer.DataContext;
import spca.datalayer.SpcaDataLayerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import DBConnection.Db;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class TransController implements Initializable{
	private enum transactionsType {cash,credit,check};
	@FXML private ComboBox<Integer> day;
	@FXML private ComboBox<Integer> month;
	@FXML private ComboBox<Integer> year;
	@FXML private ComboBox<String> expenseType;
	@FXML private Label fullName;
	@FXML private Label fullAddress;
	@FXML private Label phoneNumber;
	@FXML private Label email;
	@FXML private Label lblTotalToPay;
	@FXML private Label lblTotalPaid;
	@FXML private ToggleGroup contactType; 
	@FXML private Button btnLookContact;
	@FXML private Button btnNewContact;
	@FXML private Button btnAddItem;
	@FXML private TextField tfItem;
	@FXML private TextField tfCost;
	@FXML private TextField tfCash;
	@FXML private TextField tfCheck;
	@FXML private TextField tfCredit;
	
	@FXML private RadioButton rbContactTrue;
	@FXML private RadioButton rbContactFalse;
	@FXML private TableView<PurchaseItem> tableView;
	@FXML private TableColumn<PurchaseItem, String> colItemName;
	@FXML private TableColumn<PurchaseItem, Double> colItemCost;
	
	private int contactId = -1;
	DataContext layerFactory;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			layerFactory = SpcaDataLayerFactory.getDataContext();
		/*	for(int i=0;i<layerFactory.getTransactionType().getRows().length;i++){
				System.out.println(layerFactory.getTransactionType().getRows()[i].getString(1));
			}
			System.out.println("this is the data: "+ layerFactory.getTransactionType().getRows()[0].getString(1));
			System.out.println("this is the data: "+ layerFactory.getTransactionType().getRows().length);
			for(int i=0;i<layerFactory.getTransactionType().getColumnNames().length;i++){
				System.out.println(layerFactory.getTransactionType().getColumnNames()[i]);
			}*/
			System.out.println(layerFactory.getTransactionType().getColumnNames().length);
			for(int i=0;i<layerFactory.getTransactionType().getColumnNames().length;i++){
				System.out.println(layerFactory.getTransactionType().getColumnNames()[i]);
			}
		//	System.out.println(layerFactory.getTransactions(null, null, null, null, null, null, new Date(06.10), 20/10/2014));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initDateBoxes();
		fillExpansesTypes();
		initItemsTable();

	}


	private void initDateBoxes() {
		final ObservableList<Integer> days = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30);
		final ObservableList<Integer> months = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12);
		final ObservableList<Integer> years = FXCollections.observableArrayList(2010,2012,2013,2014,2015,2016,2017,2018,2020,2021,2022);
		day.setItems(days);
		month.setItems(months);
		year.setItems(years);
		day.setValue(days.get(0));
		month.setValue(months.get(0));
		year.setValue(years.get(0));
	
	}

	private void initItemsTable() {
		colItemName.setCellValueFactory(new PropertyValueFactory<PurchaseItem, String>("name"));
		colItemCost.setCellValueFactory(new PropertyValueFactory<PurchaseItem, Double>("cost"));		
	}


	private void fillExpansesTypes() {
		//TODO:here we need to get types from database
		ArrayList<String> typeName = new ArrayList<String>();
		try {
			int size = layerFactory.getTransactionType().getRows().length;
			for(int i=0;i<size;i++){
				typeName.add(layerFactory.getTransactionType().getRows()[i].getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObservableList<String> types = FXCollections.observableArrayList(typeName);
		expenseType.setItems(types);
		expenseType.setValue(expenseType.getItems().get(0));
	}
	@FXML
	private void newContact(ActionEvent event){
		System.out.println("add new contact");
		System.out.println(event.getSource());
		  StringBuilder st = new StringBuilder();  
	        st.append(event.getSource());
	        int start = st.indexOf("id")+3;
	        int stop = st.indexOf(",");
	        String name = st.substring(start, stop);
	        switch(name){
	        case "btnLookContact" : findContact();break;
	        case "btnNewContact": addCustomer(); break;
	        case "btnExit" : System.exit(1);
	        default : System.out.println("not found");
	        
	        }
	}
	
	private void findContact(){
		System.out.println("got here");
		openDialog("Find Contact","/FindContact.fxml");
	}
	
	private void addCustomer() {
		openDialog("Add Contact", "/AddContact.fxml");
	}
	private void openDialog(String title, String fxml) {
		try {
			Stage dialog = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
			Parent root = (Parent)loader.load();
		//	DialogController controller = loader.getController();
		//	controller.setStageRef(dialog);
		//	controller.setRestaurant(restaurant);
			Scene scene = new Scene(root,600,400);
		//	scene.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
			dialog.setScene(scene);
			dialog.setTitle(title);
			dialog.initModality(Modality.WINDOW_MODAL);
		//	dialog.initOwner(mainStage);
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void handleAddItem() {
		 try {
			 ObservableList<PurchaseItem> data = tableView.getItems();
			 data.add(new PurchaseItem(tfItem.getText(), Double.parseDouble(tfCost.getText())));
			 tfItem.setText("");
			 tfCost.setText("");
			 updateTotal();
		 } catch(NumberFormatException e) {
			//TODO: pop error message
		 }	
	}
	
	private void updateTotal() {
		double sum = 0;
		ObservableList<PurchaseItem> items = tableView.getItems();
		for(PurchaseItem item : items) {
			sum += item.getCost();
		}
		lblTotalToPay.setText(String.valueOf(sum));
		
		
	}

	@FXML
	public void handleRemoveItem() {
		int index = tableView.getSelectionModel().getSelectedIndex();
		if(index >= 0) {
			tableView.getItems().remove(index);
			updateTotal();
		}
	}
		
	@FXML
	public void handleRadios() {
		RadioButton selected = (RadioButton)contactType.getSelectedToggle();
		if(selected.equals(rbContactFalse)) {
			resetFields();
			setButtonsDisabled(true);			
		} else {
			setButtonsDisabled(false);
		}

	}
	@FXML
	public void paymentChanged() {
		double sum = parseText(tfCash) + parseText(tfCheck) + parseText(tfCredit);
		lblTotalPaid.setText(String.valueOf(sum));
		if (sum > Double.parseDouble(lblTotalToPay.getText())) {
			lblTotalPaid.setTextFill(Paint.valueOf("RED"));
		} else {
			lblTotalPaid.setTextFill(Paint.valueOf("BLACK"));
		}
	}
	@FXML
	public void save() {
		
		if(validate() != null || !(lblTotalPaid.getText().equals(lblTotalToPay.getText()))) {
			//TODO: pop error message
			
		}
		else{
			//TODO: save data to database
		    //layerFactory.addPayment(transactionId, paymentType, amount)
			System.out.println("Inside save payment");
			if(!tfCash.getText().isEmpty() && !tfCash.getText().equals("0")){
		//		layerFactory.addPayment(transactionId, transactionsType.cash.ordinal(), tfCash.getText());
			}
			if(!tfCredit.getText().isEmpty() && !tfCredit.getText().equals("0")){
		//				layerFactory.addPayment(transactionId, transactionsType.credit.ordinal(), tfCredit.getText());
			}
			if(!tfCheck.getText().isEmpty() && !tfCheck.getText().equals("0")){
			//		layerFactory.addPayment(transactionId, transactionsType.credit.ordinal(), tfCheck.getText());
			}
		}
		
//			types[counter] = transactionsType.cash.ordinal();
//			System.out.println(lblTotalPaid.getText());
//			System.out.println(tfCash.getText());
//			System.out.println(tfCheck.getText());
//			System.out.println(tfCredit.getText());
		
	}
	

	@FXML
	public void cancel(ActionEvent event) {
		((Node)(event.getSource())).getScene().getWindow().hide();
	}
	private String validate() {
		if(contactId < 0  && rbContactTrue.isSelected())
			return "בחר איש קשר או סמן ׳ללא איש קשר׳";
		return null;
		
	}


	private double parseText(TextField field) {
		String text = field.getText();
		if(text.isEmpty())
			return 0;
		try {
		  return Double.parseDouble(text);
		} catch(NumberFormatException e) {
			field.setText("");
			return 0;
		}
	}
	
	private void resetFields() {
		fullName.setText("");
		phoneNumber.setText("");
		fullAddress.setText("");
		email.setText("");
	}

	private void setButtonsDisabled(boolean status) {
		btnLookContact.disableProperty().setValue(status);
		btnNewContact.disableProperty().setValue(status);
	}
	
	
	public class PurchaseItem {
		private final SimpleStringProperty name = new SimpleStringProperty("");
		private final SimpleDoubleProperty cost = new SimpleDoubleProperty(0);
		private int id;
		
		
		public PurchaseItem(String name, double cost) {
			this.name.set(name); 
			this.cost.set(cost);
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
		
		public String getName() {
			return name.get();
		}
		public double getCost() {
			return cost.get();
		}
	}

}


