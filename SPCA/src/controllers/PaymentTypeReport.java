package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import beans.Contact;
import beans.PaymentReport;
import beans.StatusByYear;
import org.apache.log4j.Logger;
import spca.datalayer.DataContext;
import spca.datalayer.DataResult;
import spca.datalayer.DataRow;
import spca.datalayer.SpcaDataLayerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PaymentTypeReport implements Initializable{

    private static final Logger logger = Logger.getLogger(PaymentTypeReport.class);
	@FXML ComboBox year;
	@FXML ComboBox month;
	@FXML TableView<PaymentReport> table;
	@FXML TableColumn name;
	@FXML TableColumn date;
	DataContext layerFactory;
	Timestamp startDate,endDate;
	private final String day = "1";
	private final String datePattern = "dd/MM/yyyy";
	private SimpleDateFormat formatter;
	private ObservableList<PaymentReport> report;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			report = FXCollections.observableArrayList();
			table.setItems(report);
			layerFactory = SpcaDataLayerFactory.getDataContext();
		} catch (IOException e) {
			logger.error("Error occurred while connecting to database", e);
		}
		initTable();
		initalizeDates();
		
	}
	private void initTable(){
		name.setCellValueFactory(new PropertyValueFactory<PaymentReport, String>("name"));
		date.setCellValueFactory(new PropertyValueFactory<PaymentReport, String>("amount"));
	}
	
	private void initalizeDates(){
		ArrayList<String> monthArray = new ArrayList<String>();
		ArrayList<String> yearArray = new ArrayList<String>();
		for(int i=1;i<13;i++){
			monthArray.add(i+"");
		}
		for(int i=10;i<31;i++){
			yearArray.add("20"+i);
		}
		ObservableList<String> yearObserver = FXCollections.observableArrayList(yearArray);
		ObservableList<String> monthObserver = FXCollections.observableArrayList(monthArray);
		year.setItems(yearObserver);
		month.setItems(monthObserver);
		
		String cuurentDate = (Calendar.getInstance().get(Calendar.MONTH))+1+""; 
		String currentYear = Calendar.getInstance().get(Calendar.YEAR) + "";
		
		year.setValue(currentYear);
		month.setValue(cuurentDate);

	}
	
	@FXML
	public void search(){
		String endYear,endMonth;
		ArrayList<Integer> transactionsId = new ArrayList<Integer>();
		HashMap<Integer,Integer> paymentMap = new HashMap<Integer, Integer>();
		HashMap<Integer,String> paymentName = new HashMap<Integer, String>();
		report.clear();
		formatter = new SimpleDateFormat(datePattern);
		String syear = year.getValue().toString();
		String smonth = month.getValue().toString();
		String startDateString = day + "/" + smonth + "/" + syear;
		if(Integer.parseInt(month.getValue().toString()) == 12){
			endMonth = "1";
			endYear = Integer.parseInt(syear) + 1 + "";
		}
		else{
			endMonth = Integer.parseInt(smonth) + 1 + "";
			endYear = syear;
		}
		String endDateString =  day + "/" + endMonth+ "/" + endYear ;

		
		
		try {
			
			java.util.Date javaDate = formatter.parse(startDateString);
			startDate = new Timestamp(javaDate.getTime());
			javaDate = formatter.parse(endDateString);
			endDate = new Timestamp(javaDate.getTime());
			
			
			DataResult data3 ;
			data3 = layerFactory.getTransactions(null, null, null, null, null, null, null, startDate, endDate, null);
			for(int i=0;i<data3.getRows().length;i++){
				transactionsId.add((Integer) data3.getRows()[i].getObject("TransactionId") );
			}
			for(int i=0;i<transactionsId.size();i++){
				data3 = layerFactory.getPaymentsForTransaction(transactionsId.get(i));
				if(data3.getRows().length > 0){
					Integer paymentType =(Integer) data3.getRows()[0].getObject("PaymentType");
					Integer paymentAmount =(Integer)(((BigDecimal) data3.getRows()[0].getObject("Amount")).intValue());
					Integer sum = paymentMap.get(paymentType);
					if(sum != null){
						sum += paymentAmount;	
					}
					paymentMap.put(paymentType, paymentAmount);
				}
			}
			data3 = layerFactory.getPaymentTypes();
			for(int i=0;i<data3.getRows().length;i++){
				Integer id =(Integer) data3.getRows()[i].getObject("Id");
				String name = (String) data3.getRows()[i].getObject("Name");
				paymentName.put(id, name);
			}
			 Iterator it = paymentName.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			        this.report.add(createRow((String)(pairs.getValue().toString()),paymentMap.get(pairs.getKey())));
			    }
			
			
				
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	private PaymentReport createRow(String string, Integer integer) {
		PaymentReport payment = new PaymentReport();
		payment.setName(string);
		payment.setAmount(integer+"");
		return payment;
	}


}
