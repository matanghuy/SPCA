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

public class MainController implements Initializable {

	@FXML
	private Button btnNewIncome;
	@FXML
	private Button btnShowIncome;
	@FXML
	private Button btnIncomeDest;
	@FXML
	private Button btnNewExpense;
	@FXML
	private Button btnShowExpense;
	@FXML
	private Button btnExpenseDest;
	@FXML
	private Button btnExpVsInc;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		SharedMethodes.intializeCity();
		
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
		case "btnIncomeDest":
			windowName = "newTransaction.fxml";
			break;
		case "btnNewExpense":
			windowName = "newTransaction.fxml";
			break;
		case "btnShowExpense":
			windowName = "expensesReport.fxml";
			break;
		case "btnExpenseDest":
			windowName = "newTransaction.fxml";
			break;
		default:
			windowName = "errorMessage.fxml";
		}
		createNewWindow(windowName);
		
	}

	private void createNewWindow(String windowName) {
		try {
			Stage dialog = new Stage();
			System.out.println(getClass().getResource("../fxml/"+windowName));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/"+windowName));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root, 800, 600);
			dialog.setScene(scene);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
