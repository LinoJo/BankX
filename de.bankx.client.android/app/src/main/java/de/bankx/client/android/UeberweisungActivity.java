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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UeberweisungActivity extends AppCompatActivity {

    private String jsonUrl;
    private String sZweck;
    private String sBetrag;
    private String sEkontonummer;
    private String sKontonmuer;
    private EditText zweck;
    private EditText betrag;
    private EditText kontonummer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ueberweisung);

        zweck = (EditText) findViewById(R.id.eingabeZweck);
        betrag = (EditText) findViewById(R.id.eingabeBetrag);
        kontonummer = (EditText) findViewById(R.id.eingabeEmpfaenger);
        sKontonmuer = getIntent().getStringExtra("Kontonummer");
        jsonUrl = "http://"+MainActivity.sIpAdresse+":9998/rest/transaction";


        Button btn = (Button) findViewById(R.id.buttonUeberweisen);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendTransaction().execute();
            }
        });
    }

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

    public class SendTransaction extends AsyncTask<Void, Void, Void> {
        // Hashmap for ListView
        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(UeberweisungActivity.this);
            proDialog.setMessage("Please wait...");
            proDialog.setCancelable(false);
            proDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            HashMap <String, String> jsonStr = makeJson();

            webreq.makeWebServiceCall(jsonUrl,WebRequest.POSTRequest, jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);

            // Dismiss the progress dialog
            if (proDialog.isShowing())
                proDialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), KontouebersichtActivity.class);
            startActivity(intent);
        }
    }
}
