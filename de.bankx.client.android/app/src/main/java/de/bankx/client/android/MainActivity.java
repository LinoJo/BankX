package de.bankx.client.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText ipAdresse;
    private EditText kontonummer;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAdresse = (EditText) findViewById(R.id.eingabeIP);
        kontonummer = (EditText) findViewById(R.id.eingabeKontonummer);
        loginBtn = (Button) findViewById(R.id.buttonLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginServer();
            }
        });
    }

    public void loginServer(){
        String sKontonummer = kontonummer.getText().toString();
        String sIpAdresse = ipAdresse.getText().toString();
        String jsonUrl = "http://"+sIpAdresse+":9998/"+sKontonummer;
        Intent kontouebersicht = new Intent(getApplicationContext(), KontouebersichtActivity.class);
        kontouebersicht.putExtra("jsonUrl", jsonUrl);
        kontouebersicht.putExtra("kontonummer", sKontonummer);
        startActivity(kontouebersicht);
        super.finish();
    }
}