package de.bankx.client.android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Überweisungs Aktivität, überweisungen an andere Konten tätigen
 * @Author Dennis Nüßing
 */

public class UeberweisungActivity extends AppCompatActivity {

    private String jsonUrl;
    private String sZweck;
    private String sBetrag;
    private String sEkontonummer;
    private String sKontonmuer;

    private String sSaldo;

    private EditText zweck;
    private EditText betrag;
    private EditText kontonummer;
    private TextView guthaben;


    /**
     * Mehtode die beim Start der Aktivität aufgerufen wird
     * (mapped Textfelder/Buttons | holt sich die jsonURL von der Main Activity | Intialisiert das Saldo | startet das Senden der Daten an den Server)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ueberweisung);

        zweck = (EditText) findViewById(R.id.eingabeZweck);
        betrag = (EditText) findViewById(R.id.eingabeBetrag);
        kontonummer = (EditText) findViewById(R.id.eingabeEmpfaenger);
        sKontonmuer = getIntent().getStringExtra("Kontonummer");
        sSaldo = getIntent().getStringExtra("Guthaben");

        guthaben = (TextView) findViewById(R.id.textGuthaben);
        guthaben.setText(sSaldo);

        jsonUrl = "http://"+MainActivity.sIpAdresse+":9998/rest/transaction";


        Button btn = (Button) findViewById(R.id.buttonUeberweisen);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendTransaction().execute();
            }
        });
    }

    /**
     * Erstellt eine Json String aus einer HashMap
     * @return gibt json String zurück
     */

    private HashMap <String, String> makeJson () {
        HashMap <String, String> jsonParam = new HashMap<>();
        sZweck = zweck.getText().toString();
        sBetrag = betrag.getText().toString();
        sEkontonummer = kontonummer.getText().toString();

        jsonParam.put("senderNumber", sKontonmuer);
        jsonParam.put("receiverNumber", sEkontonummer);
        jsonParam.put("amount", sBetrag);
        jsonParam.put("reference", sZweck);

        return  jsonParam;
    }

    /**
     * Klasse wird im Hintergrund ausgeführt, sendet Daten an Server
     */
    public class SendTransaction extends AsyncTask<Void, Void, Void> {
        // Hashmap for ListView
        ProgressDialog proDialog;
        AlertDialog.Builder errorMessage;

        String result;

        /**
         * Gibt an was vor dem Ausführen gemacht werden soll
         * (hier, erstellen eines Fortschritt Dialogs)
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(UeberweisungActivity.this);
            proDialog.setMessage("Please wait...");
            proDialog.setCancelable(false);
            proDialog.show();
        }

        /**
         * Führt Aufgabe im Hintergrund aus
         * (in diesem Fall, WebRequest und Parsen des Json Strings)
         * @param arg0 Argumente
         * @return liefert null
         */
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            HashMap <String, String> jsonStr = makeJson();

            result = webreq.makeWebServiceCall(jsonUrl,WebRequest.POSTRequest, jsonStr);

            return null;
        }

        /**
         * Gibt an was nach dem Ausführen gemacht werden soll
         * (in diesem Fall wird der Fortschritts Dialog geschlossen und die zurück zu Kontoübersicht gewechselt, bei Fehler wird ein Error Dialog angezeigt)
         * @param requestresult
         */
        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);
            // Dismiss the progress dialog
            if (proDialog.isShowing())
                proDialog.dismiss();

            if (result != null){
                startActivity(new Intent(getApplicationContext(), KontouebersichtActivity.class));
                finish();
            } else {
                errorMessage = new AlertDialog.Builder (UeberweisungActivity.this);
                errorMessage.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), KontouebersichtActivity.class));
                                finish();
                            }
                        });
                errorMessage.setMessage("Fehler!\n\n"+WebRequest.errorMessage);
                errorMessage.show();
            }
        }
    }
}
