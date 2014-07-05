package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

import beans.Transaction;
import org.apache.log4j.Logger;
import spca.datalayer.DataContext;
import spca.datalayer.DataResult;
import spca.datalayer.DataRow;
import spca.datalayer.SpcaDataLayerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class IncomingReport implements Initializable{
	
	private static final Logger logger = Logger.getLogger(IncomingReport.class);
	@FXML ComboBox category;
	@FXML Button submit;
	@FXML ComboBox yearStart;
	@FXML ComboBox monthStart;
	@FXML ComboBox dayStart;
	@FXML ComboBox yearEnd;
	@FXML ComboBox monthEnd;
	@FXML ComboBox dayEnd;
	@FXML private TableView<Transaction> table;
	@FXML TableColumn name;
	@FXML TableColumn date;
	@FXML TableColumn totalToPay;
	@FXML TableColumn totalPaid;
	@FXML TableColumn transactionType;
	@FXML TableColumn comments;
	private ObservableList<Transaction> transaction;
	private HashMap<String,Integer> transactionTypeMap = new HashMap<String,Integer>(); 
	
	private final String datePattern = "dd/MM/yyyy";
	private SimpleDateFormat formatter;
	Timestamp startTime;
	Timestamp endTime;
	
	DataContext layerFactory;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		transaction = FXCollections.observableArrayList();
		table.setItems(transaction);
		initTable();
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
		
		String nextMonth,cuurentMonth,currentYear,nextYear;
		int currentYearint = Calendar.getInstance().get(Calendar.YEAR);
		int currentMonthint = Calendar.getInstance().get(Calendar.MONTH) + 1;
		currentYear = currentYearint + "";
		cuurentMonth = currentMonthint + "";
		if(currentMonthint == 12){
			nextMonth = 1+"";
			nextYear = currentYearint + 1 + "";
		}
		else{
			nextMonth = currentMonthint + 1 + "";
			nextYear = currentYear;
		}
		
		yearStart.setValue(currentYear);
		yearEnd.setValue(nextYear);
		monthStart.setValue(cuurentMonth);
		monthEnd.setValue(nextMonth);
		dayStart.setValue(dayStart.getItems().get(0));
		dayEnd.setValue(dayEnd.getItems().get(0));
	
		
	}
	private void fillExpansesTypes() {
		ArrayList<String> type = fillTypesFromDb();
		ObservableList<String> types = FXCollections.observableArrayList(type);
		category.setItems(types);
		category.setValue(category.getItems().get(0));
	}
	private ArrayList<String> fillTypesFromDb(){
		ArrayList<String> typeName = null;
		typeName = new ArrayList<String>();
		typeName.add("הכול");
		try {
			layerFactory = SpcaDataLayerFactory.getDataContext();
			DataRow[] data = layerFactory.getTransactionType().getRows();
			int size = data.length;
			for(int i=0;i<size;i++){
				typeName.add(data[i].getString(1));
				transactionTypeMap.put(data[i].getString(1), i+1);
			}
		} catch (SQLException e) {
			logger.error("Error occurred while getting data from database", e);
		} catch (IOException e) {
            logger.error("Error occurred while parsing dates", e);
		}
		return typeName;
	}
	@FXML
	public void handleSearch(ActionEvent event){
		transaction.clear();
		formatter = new SimpleDateFormat(datePattern);
		String sYear = yearStart.getValue().toString();
		String sMonth = monthStart.getValue().toString();
		String sDay = dayStart.getValue().toString();
		String startDateString = sDay + "/" + sMonth + "/" + sYear;
		
		String eYear = yearEnd.getValue().toString();
		String eMonth = monthEnd.getValue().toString();
		String eDay = dayEnd.getValue().toString();
		String endDateString =  eDay + "/" + eMonth+ "/" + eYear ;
		
		String show = category.getValue().toString();

		try {
			
			java.util.Date javaDate = formatter.parse(startDateString);
			startTime = new Timestamp(javaDate.getTime());
			javaDate = formatter.parse(endDateString);
			endTime = new Timestamp(javaDate.getTime());
			Integer typeSelect[] = new Integer[1];
			if(show.equals("הכול"))
				typeSelect = null;
			else
				typeSelect[0] = transactionTypeMap.get(show);
			DataResult result = layerFactory.getTransactions(null, typeSelect, null, null,null, null, null, startTime,endTime,null);

			for(int i=0;i<result.getRows().length;i++){
			this.transaction.add(creatTransaction(result.getRows()[i]));
				
			}
				
		}catch(Exception e){
			logger.error("General error", e);
		}
		
		
		
	}
	private Transaction creatTransaction(DataRow row){
		Transaction transaction = new Transaction();
		transaction.setContactName((String)row.getObject("ContactName"));
		transaction.setTransactionDate((String)(row.getObject("TransactionDate")+""));
		transaction.setTotalAmountPayed((String)(row.getObject("TotalAmountPayed")+""));
		transaction.setTotalAmountToPay((String)(row.getObject("TotalAmountToPay")+""));
		transaction.setTransactionTypeName((String)row.getObject("TransactionTypeName"));
		transaction.setComments((String)row.getObject("Comments"));
		return transaction;
	}
	
	public void initTable() {
		name.setCellValueFactory(new PropertyValueFactory<Transaction, String>("ContactName"));
		date.setCellValueFactory(new PropertyValueFactory<Transaction, String>("TransactionDate"));
		totalToPay.setCellValueFactory(new PropertyValueFactory<Transaction, String>("TotalAmountToPay"));
		totalPaid.setCellValueFactory(new PropertyValueFactory<Transaction, String>("TotalAmountPayed"));
		transactionType.setCellValueFactory(new PropertyValueFactory<Transaction, String>("TransactionTypeName"));
		comments.setCellValueFactory(new PropertyValueFactory<Transaction, String>("Comments"));		
	}

}
