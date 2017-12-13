package de.bankx.client.android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Kontoübersicht Aktivität, zeigt eine Übersicht über das Konto
 * @Author Dennis Nüßing
 */

public class KontouebersichtActivity extends AppCompatActivity {

    private String jsonUrl;
    private String sKontoinhaber;
    private String sKontonummer;
    private ListView listeTransaktionen;
    private TextView kontonummer;
    private TextView kontostand;
    private TextView kontoinhaber;
    private ArrayList<HashMap<String, String>> transactionList;
    private int iSaldo;

    // JSON Node names
    private static final String TAG_NUMBER = "number";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_TRANSACTIONS = "transactions";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_RECEIVER = "receiver";
    private static final String TAG_R_NUMBER = "number";
    private static final String TAG_R_OWNER = "owner";
    private static final String TAG_SENDER = "sender";
    private static final String TAG_S_NUMBER = "number";
    private static final String TAG_S_OWNER = "owner";
    private static final String TAG_REFERENCE = "reference";
    private static final String TAG_TRANSACTIONDATE = "transactionDate";

    /**
     * Mehtode die beim Start der Aktivität aufgerufen wird
     * (mapped Textfelder/Buttons | holt sich die jsonURL von der Main Activity | Intialisiert das Saldo | startet das Abrufen der Daten vom Server)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontouebersicht);

        kontonummer = (TextView) findViewById(R.id.textKontonummer);
        kontostand = (TextView) findViewById(R.id.textKontostand);
        kontoinhaber = (TextView) findViewById(R.id.textKontoinhaber);

        // Ruft bei Klick auf ein Element in der Liste die Detailansicht auf
        listeTransaktionen =(ListView)findViewById(R.id.listeTransaktionen);
        listeTransaktionen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KontouebersichtActivity.this, TransaktionDetailActivity.class);
                intent.putExtra("Transaktionen", transactionList);
                intent.putExtra("Position", position-=1);
                startActivity(intent);
            }
        });
        iSaldo = 0;

        jsonUrl = MainActivity.jsonUrl;

        String sSaldo = Double.toString(iSaldo);
        kontostand.setText(sSaldo);

        new GetTransactions().execute();
    }


    /**
     * Methode für Options Menü
     * @param menu gibt Menü an das eingebunden werden soll
     * @return gibt true zurück, wenn Menü erzeugt
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Methode, die bei Menüklick entsprechende Operation ausführt
     * @param item angeklicktes Menüitem
     * @return gibt true zurück wenn Operation ausgeführt
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuLogout:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
            case R.id.menuUeberweisung:
                Intent ueberweisung = new Intent(getApplicationContext(), UeberweisungActivity.class);
                ueberweisung.putExtra("Kontonummer", sKontonummer);
                String sSaldo = Double.toString(iSaldo);
                ueberweisung.putExtra("Guthaben", sSaldo);
                startActivity(ueberweisung);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Klasse, die im Hintergrund ausgeführt wird, um Daten vom Server zu bekommen
     */
    public class GetTransactions extends AsyncTask<Void, Void, Void> {
        ProgressDialog proDialog;
        AlertDialog.Builder errorMessage;

        /**
         * Gibt an was vor dem Ausführen gemacht werden soll
         * (hier, erstellen eines Fortschritt Dialogs)
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // zeigt einen Fortschrittsdialog an
            proDialog = new ProgressDialog(KontouebersichtActivity.this);
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
            // erstellt eine Instanz von WebRequest
            WebRequest webreq = new WebRequest();

            // führt einen Request aus und empfängt String
            String jsonStr = webreq.makeWebServiceCall(jsonUrl, WebRequest.GETRequest);
            Log.d("Response: ", "> " + jsonStr);
            transactionList = ParseJSON(jsonStr);

            return null;
        }

        /**
         * Gibt an was nach dem Ausführen gemacht werden soll
         * (in diesem Fall wird der Fortschritts Dialog geschlossen und die JSON Ergebnisse mittels Adapter auf eine Liste gemappt, bei Fehler wird ein Error Dialog angezeigt)
         * @param requestresult
         */
        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);

            // schließt den Fortschritts Dialog
            if (proDialog.isShowing())
                proDialog.dismiss();

            // mapped die JSON Daten mit der angezeigten Liste, sofern welche vorhanden sind
            if (transactionList != null) {
                View header = getLayoutInflater().inflate(R.layout.colum_header, null);

                ListAdapter adapter = new SimpleAdapter(KontouebersichtActivity.this, transactionList, R.layout.colum_kontouebersicht,
                        new String[]{TAG_TRANSACTIONDATE, TAG_REFERENCE, TAG_AMOUNT},
                        new int[]{R.id.datumData, R.id.zweckData, R.id.summeData});
                listeTransaktionen.addHeaderView(header);
                listeTransaktionen.setAdapter(adapter);

                String sSaldo = Double.toString(iSaldo);
                kontostand.setText(sSaldo);
                kontoinhaber.setText(sKontoinhaber);
                kontonummer.setText(sKontonummer);
            } else{
                //Bei Fehler wird ein Error Dialog angezeigt
                errorMessage = new AlertDialog.Builder (KontouebersichtActivity.this);
                errorMessage.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        });
                errorMessage.setMessage("Fehler!\n\n"+WebRequest.errorMessage);
                errorMessage.show();
            }
        }
    }

    /**
     * Methode zum Parsen des empfangen JSON Strings
     * @param json JSON String der geparst werden soll
     * @return liefert eine Hashmap der JSON Dateien
     */

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap für ListView
                ArrayList<HashMap<String, String>> transactionList = new ArrayList<HashMap<String, String>>();
                JSONObject jsonObj = new JSONObject(json);

                // JSONArray für Transaktionen
                JSONArray transactions = jsonObj.getJSONArray(TAG_TRANSACTIONS);

                // Schleife durch alle Transaktionsobjekte
                sKontonummer = jsonObj.getString(TAG_NUMBER);
                sKontoinhaber = jsonObj.getString(TAG_OWNER);
                for (int i = 0; i < transactions.length(); i++) {
                    JSONObject c = transactions.getJSONObject(i);

                    String amount = c.getString(TAG_AMOUNT);
                    String reference = c.getString(TAG_REFERENCE);
                    String transactionDate = c.getString(TAG_TRANSACTIONDATE);
                    transactionDate = transactionDate.substring(0,10);

                    JSONObject sender = c.getJSONObject(TAG_SENDER);
                    String sNumber = sender.getString(TAG_S_NUMBER);
                    String sOwner = sender.getString(TAG_S_OWNER);

                    JSONObject receiver = c.getJSONObject(TAG_RECEIVER);
                    String rNumber = receiver.getString(TAG_R_NUMBER);
                    String rOwner = receiver.getString(TAG_R_OWNER);

                    if (rOwner.equals (sKontoinhaber)){
                        iSaldo += Double.parseDouble(amount);
                        amount = "+" +amount;
                    }
                    if (sOwner.equals(sKontoinhaber)){
                        iSaldo -= Double.parseDouble(amount);
                        amount = "-" +amount;
                    }

                    // tmp hashmap für einzelne Transaktion
                    HashMap<String, String> transaction = new HashMap<String, String>();

                    // Hinzufügen der Transaktions Eigenschaften zur HashMap key => value
                    transaction.put(TAG_AMOUNT, amount);
                    transaction.put(TAG_REFERENCE, reference);
                    transaction.put(TAG_TRANSACTIONDATE, transactionDate);
                    transaction.put("sNumber", sNumber);
                    transaction.put("sOwner", sOwner);
                    transaction.put("rNumber", rNumber);
                    transaction.put("rOwner", rOwner);

                    // Hinzufügen der Transaktion zur Transaktionsliste
                    transactionList.add(transaction);
                }
                return transactionList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP request");
            return null;
        }
    }
}
