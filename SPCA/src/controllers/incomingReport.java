package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

import beans.Contact;
import beans.Transaction;
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

public class incomingReport  implements Initializable{
	
	
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
			layerFactory = SpcaDataLayerFactory.getDataContext();
			DataRow[] data = layerFactory.getTransactionType().getRows();
			int size = data.length;
			for(int i=0;i<size;i++){
				typeName.add(data[i].getString(1));
				transactionTypeMap.put(data[i].getString(1), i+1);
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
	public void handleSearch(ActionEvent event){
		transaction.clear();
		formatter = new SimpleDateFormat(datePattern);
		String syear = yearStart.getValue().toString();
		String smonth = monthStart.getValue().toString();
		String sday = dayStart.getValue().toString();
		System.out.println("year: "+syear+" month: "+smonth+" day: "+sday);
		String startDateString = sday + "/" + smonth + "/" + syear;
		
	
		String eyear = yearEnd.getValue().toString();
		String emonth = monthEnd.getValue().toString();
		String eday = dayEnd.getValue().toString();
		System.out.println("year: "+eyear+" month: "+emonth+" day: "+eday);
		String endDateString =  eday + "/" + emonth+ "/" + eyear ;
		
		String show = category.getValue().toString();
		
		
		try {
			
			java.util.Date javaDate = formatter.parse(startDateString);
			startTime = new Timestamp(javaDate.getTime());
			javaDate = formatter.parse(endDateString);
			endTime = new Timestamp(javaDate.getTime());
			System.out.println(startTime);
			System.out.println(endTime);
			Integer typeSelect[] = new Integer[1];
			if(show.equals("הכול"))
				typeSelect = null;
			else
				typeSelect[0] = transactionTypeMap.get(show);
			DataResult result = layerFactory.getTransactions(null, typeSelect, null, null,null, null, null, startTime,endTime,null);
			System.out.println(result.getRows().length);
		/*	for(int i=0;i<result.getColumnNames().length;i++){
				System.out.println(result.getColumnNames()[i]);
			}*/
			
			for(int i=0;i<result.getRows().length;i++){
			this.transaction.add(creatTransaction(result.getRows()[i]));
				
			}
				
		}catch(Exception e){
			e.printStackTrace();
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
