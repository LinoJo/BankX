package de.bankx.client.android;

import android.content.Intent;
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
        Button login = (Button) findViewById(R.id.buttonLogin);

        startActivity(new Intent(getApplicationContext(), KontouebersichtActivity.class));

        String ip = ipAdresse.getText().toString();
    }
}