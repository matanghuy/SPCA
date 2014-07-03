package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import beans.Contact;
import beans.Month;
import beans.StatusByYear;
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

public class FindYearlyStatus implements Initializable{

	@FXML private ComboBox<String> yearStart;
	@FXML private TableColumn<StatusByYear, String> category;
	@FXML private TableColumn<StatusByYear, String> jan;
	@FXML private TableColumn<StatusByYear, String> feb;
	@FXML private TableColumn<StatusByYear, String> mar;
	@FXML private TableColumn<StatusByYear, String> apr;
	@FXML private TableColumn<StatusByYear, String> may;
	@FXML private TableColumn<StatusByYear, String> jun;
	@FXML private TableColumn<StatusByYear, String> july;
	@FXML private TableColumn<StatusByYear, String> aug;
	@FXML private TableColumn<StatusByYear, String> sep;
	@FXML private TableColumn<StatusByYear, String> ock;
	@FXML private TableColumn<StatusByYear, String> nov;
	@FXML private TableColumn<StatusByYear, String> dez;
	@FXML private TableColumn<StatusByYear, String> counter;
	@FXML private TableColumn<StatusByYear, String> destination;
	@FXML private TableView<StatusByYear> table;
	
	private final String day = "1";
	private final String sMonth = "1";
	private final String eMonth = "12";
	private final int monthesNumber = 12;
	DataContext layerFactory;
	ArrayList<String> years = new ArrayList<String>();
	DataResult data3;
	private final String datePattern = "dd/MM/yyyy";
	private SimpleDateFormat formatter;
	Timestamp startDate,endDate;
	ArrayList<String> categoriesFound = new ArrayList<String>();
	Month[] monthes = new Month[monthesNumber];
	String[] monthNames = {"january", "february", "march", "april", "may", "june", "july", "august", "september",
			"october", "november", "december"};
	private ObservableList<StatusByYear> status;
	HashMap<String,Integer> budgetPerYear = new HashMap<String,Integer>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			layerFactory = SpcaDataLayerFactory.getDataContext();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		status = FXCollections.observableArrayList();
		table.setItems(status);
		initalizeYear();
		initTable();
		initMonthes();
	}
	private void initMonthes(){
		for(int i=0;i<monthesNumber;i++){
			monthes[i] = new Month(monthNames[i]);
		}
	}
	
	private void initalizeYear(){
		
		try {
			data3 = layerFactory.getBalanceTarget(null, null, null);

			for (int i = 0; i < data3.getRows().length; i++) {
				if (((String) (data3.getRows()[i].getObject("Name"))).startsWith("תקציב")) {
					String year = (String) (data3.getRows()[i].getObject("Name"));
					years.add(year.substring(year.length() - 4, year.length()));
				}
			}
			ObservableList<String> yearObserver = FXCollections.observableArrayList(years);
			yearStart.setItems(yearObserver);
			yearStart.setValue(yearStart.getItems().get(0));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initTable(){
			category.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("category"));
			jan.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("january"));
			feb.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("february"));
			mar.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("march"));
			apr.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("april"));
			may.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("may"));
			jun.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("june"));
			july.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("july"));
			aug.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("august"));
			sep.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("september"));
			ock.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("october"));
			nov.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("november"));
			dez.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("december"));
			counter.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("counter"));
			destination.setCellValueFactory(new PropertyValueFactory<StatusByYear, String>("destination"));
			
	}
	@FXML 
	public void handleSearch(){
		status.clear();
		String startDateString,endDateString;
		formatter = new SimpleDateFormat(datePattern);
		startDateString = day + "/" + sMonth + "/" + yearStart.getValue();
		endDateString =  day + "/" + eMonth+ "/" +  yearStart.getValue() ;
		try {
			java.util.Date javaDate = formatter.parse(startDateString);
			startDate = new Timestamp(javaDate.getTime());
			javaDate = formatter.parse(endDateString);
			endDate = new Timestamp(javaDate.getTime());
			System.out.println("start time: "+ startDate + " end time : "+ endDate);
			data3 = layerFactory.getBalanceTarget(null, startDate, endDate);
			for (int i = 0; i < data3.getColumnNames().length; i++) {
				System.out.println(data3.getColumnNames()[i]);
			}
			for (int i = 0; i < data3.getRows().length; i++) {
				if (!((String) (data3.getRows()[i].getObject("Name"))).startsWith("תקציב יעד שנתי")) {
					String category = (String) (data3.getRows()[i].getObject("Name"));
					Iterator<String> it = categoriesFound.iterator();
					boolean notFound = true;
					while (it.hasNext()) {
						String categoryInArray = it.next();
						if (category.equals(categoryInArray)) {
							notFound = false;
							break;
						}
					}
					if (notFound) {
						categoriesFound.add(category);
						String monthString = ((String) (data3.getRows()[i].getObject("StartDate") + ""));

						Integer monthInText = Integer.parseInt(monthString.substring(monthString.length() - 5,
									monthString.length() - 3)) - 1;
						System.out.println(monthInText);
						Integer amount = (Integer) (((BigDecimal) (data3.getRows()[i].getObject("Amount"))).intValue());
						monthes[monthInText].addAmountToCategory(category,amount);
					}
					System.out.println(data3.getRows()[i].getObject("StartDate"));
					System.out.println(data3.getRows()[i].getObject("Name"));
					System.out.println(data3.getRows()[i].getObject("Name"));

				}
				else{
					String category = (String) (data3.getRows()[i].getObject("Name"));
					String budgetCategory = category.substring(category.indexOf("שנתי") + 4, category.length() - 4);
					budgetCategory = budgetCategory.trim();
					Integer budgetAmount = (Integer)(((BigDecimal) (data3.getRows()[i].getObject("Amount"))).intValue());
					budgetPerYear.put(budgetCategory, budgetAmount);
					System.out.println("year category is: "+ budgetCategory);
					System.out.println("year budget is: "+ budgetAmount);
				}

			}
			for(int i=0;i<categoriesFound.size();i++){
				this.status.add(CreateRow(categoriesFound.get(i)));
				/*for(int j=0;j<monthes.length;j++){
					System.out.println("the month is : "+(j+1));
				System.out.println(	monthes[j].getSumPerCategory(categoriesFound.get(i)));
				}*/
			}
		
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	private StatusByYear CreateRow(String category) {
		StatusByYear statusByYear = new StatusByYear();
		statusByYear.setCategory(category);
		statusByYear.setJanuary(monthes[0].getSumPerCategory(category)+"");
		statusByYear.setFebruary(monthes[1].getSumPerCategory(category)+"");
		statusByYear.setMarch(monthes[2].getSumPerCategory(category)+"");
		statusByYear.setApril(monthes[3].getSumPerCategory(category)+"");
		statusByYear.setMay(monthes[4].getSumPerCategory(category)+"");
		statusByYear.setJune(monthes[5].getSumPerCategory(category)+"");
		statusByYear.setJuly(monthes[6].getSumPerCategory(category)+"");
		statusByYear.setAugust(monthes[7].getSumPerCategory(category)+"");
		statusByYear.setSeptember(monthes[8].getSumPerCategory(category)+"");
		statusByYear.setOctober(monthes[9].getSumPerCategory(category)+"");
		statusByYear.setNovember(monthes[10].getSumPerCategory(category)+"");
		statusByYear.setDecember(monthes[11].getSumPerCategory(category)+"");
		statusByYear.setCounter(getCounterForCategory(category));
		System.out.println(budgetPerYear.get("תשלום דמי חבר"));
		System.out.println(category);
		statusByYear.setDestination(budgetPerYear.get(category)+"");
		return statusByYear;
	}
	private String getCounterForCategory(String category){
		Integer sum = 0;
		for(int i=0;i<monthes.length;i++){
			Integer o = monthes[i].getSumPerCategory(category);
			if(o != null)
				sum += o;
		}
		return sum + "";
	}

}
