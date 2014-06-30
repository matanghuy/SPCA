package main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{


	public static void main(String[] args) {
		launch();	
	}
	public void start(final Stage primaryStage) {
		try {
			System.out.println(getClass().getResource("."));
			Parent root = (Parent)FXMLLoader.load(getClass().getResource("../expensesReport.fxml"));
			Scene scene = new Scene(root,800,600);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
