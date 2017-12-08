package application;

import application.Table;

//import java.io.IOException;
import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.HashMap;
//import java.util.Timer;
//import java.util.TimerTask;

//import application.Main;

import org.json.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
//import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

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
    private TableView<Table> table_transaktionshistorie;

    @FXML
    private TableColumn<Table, String> col_sender;

    @FXML
    private TableColumn<Table, String> col_empfaenger;

    @FXML
    private TableColumn<Table, String> col_Betrag;

    @FXML
    private TableColumn<Table, String> col_Verwendungszweg;

    @FXML
    private Button btnAbmelden;

    final ObservableList<Table> allTransactions = FXCollections.observableArrayList();

    boolean login = false;
    boolean first = true;
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

						initialTable();
						switchLoginModus();
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
    			lblWarnungLogin.setText("Ungültige Eingabe");
    		}
    	}

    	else if(event.getSource().equals(btnAbmelden))
    	{
    		System.out.println(login);
    		if(login){
    			login = false;
    			switchLoginModus();
    			lblWarnungLogin.setTextFill(Color.GREEN);
    			lblWarnungLogin.setText("Erfolgreich Abgemeldet");
    			clearAll();
    		}
    	}


    	else if(event.getSource().equals(btnPageTransaktionshistorie))
    	{
    		reloadTransakionshistorie();
    		tabPane.getSelectionModel().select(tabTransaktionshistorie);
    	}
    	else if(event.getSource().equals(btnPageUeberweisung))
    	{
    		tabPane.getSelectionModel().select(tabUeberweisung);
    	}

    	else if(event.getSource().equals(btnSenden))
    	{
    		if(txtEmpfaenger.getText().isEmpty() || txtBetrag.getText().isEmpty() || txtVerwendungszweg.getText().isEmpty())
    		{
    			lblWarnungUeberweiung.setText("Ungültige Eingabe");
    			clearElementAfter(lblWarnungUeberweiung,5000);
    		}
    		else
    		{
    			String res = NetClientPost.postRequest(_serverip,makeJsonUeberweisung());
    			lblWarnungUeberweiung.setText(res);
    			if(res.startsWith("ERFOLGREICH")){
    				clearElementAfter(txtBetrag,0);
    				clearElementAfter(txtEmpfaenger,0);
    				clearElementAfter(txtVerwendungszweg,0);
    				clearElementAfter(lblWarnungUeberweiung,5000);
    			}
    		}
    	}
    }

    /**
     * HashMap..map a HashMap, make a JSON, do Some Magic Stuff
     *
     * @return	jsonParam {<kontonummer>,<Empfänger>,<Betrag>,<Verwendungszweg>}
     */
    private HashMap <String,String> makeJsonUeberweisung() {
    	HashMap<String,String> jsonParam = new HashMap<>();
    	jsonParam.put("senderNumber", _kontonummer.toString());
    	jsonParam.put("receiverNumber", txtEmpfaenger.getText());
    	jsonParam.put("amount", txtBetrag.getText());
    	jsonParam.put("reference", txtVerwendungszweg.getText());

    	return jsonParam;
	}

    /**
     * initialTable .. MAGIC: its initial the Table
     */

    private void initialTable(){
		col_sender.setCellValueFactory(new PropertyValueFactory<>("sender"));
		col_empfaenger.setCellValueFactory(new PropertyValueFactory<>("receiver"));
		col_Betrag.setCellValueFactory(new PropertyValueFactory<>("amount"));
		col_Verwendungszweg.setCellValueFactory(new PropertyValueFactory<>("reference"));
    }

    /**
     * SwitchLoginModus change the selected TabPane and setDisable
     */
    private void switchLoginModus(){
    	if(login)
    		tabPane.getSelectionModel().select(tabIndex);
    	else{
    		grid_info.setOpacity(0);
    		tabPane.getSelectionModel().select(tabLogin);
    	}
    	tabIndex.setDisable(!login);
		tabTransaktionshistorie.setDisable(!login);
		tabUeberweisung.setDisable(!login);
		tabLogin.setDisable(login);
    }

    /**
     * ClearAll dynamic TextFields and Label from the GUI
     */
    private void clearAll(){
    	txtKontonummer.setText("");
    	//txtServerip.setText("");
		//lblStatus.setText("");
		lblKontonummer.setText("");
		lblKontoinhaber.setText("");
		lblKontostand.setText("");
		txtEmpfaenger.setText("");
		txtBetrag.setText("");
		txtVerwendungszweg.setText("");
		lblWarnungUeberweiung.setText("");
		clearElementAfter(lblWarnungLogin,5000);
    }


    /**
     * SetText() of the Object after "millis" -> to EmteyString ("")
     * Object must be a TextField or Lable
     * <p>
     * no Return
     *
     * @param	element	type of Object - TextField or Object
     * @param	millis	the Milliseconds of Timespan to *.setText("")
     * @see		setText
     */
    private void clearElementAfter(Object element, int millis){
    	//NOTE: Nur um missverstandnisse zu Umgehen und Error-Handling für nicht Funktionale DatenTypen
    	if(element.getClass().equals(new TextField().getClass()))
    		clearElementAfter(new TextField().getClass().cast(element),millis);
    	else if(element.getClass().equals(new Label().getClass()))
    		clearElementAfter(new Label().getClass().cast(element),millis);
    	else
    		System.out.println("VarType {"+ element.getClass().toString() +"} not support in clearElementAfter(Object element, int millis)!");
    }

    private void clearElementAfter(Label element ,int millis){
    	Timeline timeline = new Timeline(new KeyFrame(
    	        Duration.millis(millis),
    	        ae -> element.setText("")));
    	timeline.play();
    }

    private void clearElementAfter(TextField element ,int millis){
    	Timeline timeline = new Timeline(new KeyFrame(
    	        Duration.millis(millis),
    	        ae -> element.setText("")));
    	timeline.play();
    }

    /**
     * reloadTransakionshistorie reload TransactionsHistory
     * in the Transaktionshistory Tab.
     * Every Call to the Tap call this Methode
     * <p>
     * no Return, only catch Exceptions
     */
    private void reloadTransakionshistorie(){
    	allTransactions.clear();
    	String res = NetClientGet.getRequest(_serverip, "admin/getAllTransactions");
		try {
			JSONObject jObj = new JSONObject(res);
			JSONArray jary = jObj.getJSONArray("items");
			//System.out.println("First: "+jary.get(0).toString());
			for(int i=0;i < jary.length(); i++)
			{
				Table tb = new Table();
				JSONObject trans_obj = new JSONObject(jary.get(i).toString().replace("[", ""));
				//System.out.println("obj:"+trans_obj.get("reference").toString()+" : " + trans_obj.toString());
				//System.out.println(new JSONObject(trans_obj.get("sender").toString()).get("number"));

				String sender = new JSONObject(trans_obj.get("sender").toString()).get("number").toString();
				String receiver = new JSONObject(trans_obj.get("receiver").toString()).get("number").toString();
				String amount = new DecimalFormat("##,###.00").format(trans_obj.get("amount")) + "\u20ac";
				String reference = trans_obj.get("reference").toString();
				if(_kontonummer.equals(sender)){
					tb.setSender(sender);
					tb.setReceiver(receiver);
					tb.setAmount("- "+amount);
					tb.setReference(reference);
					allTransactions.add(tb);
				}
				else if (_kontonummer.equals(receiver)){
					tb.setSender(sender);
					tb.setReceiver(receiver);
					tb.setAmount(amount);
					tb.setReference(reference);
					allTransactions.add(tb);
				}
			}
			table_transaktionshistorie.setItems(allTransactions);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(res);
		}
    }
}
