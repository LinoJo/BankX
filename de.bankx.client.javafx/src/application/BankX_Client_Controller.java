package application;

import application.Table;

//import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.event.Event;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Label lblServerip;

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
    private TableColumn<Table, String> col_Datum;

    @FXML
    private TableColumn<Table, String> col_Verwendungszweg;

    @FXML
    private Button btnAbmelden;

    @FXML
    private Label lblWarnungStatus;

    @FXML
    private ImageView imageView;


    final ObservableList<Table> allTransactions = FXCollections.observableArrayList();

    boolean login = false;
    boolean first = true;
	int loginindex = -1;
	String _kontonummer = "";
	String _kontoinhaber = "";
	String _kontostand ="";
	String _serverip = "";
	String _oldTab ="";

	JSONObject _$Session = new JSONObject();

    @FXML
    void handleButtonEvent(ActionEvent event) {
    	reloadSession();
    	if(event.getSource().equals(btnVerbindung))
    	{
    		lblWarnungLogin.setTextFill(Color.RED);
    		if(CheckLoginData()){
    			try {
    				reloadSession();
					login=true;
					if(login){
						showImage();
						_serverip = txtServerip.getText();
						_kontonummer = txtKontonummer.getText();
						_kontoinhaber= _$Session.get("owner").toString();
						reloadKontostand(0);
						//_kontostand = NetClientGet.getRequest(txtServerip.getText(), ("account/"+_kontonummer+"/value"));
						//lblKontostand.setText(_kontostand);
						lblServerip.setText(_serverip);
						lblKontoinhaber.setText(_kontoinhaber);
						lblKontonummer.setText(_kontonummer);
						grid_info.setOpacity(1);

						initialTable();
						switchLoginModus();
					}
					else{
						lblWarnungLogin.setText("Fehlerhafte Kontonummer: "+ txtKontonummer.getText());
					}
				} catch (Exception e) {
					e.printStackTrace();
					//lblWarnungLogin.setText(allAccounts);
				}
    		}
    	}

    	else if(event.getSource().equals(btnAbmelden))
    	{
    		//System.out.println(login);
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
    		reloadKontostand(1);
    		reloadTransakionshistorie();
    		tabPane.getSelectionModel().select(tabTransaktionshistorie);
    	}
    	else if(event.getSource().equals(btnPageUeberweisung))
    	{
    		reloadKontostand(2);
    		tabPane.getSelectionModel().select(tabUeberweisung);
    	}

    	else if(event.getSource().equals(btnSenden))
    	{
    		//lblWarnungUeberweiung.setTextFill(Color.RED);
    		if(CheckUeberweisungData())
    		{
    			String res = NetClientPost.postRequest(_serverip,makeJsonUeberweisung(),lblWarnungUeberweiung);
    			System.out.println(res);
    			if(res !=""){
    				clearElementAfter(txtBetrag,1);
    				clearElementAfter(txtEmpfaenger,1);
    				clearElementAfter(txtVerwendungszweg,1);
    				lblWarnungUeberweiung.setTextFill(Color.GREEN);
    				lblWarnungUeberweiung.setText(res);
    				clearElementAfter(lblWarnungUeberweiung,5000);
    			}
    		}
    		reloadKontostand(3);
    	}
    }

    @FXML
    void handleTabEvent(Event event) {
    	if(event.getSource().equals(tabLogin)){
    		//
    	}
    	if(event.getSource().equals(tabIndex)){
    		//
    	}
    	if(event.getSource().equals(tabUeberweisung)){
    		//
    	}
    	if(event.getSource().equals(tabTransaktionshistorie)){
    		reloadTransakionshistorie();
    		//System.out.println("bool:	"+event.getSource().equals(tabTransaktionshistorie));
    	}
    	//reloadKontostand(4);
    }

    public void reloadSession(){
    	try {
    		String accountItem = NetClientGet.getRequest(txtServerip.getText(), "account/"+txtKontonummer.getText(),lblWarnungStatus,lblWarnungLogin);
    		JSONObject jObj = new JSONObject(accountItem);
    		_$Session = jObj;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    public boolean CheckUeberweisungData(){
    	try {
	        if ( txtEmpfaenger.getText() == null || txtEmpfaenger.getText().isEmpty() ) {
	            throw new NumberFormatException("Unvollst�ndige Eingabe | Empf�nger!");
	        }
	        if ( txtVerwendungszweg.getText() == null || txtVerwendungszweg.getText().isEmpty() ) {
	        	throw new NumberFormatException("Unvollst�ndige Eingabe | Verwendungszweg!");
	        }
	        if ( txtBetrag.getText() == null || txtBetrag.getText().isEmpty() ) {
	        	throw new NumberFormatException("Unvollst�ndige Eingabe | Betrag!");
	        }
	        String regex = "[0-9]+";
	    	if (!txtEmpfaenger.getText().matches(regex)) {
	    		throw new NumberFormatException("Ung�ltige(s) Zeiche(s) in der Kontonummer des Empf�ngers!");
	    	}
	        if ( 999 > Integer.parseInt(txtKontonummer.getText()) || 2000 < Integer.parseInt(txtKontonummer.getText())) {
	        	throw new NumberFormatException("Ung�ltige Angabe der Kontonummer des Empf�ngers!");
	        }

	        String[] parts = txtBetrag.getText().split( "\\," );

	        if ( parts.length > 2 ) {
	        	throw new NumberFormatException("Ung�ltige Eingabe des Betrags, bitte mit ',' trenen!");
	        }

	        for ( String s : parts ) {
	        	if (!s.matches(regex)) {
    	    		throw new NumberFormatException("Ung�ltige(s) Zeiche(s) im Betrag!");
    	    	}
	            int i = Integer.parseInt( s );
	            if ( (i < 0)) {
	            	throw new NumberFormatException("Ung�ltige Betrag!");
	            }
	        }
	        if ( 0 > Integer.parseInt(txtBetrag.getText()) && Integer.parseInt(_kontostand) <= Integer.parseInt(txtBetrag.getText())) {
	        	throw new NumberFormatException("Ung�ltiger Betrag!");
	        }

	        return true;
	    } catch (NumberFormatException nfe) {
	    	lblWarnungUeberweiung.setText(nfe.getMessage());
	        return false;
	    }
    }

    public boolean CheckLoginData(){
    	    try {
    	        if ( txtServerip.getText() == null || txtServerip.getText().isEmpty() ) {
    	            throw new NumberFormatException("Unvollst�ndige Eingabe!");
    	        }

    	        String[] parts = txtServerip.getText().split( "\\." );
    	        if ( parts.length != 4 ) {
    	        	throw new NumberFormatException("Ung�ltige L�nge der ServerIP!");
    	        }

    	        String regex = "[0-9]+";
    	        for ( String s : parts ) {
    	        	if (!s.matches(regex)) {
        	    		throw new NumberFormatException("Ung�ltige(s) Zeiche(s) in der ServerIP!");
        	    	}
    	            int i = Integer.parseInt( s );
    	            if ( (i < 0) || (i > 255) ) {
    	            	throw new NumberFormatException("Ung�ltige Angabe der ServerIP!");
    	            }
    	        }

    	        if ( txtServerip.getText().endsWith(".") ) {
    	        	throw new NumberFormatException("Ung�ltige Angabe der ServerIP!");
    	        }
    	        if ( txtServerip.getText().endsWith(".")) {
    	        	throw new NumberFormatException("Ung�ltige Angabe der ServerIP!");
    	        }
    	    	if (!txtKontonummer.getText().matches(regex)) {
    	    		throw new NumberFormatException("Ung�ltige(s) Zeiche(s) in der Kontonummer");
    	    	}
    	        /*if ( 999 > Integer.parseInt(txtKontonummer.getText()) || 2000 < Integer.parseInt(txtKontonummer.getText())) {
    	        	throw new NumberFormatException("Ung�ltige Angabe der Kontonummer");
    	        }*/
    	        return true;
    	    } catch (NumberFormatException nfe) {
    	    	lblWarnungLogin.setText(nfe.getMessage());
    	        return false;
    	    }

    }

    public void reloadKontostand(int number){
    	System.out.println("It�s me: "+number);
    	double amount = 0;
    	if(login){
    		try {
    		JSONArray jAry = new JSONArray(_$Session.get("transactions").toString());
			for(int i=0; i < jAry.length(); i++){
				//System.out.println(i+" "+jAry.get(i).toString());
				JSONObject transaktion = new JSONObject(jAry.get(i).toString());
				JSONObject receiver = new JSONObject(transaktion.get("receiver").toString());
				JSONObject sender = new JSONObject(transaktion.get("sender").toString());
				if(receiver.get("number").toString().equals(_kontonummer) ){
					amount += transaktion.getDouble("amount");
					}
				if(sender.get("number").toString().equals(_kontonummer) ){
					amount -= transaktion.getDouble("amount");
					}
			}

    		} catch (JSONException e) {
				e.printStackTrace();
			}

    		_kontostand = ""+amount;
			//_kontostand = NetClientGet.getRequest(txtServerip.getText(), ("account/"+_kontonummer+"/value"), lblWarnungStatus);
			lblKontostand.setText(_kontostand);
    	}
    }

    /**
     * HashMap..map a HashMap, make a JSON, do Some Magic Stuff
     *
     * @return	jsonParam {<Kontonummer>,<Empf�nger>,<Betrag>,<Verwendungszweg>}
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
		col_Datum.setCellValueFactory(new PropertyValueFactory<>("date"));
		col_Verwendungszweg.setCellValueFactory(new PropertyValueFactory<>("reference"));
    }

    /**
     * SwitchLoginModus change the selected TabPane and setDisable
     */
    private void switchLoginModus(){
    	if(login){
    		tabPane.getSelectionModel().select(tabIndex);
    		}
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
		lblWarnungStatus.setText("");
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
    	//NOTE: Nur um missverstandnisse zu Umgehen und Error-Handling f�r nicht Funktionale DatenTypen
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
    	//http://10.13.37.113:9998/rest/account/2000
    	//String res = NetClientGet.getRequest(_serverip, "account/"+_kontonummer,lblWarnungStatus);
    	if(login){
    		try {
    			System.out.println("Transaktion");
    			JSONArray jAry = new JSONArray(_$Session.get("transactions").toString());
    			for(int i=0; i < jAry.length(); i++){
    				//System.out.println(i+" "+jAry.get(i).toString());
    				JSONObject transaktion = new JSONObject(jAry.get(i).toString());
    				JSONObject receiver = new JSONObject(transaktion.get("receiver").toString());
    				JSONObject sender = new JSONObject(transaktion.get("sender").toString());

    				Table tb = new Table();

    				String _sender = sender.get("number").toString();
    				String _receiver = receiver.get("number").toString();
    				String _amount = transaktion.get("amount").toString();
    				String _reference = transaktion.get("reference").toString();
    				String _formattedDate = transaktion.get("transactionDate").toString().substring(0, 10);

    				System.out.println("-------------------\n"+_sender +"/n"+_receiver+"/n"+_amount+"/n"+_reference+"/n"+_formattedDate);
    				System.out.println();

    				tb.setSender(_sender);
					tb.setReceiver(_receiver);
					tb.setReference(_reference);
					tb.setDate(_formattedDate);
					//tb.setAmount(_amount);
					if(receiver.get("number").toString().equals(_kontonummer) ){
						tb.setAmount("+ "+_amount);
						}
					if(sender.get("number").toString().equals(_kontonummer) ){
						tb.setAmount("- "+_amount);
						}
					allTransactions.add(tb);
    			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		table_transaktionshistorie.setItems(allTransactions);
    	}
    }
    public void showImage() {
        try {
            Image image = new Image(getClass().getResourceAsStream("bankx_edit.jpg"));
            imageView.setImage(image);
            imageView.setCache(true);
        } catch (Exception e) {
           //System.out.println(e);
        	//printStackTrace();
        }
    }

}
