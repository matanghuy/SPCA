package controllers;
import beans.Contact;
import beans.TransactionItem;
import beans.TransactionType;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import spca.datalayer.DataContext;
import spca.datalayer.DataResult;
import spca.datalayer.DataRow;
import spca.datalayer.SpcaDataLayerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

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
import org.apache.log4j.Logger;

public class TransController implements Initializable{

    private static final Logger logger = Logger.getLogger(TransController.class);
	@FXML private ComboBox<Integer> day;
	@FXML private ComboBox<Integer> month;
	@FXML private ComboBox<Integer> year;
	@FXML private ComboBox<TransactionType> cbTransactionType;
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
    @FXML private TextField tfItemComment;
	@FXML private TextField tfCash;
	@FXML private TextField tfCheck;
	@FXML private TextField tfCredit;
    @FXML private TextField tfTransfer;

	@FXML private RadioButton rbContactTrue;
	@FXML private RadioButton rbContactFalse;
	@FXML private TableView<TransactionItem> tableView;
	@FXML private TableColumn<TransactionItem, String> colItemName;
	@FXML private TableColumn<TransactionItem, Double> colItemCost;
    @FXML private TableColumn<TransactionItem, String> colItemComment;

    private ObservableList<TransactionType> transactionTypesList;
    private ObservableList<TransactionItem> transactionItemsList;
    private Map<String, Integer> paymentTypes;
	private int contactId;
	DataContext dataContext;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			dataContext = SpcaDataLayerFactory.getDataContext();
		} catch (IOException e) {
			e.printStackTrace();
		}
        initPaymentTypes();
        CommonUtils.contact = null;
        transactionTypesList = FXCollections.observableArrayList();
		initDateBoxes();
		initTransactionTypes();
		initItemsTable();

	}


    private void initPaymentTypes() {
        paymentTypes = new HashMap<String, Integer>();
        try {
            DataResult result = dataContext.getPaymentTypes();
            for(DataRow row : result.getRows()) {
                paymentTypes.put(((String)row.getObject("Name")).trim(), (Integer)row.getObject("ID"));
            }
            System.out.println(paymentTypes);
        }catch (Exception e) {

        }
    }

	private void initDateBoxes() {
		final ObservableList<Integer> days = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31);
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
        colItemName.setCellValueFactory(new PropertyValueFactory<TransactionItem, String>("name"));
		colItemCost.setCellValueFactory(new PropertyValueFactory<TransactionItem, Double>("cost"));
        colItemComment.setCellValueFactory(new PropertyValueFactory<TransactionItem, String>("comment"));

        transactionItemsList = FXCollections.observableArrayList();
        tableView.setItems(transactionItemsList);
	}


	private void initTransactionTypes() {
        transactionTypesList.addAll(CommonUtils.getTransactionTypes());
        cbTransactionType.setItems(transactionTypesList);
		cbTransactionType.setValue(cbTransactionType.getItems().get(0));
	}
	@FXML
	private void newContact(ActionEvent event){
   		  StringBuilder st = new StringBuilder();
	        st.append(event.getSource());
	        int start = st.indexOf("id")+3;
	        int stop = st.indexOf(",");
	        String name = st.substring(start, stop);
	        switch(name){
	        case "btnLookContact" : findContact();break;
	        case "btnNewContact": addContact(); break;
	        case "btnExit" : System.exit(1);
	        default : System.out.println("not found");

	        }
	}

	private void findContact(){
		openDialog("Find Contact","/fxml/FindContact.fxml");
	}

	private void addContact() {
		openDialog("Add Contact", "/fxml/AddContact.fxml");
	}
    @FXML
    public void attachAnimal() {
        openDialog("Select Animal", "../fxml/FindAnimal.fxml");
    }
	private void openDialog(String title, String fxml) {
		try {
			CommonUtils.contact = null;
            Stage dialog = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
			Parent root = (Parent)loader.load();
			Scene scene = new Scene(root,600,400);
			dialog.setScene(scene);
			dialog.setTitle(title);
			dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    setDisplayedContact(CommonUtils.contact);
                }
            });
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void handleAddItem() {
		 try {
             TransactionItem item = new TransactionItem(tfItem.getText(),
                     Double.parseDouble(tfCost.getText()), tfItemComment.getText());
			 transactionItemsList.add(item);
             tfItemComment.setText("");
			 tfItem.setText("");
			 tfCost.setText("");
			 updateTotal();
		 } catch(NumberFormatException e) {
			//TODO: pop error message
		 }
	}

	private void updateTotal() {
		double sum = 0;
		for(TransactionItem item : transactionItemsList) {
			sum += item.getCost();
		}
		lblTotalToPay.setText(String.valueOf(sum));
	}

	@FXML
	public void handleRemoveItem() {
        transactionItemsList.remove(tableView.getSelectionModel().getSelectedItem());
        updateTotal();
	}

	@FXML
	public void handleRadios() {
		RadioButton selected = (RadioButton)contactType.getSelectedToggle();
		if(selected.equals(rbContactFalse)) {
			resetFields();
            CommonUtils.contact = null;
			setButtonsDisabled(true);
		} else {
			setButtonsDisabled(false);
		}

	}
	@FXML
	public void paymentChanged() {
		double sum = parseText(tfCash) + parseText(tfCheck) + parseText(tfCredit) + parseText(tfTransfer);
		lblTotalPaid.setText(String.valueOf(sum));
		if (sum > Double.parseDouble(lblTotalToPay.getText())) {
			lblTotalPaid.setTextFill(Paint.valueOf("RED"));
		} else {
			lblTotalPaid.setTextFill(Paint.valueOf("BLACK"));
		}
	}

	@FXML
	public void save() {
        if (validateInputs()) {
            TransactionType type = cbTransactionType.getSelectionModel().getSelectedItem();
            try {
                int transId = createTransaction(type, CommonUtils.contact.getId());
                addTransactionItems(transId);
                createPayments(transId);
            } catch (SQLException e) {
                logger.error("Failed to crate transaction: ContactID= " + contactId, e);
                e.getErrorCode();
                e.getSQLState();
            }
        }
    }

    private void addTransactionItems(int transId) throws SQLException{
            for (TransactionItem item : transactionItemsList) {
                dataContext.addTransactionItem(item.getComment(), (int) item.getCost(), transId);
            }
        logger.info(transactionItemsList.size() + " new transaction items has been created");
    }

    private void createPayments(int transId) throws SQLException{
        Double cash = tfCash.getText().equals("") ? 0 : Double.parseDouble(tfCash.getText());
        Double credit = tfCredit.getText().equals("") ? 0 : Double.parseDouble(tfCredit.getText());
        Double check = tfCheck.getText().equals("") ? 0 : Double.parseDouble(tfCheck.getText());
        Double transfer = tfTransfer.getText().equals("") ? 0 : Double.parseDouble(tfTransfer.getText());
        if (cash > 0) {
            dataContext.addPayment(transId, paymentTypes.get("מזומן"), cash.intValue());
        }
        if (credit > 0) {
            dataContext.addPayment(transId, paymentTypes.get("אשראי"), credit.intValue());
        }
        if (check > 0) {
            dataContext.addPayment(transId, paymentTypes.get("צ'ק"), check.intValue());
        }
        if(transfer > 0) {
            dataContext.addPayment(transId, paymentTypes.get("העברה בנקאית"), transfer.intValue());
        }
    }

    private int createTransaction(TransactionType type, Integer contactId) throws SQLException {
        contactId = contactId == -1 ? null : contactId;
        DataResult result = dataContext.addTransaction(contactId, null, type.getId(), null, null, null);
        int transId = (int)result.getReturnValue();
        logger.info("Transaction created with id: " + transId);
        return transId;
    }

	@FXML
	public void cancel(ActionEvent event) {
		((Node)(event.getSource())).getScene().getWindow().hide();
	}
	private boolean validateInputs() {
        if(!lblTotalPaid.getText().equals(lblTotalToPay.getText())) {
            logger.warn("You cannot leave unpaid costs");
            return false;
        }
        if(transactionItemsList.isEmpty()) {
            logger.warn("Transaction must have at least one item ");
            return false;
        }
        if(CommonUtils.contact.getId() < 0  && rbContactTrue.isSelected()) {
            logger.warn("Choose contact or select 'Without Contact'");
            return false;
        }
        return true;
    }

    public void setDisplayedContact(Contact contact) {
        if(contact == null) {
            return;
        }
        fullName.setText(contact.getFullName());
        fullAddress.setText(contact.getFullAddress());
        phoneNumber.setText(contact.getPhoneNumbers());
        email.setText(contact.getEmail1());
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




}


