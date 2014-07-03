package controllers;

import beans.Contact;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import spca.datalayer.DataContext;
import spca.datalayer.DataResult;
import spca.datalayer.DataRow;
import spca.datalayer.SpcaDataLayerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class FindContact implements Initializable {

	@FXML
	TextField findName;
	@FXML
	ChoiceBox<String> findAccording;
	@FXML
	ChoiceBox<String> changeView;
	private DataContext database;
	private Map<String, Integer> contactTypes;
	private Map<String, Integer> contactGroups;
	private ObservableList<Contact> contacts;
	@FXML private TableView<Contact> tableContacts;
	@FXML private TableColumn<Contact, String> firstName;
	@FXML private TableColumn<Contact, String> lastName;
	@FXML private TableColumn<Contact, String> address;
	@FXML private TableColumn<Contact, String> phone1;
	@FXML private TableColumn<Contact, String> phone2;
	@FXML private TableColumn<Contact, String> email1;
	@FXML private TableColumn<Contact, String> email2;
	@FXML private TableColumn<Contact, String> category;
	@FXML private TableColumn<Contact, String> city;
	private static ArrayList<String> searchCategories;
	private static final String nameHebrew = "שם";
	private static final String typeHebrew = "קבוצה";
	private static final String typeGroupHebrew = "מחלקה";
	
	
	

	static {
		searchCategories= new ArrayList<String>(3); 
		searchCategories.add(nameHebrew);
		searchCategories.add(typeHebrew);
		searchCategories.add(typeGroupHebrew);
		
		
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getGroupsAndTypes();
		initializeSelectOption();
		initTable();
		contacts = FXCollections.observableArrayList();
		tableContacts.setItems(contacts);
		

	}
	
	public void initTable() {
		firstName.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
		lastName.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
		address.setCellValueFactory(new PropertyValueFactory<Contact, String>("address"));
		phone1.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone1"));
		phone2.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone2"));
		email1.setCellValueFactory(new PropertyValueFactory<Contact, String>("email1"));
		email2.setCellValueFactory(new PropertyValueFactory<Contact, String>("email2"));
		city.setCellValueFactory(new PropertyValueFactory<Contact, String>("city"));
		category.setCellValueFactory(new PropertyValueFactory<Contact, String>("type"));
	}
	//get all contacts types and groups from DB
	private void getGroupsAndTypes() {
		contactTypes = new HashMap<String,Integer>();
		contactGroups = new HashMap<String, Integer>();
		try {
			database = SpcaDataLayerFactory.getDataContext();
	
			DataRow[] types = database.getContactTypes().getRows();
			DataRow[] groups = database.getContactTypeGroups().getRows();
			int typesLength = types.length;
			int groupsLength = groups.length;
		
			for (int i = 0; i < typesLength; i++) {
				String name = (String) types[i].getObject("Name");
				int id = (Integer)types[i].getObject("ID");
				contactTypes.put(name, id);
				
			}

			for (int i = 0; i < groupsLength; i++) {
				String name = (String) groups[i].getObject("Name");
				int id = (Integer)types[i].getObject("ID");
				contactGroups.put(name, id);
			}

		} catch (IOException | SQLException e ) {
			e.printStackTrace();
		}

	}

	private void initializeSelectOption() {
		ObservableList<String> categoryToSearch = FXCollections.observableArrayList(searchCategories);
		
		findAccording.setItems(categoryToSearch);
		findAccording.setValue(findAccording.getItems().get(0));
		
		findAccording.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> arg0,	Number arg1, Number arg2) {
						String newName = (String) (findAccording.getItems().get(((int) arg2)));
						checkView(newName);
					}
				});
		checkView(findAccording.getValue().toString());
	}

	public void checkView(String newName) {
		if (newName.equals(nameHebrew)) {
			findName.setVisible(true);
			changeView.setVisible(false);
		} else if (newName.equals(typeHebrew)) {
			findName.setVisible(false);
			changeView.setVisible(true);
			changeCheckBoxView(contactTypes.keySet());
		} else {
			findName.setVisible(false);
			changeView.setVisible(true);
			changeCheckBoxView(contactGroups.keySet());
		}
	}

	@FXML
	public void search() {
		contacts.clear();
		String contactName = null;
		Integer[] type = null;
		Integer[] group = null;
		String selectedValue;
		switch(findAccording.getValue()) {
		case nameHebrew:
			contactName = findName.getText();
			break;
		case typeHebrew:
			selectedValue = changeView.getValue();
			type = new Integer[1];
			type[0] = this.contactTypes.get(selectedValue);
			break;
		case typeGroupHebrew:
			selectedValue = changeView.getValue();
			group = new Integer[1];
			group[0] = this.contactGroups.get(selectedValue);
		}
		try {
			DataResult result = database.getContacts(null, type, group, contactName, null);
			DataRow[] contactsRows = result.getRows();
			for(int i = 0; i < contactsRows.length; i++) {
				this.contacts.add(createContact(contactsRows[i]));
			}
		}catch(SQLException e) {
			System.out.println("Error while getting data from database" + e.getMessage());
		}
	}
	
	private Contact createContact(DataRow row) {
		Contact contact = new Contact();
		contact.setFirstName((String)row.getObject("FirstName"));
		contact.setLastName((String)row.getObject("LastName"));
		contact.setPhone1((String)row.getObject("Phone_1"));
		contact.setPhone2((String)row.getObject("Phone_2"));
		contact.setEmail1((String)row.getObject("Email_1"));
		contact.setEmail2((String)row.getObject("Email_2"));
		contact.setAddress((String)row.getObject("Address"));
		contact.setCity((String)row.getObject("CityName"));
		contact.setType((String)row.getObject("ContactTypeNames"));
        contact.setId((Integer)row.getObject("ContactId"));

		return contact;
	}
	@FXML
	private void accept(ActionEvent event) {
		Contact selected = tableContacts.getSelectionModel().getSelectedItem();
        CommonUtils.contact = selected;
        ((Node)event.getSource()).getScene().getWindow().hide();

	}

	private void changeCheckBoxView(Collection<String> elements) {
		ObservableList<String> categoryToSearch = FXCollections.observableArrayList(elements);
		changeView.setItems(categoryToSearch);
		changeView.setValue(changeView.getItems().get(0));
	}

}
