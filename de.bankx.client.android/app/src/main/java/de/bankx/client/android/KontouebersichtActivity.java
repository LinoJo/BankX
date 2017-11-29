package de.bankx.client.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class KontouebersichtActivity extends AppCompatActivity {

    // JSON Node names
    private static final String TAG_STUDENT_INFO = "studentsinfo";
    private static final String TAG_ID = "id";
    private static final String TAG_STUDENT_NAME = "sname";
    private static final String TAG_STUDENTEMAIL = "semail";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_STUDENT_GENDER = "gender";
    private static final String TAG_STUDENT_PHONE = "sphone";
    private static final String TAG_STUDENT_PHONE_MOBILE = "mobile";
    private static final String TAG_STUDENT_PHONE_HOME = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontouebersicht);


        TextView kontonummer = (TextView) findViewById(R.id.textKontonummer);

        String jsonURL = getIntent().getStringExtra("jsonUrl");
        String sKontonummer = getIntent().getStringExtra("kontonummer");
        kontonummer.setText(sKontonummer);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
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

/*
    public void getJSONObject(String url)
    {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        String kontodatenXmlString = "";

        try {
            URL webUrl = new URL(jsonUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
        }
        catch (IOException e){

        }
    }
    */
}
