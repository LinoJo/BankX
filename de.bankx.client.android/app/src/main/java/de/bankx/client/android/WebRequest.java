package de.bankx.client.android;

import android.os.HandlerThread;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by denni on 01.12.2017.
 */

public class WebRequest {
    static String response = null;
    static String errorMessage = null;
    public final static int GETRequest = 1;
    public final static int POSTRequest = 2;

    //Constructor with no parameter
    public WebRequest() {
    }

    public String makeWebServiceCall(String url, int requestmethod) {
        return this.makeWebServiceCall(url, requestmethod, null);
    }

    public String makeWebServiceCall(String jsonUrl, int requestmethod, HashMap<String, String> params) {
        URL url;
        HttpURLConnection conn = null;
        String response = "";
        try {
            url = new URL(jsonUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15001);
            conn.setConnectTimeout(15001);
            //conn.setDoInput(true);
            //conn.setDoOutput(true);
            if (requestmethod == POSTRequest) {
                conn.setRequestMethod("POST");
            } else if (requestmethod == GETRequest) {
                conn.setRequestMethod("GET");
            }

            if (params != null) {
                OutputStream ostream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(ostream, "UTF-8"));
                StringBuilder requestresult = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (first)
                        first = false;
                    else
                        requestresult.append("&");
                    requestresult.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    requestresult.append("=");
                    requestresult.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
                writer.write(requestresult.toString());

                writer.flush();
                writer.close();
                ostream.close();
            }
            int reqresponseCode = conn.getResponseCode();

            if (reqresponseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else if (reqresponseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                errorMessage = "fehlerhafte Eingabe, Bitte Account überprüfen";
                return null;
            } else if (reqresponseCode == HttpURLConnection.HTTP_NOT_FOUND){
                errorMessage = "Account konnte nicht in der Datenbank gefunden werden";
                return null;
            } else if (reqresponseCode == HttpURLConnection.HTTP_NOT_ACCEPTABLE){
                errorMessage = "Betrag konnte nicht überwiesen werden, kein ausreichender Saldo";
                return null;
            } else {
                errorMessage = "Unbekannter Fehler" +conn.getResponseMessage();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Verbindung zum Server konnte nicht hergestellt werden\n" + e;
        }
        return response;
    }
}
