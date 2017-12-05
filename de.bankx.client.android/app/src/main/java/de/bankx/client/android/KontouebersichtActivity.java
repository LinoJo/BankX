package de.bankx.client.android;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class KontouebersichtActivity extends AppCompatActivity {

    private String jsonUrl;
    private String sKontoinhaber;
    private String sKontonummer;
    private ListView listeTransaktionen;
    private TextView kontonummer;
    private TextView kontostand;
    private TextView kontoinhaber;
    private ArrayList<HashMap<String, String>> transactionList;
    private int saldo;

    // JSON Node names
    private static final String TAG_NUMBER = "number";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_TRANSACTIONS = "transactions";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_RECEIVER = "receiver";
    private static final String TAG_R_ID = "id";
    private static final String TAG_R_NUMBER = "number";
    private static final String TAG_R_OWNER = "owner";
    private static final String TAG_SENDER = "sender";
    private static final String TAG_S_ID = "id";
    private static final String TAG_S_NUMBER = "number";
    private static final String TAG_S_OWNER = "owner";
    private static final String TAG_REFERENCE = "reference";
    private static final String TAG_TRANSACTIONDATE = "transactionDate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontouebersicht);

        kontonummer = (TextView) findViewById(R.id.textKontonummer);
        kontostand = (TextView) findViewById(R.id.textKontostand);
        kontoinhaber = (TextView) findViewById(R.id.textKontoinhaber);

        listeTransaktionen =(ListView)findViewById(R.id.listeTransaktionen);
        listeTransaktionen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KontouebersichtActivity.this, TransaktionDetailActivity.class);
                intent.putExtra("Transaktionen", transactionList);
                intent.putExtra("Position", position);
                startActivity(intent);
            }
        });
        saldo = 0;

        jsonUrl = getIntent().getStringExtra("jsonUrl");

        String sSaldo = Integer.toString(saldo);
        kontostand.setText(sSaldo);

        new GetTransactions().execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuLogout:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.menuUeberweisung:
                startActivity(new Intent(getApplicationContext(), UeberweisungActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class GetTransactions extends AsyncTask<Void, Void, Void> {
        // Hashmap for ListView
        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(KontouebersichtActivity.this);
            proDialog.setMessage("Please wait...");
            proDialog.setCancelable(false);
            proDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonStr = webreq.makeWebServiceCall(jsonUrl, WebRequest.GETRequest);

            Log.d("Response: ", "> " + jsonStr);

            transactionList = ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);
            // Dismiss the progress dialog
            if (proDialog.isShowing())
                proDialog.dismiss();
            /**
             * Updating received data from JSON into ListView
             * */

            ListAdapter adapter = new SimpleAdapter(KontouebersichtActivity.this, transactionList, R.layout.colum_kontouebersicht,
                    new String[]{TAG_TRANSACTIONDATE, TAG_REFERENCE, TAG_AMOUNT},
                    new int[]{R.id.datumData,R.id.zweckData, R.id.summeData});
            listeTransaktionen.setAdapter(adapter);

            String sSaldo = Integer.toString(saldo);
            kontostand.setText(sSaldo);
            kontoinhaber.setText(sKontoinhaber);
            kontonummer.setText(sKontonummer);
        }
    }

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
                    String sId = sender.getString(TAG_S_ID);
                    String sNumber = sender.getString(TAG_S_NUMBER);
                    String sOwner = sender.getString(TAG_S_OWNER);

                    JSONObject receiver = c.getJSONObject(TAG_RECEIVER);
                    String rId = receiver.getString(TAG_R_ID);
                    String rNumber = receiver.getString(TAG_R_NUMBER);
                    String rOwner = receiver.getString(TAG_R_OWNER);

                    if (rOwner.equals (sKontoinhaber)){
                        saldo += Integer.parseInt(amount);
                        amount = "+" +amount;
                    }
                    if (sOwner.equals(sKontoinhaber)){
                        saldo -= Integer.parseInt(amount);
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
