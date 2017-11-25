package de.bankx.client.android.bankx;

import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loginServer(View view){

        EditText ipAdresse = (EditText) findViewById(R.id.eingabeIP);
        EditText kontonummer = (EditText) findViewById(R.id.eingabeKontonummer);

        String ip = ipAdresse.getText().toString();

        kontonummer.setText(String.valueOf(ip));
    }

}
