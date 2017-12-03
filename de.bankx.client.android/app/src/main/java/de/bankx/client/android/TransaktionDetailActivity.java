package de.bankx.client.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class TransaktionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaktion_detail);

        ArrayList<HashMap<String, String>> transactionList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("Transaktionen");
        int id = getIntent().getIntExtra("Position", 0);

        TextView detailEmpfaenger = (TextView) findViewById(R.id.detailEName);
        TextView detailEKontonummer = (TextView) findViewById(R.id.detailEKontonummer);
        TextView detailSender = (TextView) findViewById(R.id.detailSName);
        TextView detailSKontonummer = (TextView) findViewById(R.id.detailSKontonummer);
        TextView detailZweck = (TextView) findViewById(R.id.detailZweck);
        TextView detailSumme = (TextView) findViewById(R.id.detailSumme);

        detailEmpfaenger.setText(transactionList.get(id).get("rOwner"));
        detailEKontonummer.setText(transactionList.get(id).get("rNumber"));
        detailSender.setText(transactionList.get(id).get("sOwner"));
        detailSKontonummer.setText(transactionList.get(id).get("sNumber"));
        detailZweck.setText(transactionList.get(id).get("reference"));
        detailSumme.setText(transactionList.get(id).get("amount"));
    }
}
