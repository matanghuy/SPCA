package controllers;

import beans.TransactionType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import spca.datalayer.DataContext;
import spca.datalayer.SpcaDataLayerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class addTransactionType implements Initializable{
	
	private static final Logger logger = Logger.getLogger(addTransactionType.class);
	@FXML private ChoiceBox<TransactionType> category;
	@FXML private TextField operationName;
	DataContext dataContext;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        try {
            dataContext = SpcaDataLayerFactory.getDataContext();
        } catch (IOException e) {
            logger.error("Can't get data context", e);
        }
        List<TransactionType> transactionTypes = CommonUtils.getTransactionTypes();
        ObservableList<TransactionType> typeObservableList = FXCollections.observableArrayList(transactionTypes);
        category.setItems(typeObservableList);
        category.setValue(category.getItems().get(0));


    }
		@FXML
		public void create(ActionEvent event){
			try {
				dataContext.addTransactionType(operationName.getText());
                logger.info("New transaction type created: " + operationName.getText());
			} catch (SQLException e) {
                logger.error("Error occurred while creating transaction type", e);
			}
			((Node)event.getSource()).getScene().getWindow().hide();
		}

}
