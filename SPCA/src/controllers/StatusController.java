package controllers;

import beans.StatusByMonth;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import spca.datalayer.DataContext;
import spca.datalayer.DataResult;
import spca.datalayer.DataRow;
import spca.datalayer.SpcaDataLayerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

public class StatusController implements Initializable{
	
	@FXML private Button start;
	@FXML private ComboBox<String> month;
	@FXML private ComboBox<String> year;
	@FXML private TableView<StatusByMonth> tableView;
	@FXML private TableColumn<StatusByMonth, String> colStatus;
	@FXML private TableColumn<StatusByMonth, String> colDestination;
	@FXML private TableColumn<StatusByMonth, String> colSubject;
	@FXML private TableColumn<StatusByMonth, String> colSum;
	@FXML private Text startLabel;
	@FXML private Text endLabel;
	@FXML private Text startField;
	@FXML private Text endField;
	
	DataContext layerFactory;
	private ObservableList<StatusByMonth> statusByMonth;
	private final String datePattern = "dd/MM/yyyy";
	private SimpleDateFormat formatter;
	final String bday = "1";
	private HashMap<String,Integer> transactionTypeMap = new HashMap<String,Integer>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		statusByMonth = FXCollections.observableArrayList();
		tableView.setItems(statusByMonth);
		initTable();
		try {
			layerFactory = SpcaDataLayerFactory.getDataContext();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initComboBox();
		initTransactionType();
		
		
	}
	private void initTransactionType(){
		try {
			layerFactory = SpcaDataLayerFactory.getDataContext();
			DataRow[] data = layerFactory.getTransactionType().getRows();
			int size = data.length;
			for(int i=0;i<size;i++){
				transactionTypeMap.put(data[i].getString(1), i+1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initComboBox(){
		ArrayList<String> monthArray = new ArrayList<String>();
		ArrayList<String> yearArray = new ArrayList<String>();
		for(int i=1;i<13;i++){
			monthArray.add(i+"");
		}
		for(int i=10;i<31;i++){
			yearArray.add("20"+i);
		}
		final ObservableList<String> dates = FXCollections.observableArrayList(monthArray);
		final ObservableList<String> years = FXCollections.observableArrayList(yearArray);

		month.setItems(dates);
		year.setItems(years);
		String cuurentDate = (Calendar.getInstance().get(Calendar.MONTH))+1+""; 
		month.setValue(cuurentDate);
		String currentYear = Calendar.getInstance().get(Calendar.YEAR) + "";
		year.setValue(currentYear);
		
	}

	@FXML
	public void search(){
		statusByMonth.clear();
		String startDate,endDate;
		String endYear,endMonth;
		Timestamp start,end;
		
		
		try {
			if(Integer.parseInt(month.getValue()) == 12){
				endYear = Integer.parseInt(year.getValue()) + 1 + "";
				endMonth = "1";
			}
			else{
				endMonth = Integer.parseInt(month.getValue()) + 1 + "";
				endYear = year.getValue();
			}
			startDate = bday + "/" + month.getValue() +"/" +year.getValue();
			formatter = new SimpleDateFormat(datePattern);
			java.util.Date javaDate = formatter.parse(startDate);
			start = new Timestamp(javaDate.getTime());
			endDate = bday + "/" + endMonth + "/" + endYear;
			formatter = new SimpleDateFormat(datePattern);
			javaDate = formatter.parse(endDate);
			end = new Timestamp(javaDate.getTime());
			
			System.out.println(start);
			System.out.println(end);
			
			DataResult data3 = layerFactory.getBalanceTarget(null, start,end);
			ArrayList<String> nameNotChecked = new ArrayList<String>();
			ArrayList<Integer> totalDestination = new ArrayList<Integer>();
			ArrayList<Integer> totalAmountToPaySum = new ArrayList<Integer>();
			ArrayList<Integer> totalDifferenceSum = new ArrayList<Integer>();
			for(int i=0;i<data3.getRows().length;i++){
				System.out.println(data3.getRows()[i].getObject("Name"));
				System.out.println(data3.getRows()[i].getObject("Amount"));
				nameNotChecked.add((String) data3.getRows()[i].getObject("Name"));
				totalDestination.add((Integer)((BigDecimal)data3.getRows()[i].getObject("Amount")).intValue());
			}
			//boolean isFound = false;
			
			
			DataResult data5 = layerFactory.getTransactions(null,
					null, null, null, null, null,
					null, start, end, null); 
			
			for(int i=0;i<nameNotChecked.size();i++){
				Integer sum = 0;
				for (int j = 0;j < data5.getRows().length; j++) {
					//System.out.println(data5.getRows()[j].getObject("TotalAmountToPay"));
					String typeName =(String) ((data5.getRows()[j].getObject("TransactionTypeName")));
					System.out.println(typeName);
					if(typeName.equals(nameNotChecked.get(i))){
						Object payment = ((data5.getRows()[j].getObject("TotalAmountToPay")));
						if(payment != null){
							sum += ((BigDecimal)payment).intValue();
						}
					}
				}
				totalAmountToPaySum.add(sum);
				totalDifferenceSum.add((sum - totalDestination.get(i)));
				this.statusByMonth.add(createStatusByMonth(nameNotChecked.get(i),
						totalDestination.get(i),totalAmountToPaySum.get(i),totalDifferenceSum.get(i)));
				System.out.println("name is : "+nameNotChecked.get(i) + "destination is: "+totalDestination.get(i)+
						" total payed is: "+totalAmountToPaySum.get(i) + " difference is : "+totalDifferenceSum.get(i));
			}
			
			
			startLabel.setVisible(true);
			startField.setText(startDate);
			startField.setVisible(true);
			endLabel.setVisible(true);
			endField.setText(endDate);
			endField.setVisible(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private StatusByMonth createStatusByMonth(String name,Integer destination, Integer AmountToPay,Integer difference) {
		
		StatusByMonth status = new StatusByMonth();
		status.setComment(((String)(difference+"")));
		status.setSubject((String)(name));
		status.setDestination((String)(destination + ""));
		status.setTotal((String)(AmountToPay + ""));
		System.out.println(status);
		
		return status;
	}
	
	public void initTable() {
		colStatus.setCellValueFactory(new PropertyValueFactory<StatusByMonth, String>("Comment"));
		colDestination.setCellValueFactory(new PropertyValueFactory<StatusByMonth, String>("Destination"));
		colSubject.setCellValueFactory(new PropertyValueFactory<StatusByMonth, String>("Subject"));
		colSum.setCellValueFactory(new PropertyValueFactory<StatusByMonth, String>("Total"));
	}

}
