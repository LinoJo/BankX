package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class BankX_Client_Controller {

	@FXML
    private TabPane tabPane;

    @FXML
    private Tab tabLogin;

    @FXML
    private TextField txtKontonummer;

    @FXML
    private TextField txtServerip;

    @FXML
    private Label lblWarnungLogin;

    @FXML
    private Button btnVerbindung;

    @FXML
    private Tab tabIndex;

    @FXML
    private Label lblKontoinhaber;

    @FXML
    private Label lblKontonummer;

    @FXML
    private Label lblKontostand;

    @FXML
    private Button btnPageTransaktionshistorie;

    @FXML
    private Button btnPageUeberweisung;

    @FXML
    private Tab tabUeberweisung;

    @FXML
    private TextField txtEmpfaenger;

    @FXML
    private TextField txtBetrag;

    @FXML
    private Button btnSenden;

    @FXML
    private Tab tabTransaktionshistorie;

    @FXML
    private Button btnAbmelden;

    @FXML
    void handleButtonEvent(ActionEvent event) {
    	if(event.getSource().equals(btnVerbindung))
    	{	// TODO : handelConnection
    		//txtKontonummer.getText();
    		//txtServerip.getText();

    	}
    	else if(event.getSource().equals(btnAbmelden))
    	{
    		// TODO : close connection
    		tabPane.getSelectionModel().select(tabIndex);
    	}
    	else if(event.getSource().equals(btnPageTransaktionshistorie))
    	{
    		tabPane.getSelectionModel().select(tabTransaktionshistorie);
    	}
    	else if(event.getSource().equals(btnPageUeberweisung))
    	{
    		tabPane.getSelectionModel().select(tabUeberweisung);
    	}
    	else if(event.getSource().equals(btnSenden))
    	{
    		//TODO : Send transaction
    		//txtKontonummer.getText();
    		//txtEmpfaenger.getText();
    		//txtBetrag.getText();
    	}
    }
}
