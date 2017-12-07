package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import application.Main;

import org.json.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class BankX_Client_Controller {

	@FXML
    private GridPane grid_info;

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
    private Label lblStatus;

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
    private TextField txtVerwendungszweg;

    @FXML
    private Button btnSenden;

    @FXML
    private Label lblWarnungUeberweiung;

    @FXML
    private Tab tabTransaktionshistorie;

    @FXML
    private TableView<?> table_transaktionshistorie;

    @FXML
    private TableColumn<?, ?> col_sender;

    @FXML
    private TableColumn<?, ?> col_empfaenger;

    @FXML
    private TableColumn<?, ?> col_Betrag;

    @FXML
    private Button btnAbmelden;

    boolean login = false;
	int loginindex = -1;
	String _kontonummer = "";
	String _kontoinhaber = "";
	String _kontostand ="";
	String _serverip = "";

    @FXML
    void handleButtonEvent(ActionEvent event) {
    	if(event.getSource().equals(btnVerbindung))
    	{
    		lblWarnungLogin.setTextFill(Color.RED);
    		if(!txtServerip.getText().isEmpty() && !txtKontonummer.getText().isEmpty()){
    			String res = NetClientGet.getRequest(txtServerip.getText(), "admin/getAllAccounts");
    			try {
					JSONObject jObj = new JSONObject(res);
					JSONArray jary = jObj.getJSONArray("items");

					for(int i = 0; i < jary.length(); i++){
						//System.out.println(jary.get(i).toString());
						if(jary.getJSONObject(i).get("number").equals(txtKontonummer.getText())){
							login=true;
							loginindex=i;
						}
					}
					if(login){
						_serverip = txtServerip.getText();
						_kontonummer = txtKontonummer.getText();
						_kontostand = NetClientGet.getRequest(txtServerip.getText(), ("account/"+_kontonummer+"/value"));

						_kontoinhaber= jary.getJSONObject(loginindex).get("owner").toString();
						lblStatus.setText("Status:\nOnline");
						lblKontoinhaber.setText("Kontoinhaber:\n"+_kontoinhaber);
						lblKontonummer.setText("Kontonummer:\n"+_kontonummer);
						lblKontostand.setText("Kontostand:\n"+_kontostand);
						grid_info.setOpacity(1);

						tabIndex.setDisable(false);
	    				tabTransaktionshistorie.setDisable(false);
	    				tabUeberweisung.setDisable(false);
	    				tabPane.getSelectionModel().select(tabIndex);
						tabLogin.setDisable(true);
					}
					else{
						lblWarnungLogin.setText("Fehlerhafte Kontonummer");
					}
				} catch (Exception e) {
					e.printStackTrace();
					lblWarnungLogin.setText(res);
				}
    		}
    		else{
    			lblWarnungLogin.setText("Ung�ltige Eingabe");
    		}
    	}


    	else if(event.getSource().equals(btnAbmelden))
    	{
    		System.out.println(login);
    		if(login){
    			login = false;
    			txtKontonummer.setText("");
    			lblStatus.setText("");
    			lblKontonummer.setText("");
    			lblKontoinhaber.setText("");
    			lblKontostand.setText("");
    			txtEmpfaenger.setText("");
    			txtBetrag.setText("");
    			grid_info.setOpacity(0);
    			tabLogin.setDisable(false);
    			tabIndex.setDisable(true);
    			tabTransaktionshistorie.setDisable(true);
    			tabUeberweisung.setDisable(true);
    			lblWarnungLogin.setTextFill(Color.GREEN);
    			lblWarnungLogin.setText("Erfolgreich Abgemeldet");
    			tabPane.getSelectionModel().select(tabLogin);
    		}
    	}


    	else if(event.getSource().equals(btnPageTransaktionshistorie))
    	{
    		/*String res = NetClientGet.getRequest(_serverip, "admin/getAllTransactions");
			try {
				JSONObject jObj = new JSONObject(res);
				JSONArray jary = jObj.getJSONArray("items");
				ArrayList<JSONObject> transaktions = new ArrayList<JSONObject>();
				for(int i = 0; i < jary.length(); i++){
					JSONObject tempobj = new JSONObject(jary.get(i).toString().replace("[", ""));
					System.out.println("NR:"+i+"   "+new JSONObject(tempobj.get("sender")).keys()+"\n\n");
					if(new JSONObject(tempobj.get("sender")).get("number").toString().equals(_kontonummer) || new JSONObject(tempobj.get("receiver")).get("number").toString().equals(_kontonummer))
					{
						transaktions.add(tempobj);
					}
				}

				ObservableList<ObservableList> list = FXCollections.observableArrayList();
				ObservableList<String> row = FXCollections.observableArrayList();
				for(int i = 0; i < transaktions.size(); i++)
				{
					row.add(new JSONObject(transaktions.get(i).getString("sender")).getString("owener").toString());
					row.add( new JSONObject(transaktions.get(i).getString("receiver")).getString("owener").toString());
					row.add(transaktions.get(i).getString("amount").toString());
					System.out.println("Add:" + row.toString());
					list.add(row);
				}

				//table_transaktionshistorie.setItems(list);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(res);
			}*/
    		tabPane.getSelectionModel().select(tabTransaktionshistorie);
    	}
    	else if(event.getSource().equals(btnPageUeberweisung))
    	{
    		tabPane.getSelectionModel().select(tabUeberweisung);
    	}

    	else if(event.getSource().equals(btnSenden))
    	{
    		if(txtEmpfaenger.getText().isEmpty() || txtBetrag.getText().isEmpty() || txtVerwendungszweg.getText().isEmpty())
    			lblWarnungUeberweiung.setText("Ung�ltige Eingabe");
    		else{
    			String res = NetClientPost.postRequest(_serverip,makeJson());
    			lblWarnungUeberweiung.setText(res);
    			if(res.startsWith("ERFOLGREICH")){
    				txtEmpfaenger.setText("");
    				txtBetrag.setText("");
    				txtVerwendungszweg.setText("");
    			}
    		}
    	}
    }

    private HashMap <String,String> makeJson() {
    	HashMap<String,String> jsonParam = new HashMap<>();
    	jsonParam.put("senderNumber", _kontonummer.toString());
    	jsonParam.put("receiverNumber", txtEmpfaenger.getText());
    	jsonParam.put("amount", txtBetrag.getText());
    	jsonParam.put("reference", txtVerwendungszweg.getText());

    	return jsonParam;
	}
}
