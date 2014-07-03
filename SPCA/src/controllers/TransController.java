package controllers;
import beans.Animal;
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
	@FXML private Label lblAnimalType;
    @FXML private Label lblAnimalName;
    @FXML private Label lblTotalToPay;
	@FXML private Label lblTotalPaid;
	@FXML private ToggleGroup contactType;
	@FXML private Button btnLookContact;
	@FXML private Button btnNewContact;
	@FXML private Button btnAddItem;
    @FXML private Button btnSave;
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

    private static Contact currentContact;
    private static Animal currentAnimal;

    private ObservableList<TransactionType> transactionTypesList;
    private ObservableList<TransactionItem> transactionItemsList;
    private Map<String, Integer> paymentTypes;
	DataContext dataContext;
    /* Start Initialization*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			dataContext = SpcaDataLayerFactory.getDataContext();
		} catch (IOException e) {
			e.printStackTrace();
		}
        initPaymentTypes();

        currentContact = null;
        currentAnimal = null;
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
        }catch (Exception e) {
            logger.error("Couldn't get payment types from database", e);
        }
    }

	private void initDateBoxes() {
		final int daysNumber = 32;
		final int monthNumber = 13;
		final int yearNumber = 2030;
		ArrayList<Integer> daysArray = new ArrayList<Integer>();
		ArrayList<Integer> monthArray = new ArrayList<Integer>();
		ArrayList<Integer> yearArray = new ArrayList<Integer>();
		for(int i=1;i<daysNumber;i++){
			daysArray.add(i);
		}
		for(int i=1;i<monthNumber;i++){
			monthArray.add(i);
		}
		for(int i=2010;i<yearNumber;i++){
			yearArray.add(i);
		}
		final ObservableList<Integer> days = FXCollections.observableArrayList(daysArray);
		final ObservableList<Integer> months = FXCollections.observableArrayList(monthArray);
		final ObservableList<Integer> years = FXCollections.observableArrayList(yearArray);
		day.setItems(days);
		month.setItems(months);
		year.setItems(years);
		Integer cuurentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1); 
		Integer currentYear = Calendar.getInstance().get(Calendar.YEAR) ;
		Integer currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		day.setValue(currentDay);
		month.setValue(cuurentMonth);
		year.setValue(currentYear);

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
	/* End of Initialization*/


    @FXML
	private void findContact(){
		openDialog("Find Contact","/fxml/FindContact.fxml");
	}
    @FXML
	private void addContact() {
		openDialog("Add Contact", "/fxml/AddContact.fxml");
	}
    @FXML
    public void attachAnimal() {
        openDialog("Select Animal", "../fxml/FindAnimal.fxml");
    }


    private void openDialog(String title, String fxml) {
		try {
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
                    refreshFields();
                }
            });
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


    @FXML
	private void handleAddItem() {
             TransactionItem item = new TransactionItem(tfItem.getText(),
                     Double.parseDouble(tfCost.getText()), tfItemComment.getText());
			 transactionItemsList.add(item);
             tfItemComment.setText("");
			 tfItem.setText("");
			 tfCost.setText("");
			 updateTotal();
	}

    @FXML
    public void handleRemoveItem() {
        transactionItemsList.remove(tableView.getSelectionModel().getSelectedItem());
        updateTotal();
    }

	private void updateTotal() {
		double sum = 0;
		for(TransactionItem item : transactionItemsList) {
			sum += item.getCost();
		}
		lblTotalToPay.setText(String.valueOf(sum));
	}



	@FXML
	public void handleRadios() {
		RadioButton selected = (RadioButton)contactType.getSelectedToggle();
		if(selected.equals(rbContactFalse)) {
            currentContact = null;
			resetContactFields();
			setButtonsDisabled(true);
		} else {
			setButtonsDisabled(false);
		}

	}
	@FXML
	public void paymentChanged() {
		double sum = parseCostsTextFields(tfCash) + parseCostsTextFields(tfCheck)
                + parseCostsTextFields(tfCredit) + parseCostsTextFields(tfTransfer);
		lblTotalPaid.setText(String.valueOf(sum));
		if (sum > Double.parseDouble(lblTotalToPay.getText())) {
			lblTotalPaid.setTextFill(Paint.valueOf("RED"));
            btnSave.setDisable(true);
		} else {
			lblTotalPaid.setTextFill(Paint.valueOf("BLACK"));
            btnSave.setDisable(false);
		}
	}

	@FXML
	public void save() {
        if (validateInputs()) {
            TransactionType type = cbTransactionType.getSelectionModel().getSelectedItem();
            try {
                int contactId = currentContact == null ? null : currentContact.getId();
                int animalId = currentAnimal == null ? null : currentAnimal.getId();
                int transId = createTransaction(type, contactId, animalId);
                addTransactionItems(transId);
                createPayments(transId);
            } catch (SQLException e) {
                logger.error("Failed to crate transaction", e);
                e.getErrorCode();
                e.getSQLState();
            }
        }
    }
    @FXML
    public void cancel(ActionEvent event) {
        currentAnimal = null;
        currentContact = null;
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    /* Transaction creation */
    private int createTransaction(TransactionType type, Integer contactId, Integer AnimalId) throws SQLException {
        contactId = contactId == -1 ? null : contactId;
        DataResult result = dataContext.addTransaction(contactId, AnimalId, type.getId(), null, null, null);
        int transId = (int)result.getReturnValue();
        logger.info("Transaction created with id: " + transId);
        return transId;
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
            DataResult result = dataContext.addPayment(transId, paymentTypes.get("מזומן"), cash.intValue());
            logger.debug("Payment added. Payment ID: " + result.getReturnValue());
        }
        if (credit > 0) {
            DataResult result = dataContext.addPayment(transId, paymentTypes.get("אשראי"), credit.intValue());
            logger.debug("Payment added. Payment ID: " + result.getReturnValue());
        }
        if (check > 0) {
            DataResult result = dataContext.addPayment(transId, paymentTypes.get("צ'ק"), check.intValue());
            logger.debug("Payment added. Payment ID: " + result.getReturnValue());
        }
        if(transfer > 0) {
            DataResult result = dataContext.addPayment(transId, paymentTypes.get("העברה בנקאית"), transfer.intValue());
            logger.debug("Payment added. Payment ID: " + result.getReturnValue());
        }
    }
        /* End of Transaction creation */

    private boolean validateInputs() {
        if(!lblTotalPaid.getText().equals(lblTotalToPay.getText())) {
            logger.warn("You cannot leave unpaid costs");
            return false;
        }
        if(transactionItemsList.isEmpty()) {
            logger.warn("Transaction must have at least one item ");
            return false;
        }
        if(currentContact.getId() < 0  && rbContactTrue.isSelected()) {
            logger.warn("Choose contact or select 'Without Contact'");
            return false;
        }
        return true;
    }

    private void refreshFields() {
        displayAnimal(currentAnimal);
        displayContact(currentContact);
    }

    private void displayContact(Contact contact) {
        if(contact == null) {
            resetContactFields();
            return;
        }
        fullName.setText(contact.getFullName());
        fullAddress.setText(contact.getFullAddress());
        phoneNumber.setText(contact.getPhoneNumbers());
        email.setText(contact.getEmail1());
    }

    private void displayAnimal(Animal animal) {
        if(animal == null) {
            resetAnimalFields();
            return;
        }
        lblAnimalName.setText(animal.getName());
        lblAnimalType.setText(animal.getType());
    }


	private double parseCostsTextFields(TextField field) {
		String text = field.getText();
		if(text.isEmpty())
			return 0;
		try {
		  return Double.parseDouble(text);
		} catch(NumberFormatException e) {
            logger.warn("This field accept only numbers");
			field.setText("");
			return 0;
		}
	}

	private void resetContactFields() {
		fullName.setText("");
		phoneNumber.setText("");
		fullAddress.setText("");
		email.setText("");
	}
    private void resetAnimalFields() {
        lblAnimalName.setText("");
        lblAnimalType.setText("");
    }

	private void setButtonsDisabled(boolean status) {
		btnLookContact.disableProperty().setValue(status);
		btnNewContact.disableProperty().setValue(status);
	}


    public void removeAnimal() {
        currentAnimal = null;
        resetAnimalFields();
    }

    public static void setContact(Contact contact) {
        currentContact = contact;
    }
    public static void setAnimal(Animal animal) {
        currentAnimal = animal;
    }
}


