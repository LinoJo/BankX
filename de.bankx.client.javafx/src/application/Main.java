package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {

	static String serverip;
	static String kontonummer;

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("BankX_Client_Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	/*public static String loginServer(String _serverip){
		serverip = _serverip;
		kontonummer = _kontonummer;

		String JsonURL = "http://"+serverip+":9998/rest/account/"+kontonummer;
		return "";
	}*/

	public static String logoutServer(){
		serverip = "";
		kontonummer = "";
		//String JsonURL = "";
		return "Logout erfolgreich!";
	}
}
