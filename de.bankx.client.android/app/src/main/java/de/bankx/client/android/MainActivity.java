package de.bankx.client.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Haupt Aktivität, hier beginnt die App
 * @Author Dennis Nüßing
 */

public class MainActivity extends AppCompatActivity {

    private EditText ipAdresse;
    private EditText kontonummer;
    private Button loginBtn;

    public static String sIpAdresse;
    public static String jsonUrl;
    private String sKontonummer;


    /**
     * Mehtode die beim Start der Aktivität aufgerufen wird
     * (mapped Textfelder/Buttons)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAdresse = (EditText) findViewById(R.id.eingabeIP);
        kontonummer = (EditText) findViewById(R.id.eingabeKontonummer);
        loginBtn = (Button) findViewById(R.id.buttonLogin);

        ipAdresse.setText(sIpAdresse);
        kontonummer.setText(sKontonummer);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginServer();
            }
        });
    }

    /**
     * Methode zum Server Login
     * (erstellt die jsonUrl | erstellt einen neuen Intent für Kontoübersicht und startet diesen)
     */
    public void loginServer(){
        //Erstellen der JSON URL mithilfe von IP und Kontonummer aus Textfeldern
        sKontonummer = kontonummer.getText().toString();
        sIpAdresse = ipAdresse.getText().toString();
        jsonUrl = "http://"+sIpAdresse+":9998/rest/account/"+sKontonummer;

        //Starten der Kontoübersicht Activity
        startActivity(new Intent(getApplicationContext(), KontouebersichtActivity.class));
        super.finish();
    }
}