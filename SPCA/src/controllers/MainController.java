package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class MainController implements Initializable {

    private static final Logger logger = Logger.getLogger(MainController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CommonUtils.initializeCity();

	}

	@FXML
	public void buttonsHandler(ActionEvent event) {
		Button clicked = (Button) event.getSource();
		String windowName;
		switch (clicked.getId()) {
		case "btnNewIncome":
			windowName = "newTransaction.fxml";
			break;
		case "btnShowIncome":
			windowName = "incomingReport.fxml";
			break;
		case "btnNewExpense":
			windowName = "newTransaction.fxml";
			break;
		case "btnShowExpense":
			windowName = "incomingReport.fxml";
			break;
		case "btnMonthlyDest":
			windowName = "StatusByMonth.fxml";
			break;
        case "btnYearlyDest":
			windowName = "FindYearlyStatus.fxml";
			break;
		case "btnAddMonthlyDest":
			windowName = "newDestination.fxml";
			break;
        case "btnAddYearlyDest":
			windowName = "newYearlyDestination.fxml";
			break;
        case "btnAddTransactionType":
			windowName = "addTransactionType.fxml";
			break;
		default:
			logger.error("Error occurred! cant find page to view");
            windowName= "";
		}

		createNewWindow(windowName);
		
	}

	private void createNewWindow(String windowName) {
		try {
			Stage dialog = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/"+windowName));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root, 1000, 800);
			dialog.setScene(scene);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
