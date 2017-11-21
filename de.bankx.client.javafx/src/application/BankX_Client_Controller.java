package application;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BankX_Client_Controller {

    @FXML
    private Button btnVerbindung;

    @FXML
    private Label lblLogo;

    @FXML
    private Label lblWarnung;

    @FXML
    private TextField txtKontonummer;

    @FXML
    private TextField txtServerip;

    @FXML
    void handleButtonAction(ActionEvent event) {
    	String Kontonummer = txtKontonummer.getText().toString();
    	String Serverip = txtServerip.getText().toString();


    	if(event.getSource().equals(btnVerbindung)){

    		// doConnection

    		//Warning
    		//lblWarnung.setText("Warnung!");

    	}
    }
}
