package de.bankx.client.android;


import java.io.BufferedReader;
import java.io.BufferedWriter;
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
 * WebRequest Klasse, baut Verbindung zum Server auf und sendet bzw. empfängt Daten
 * @Author Dennis Nüßing
 */

public class WebRequest {
    static String errorMessage = null;
    public final static int GETRequest = 1;
    public final static int POSTRequest = 2;

    /**
     * leerer Konstruktor
     */
    public WebRequest() {
    }

    /**
     * Konstruktor
     * @param url gibt die URL des Servers an
     * @param requestmethod gibt an, ob ein POST oder GET ausgeführt werden soll
     * @return gibt das Ergebniss von makeWebServiceCall zurück
     */
    public String makeWebServiceCall(String url, int requestmethod) {
        return this.makeWebServiceCall(url, requestmethod, null);
    }

    /**
     * Mehtode für den WebServiceCall
     * @param jsonUrl gibt die URL des Servers an
     * @param requestmethod gibt an, ob ein POST oder GET ausgeführt werden soll
     * @param params Daten die per POST gesendet werden sollen
     * @return gibt bei erfolgreichem GET den vom Server empfangenen String, oder bei erfolgreichem POST "OK", oder bei Fehler NULL zurück
     */

    public String makeWebServiceCall(String jsonUrl, int requestmethod, HashMap<String, String> params) {
        URL url;
        HttpURLConnection conn = null;
        String response = "";
        try {
            url = new URL(jsonUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15001);
            conn.setConnectTimeout(15001);

            //überprüft ob GET oder POST gemacht werden soll
            if (requestmethod == POSTRequest) {
                conn.setRequestMethod("POST");
            } else if (requestmethod == GETRequest) {
                conn.setRequestMethod("GET");
            }

            //überprüft ob Parameter mitgegeben wurden
            if (params != null) {
                //öffnet einen Output Stream
                OutputStream ostream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(ostream, "UTF-8"));
                StringBuilder requestresult = new StringBuilder();
                boolean first = true;

                //Wandelt die Hashmap in einen String um und sendet diese
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

                //schließt Verbindungen
                writer.flush();
                writer.close();
                ostream.close();
                response= "OK";
            }

            //empfängt Response Code vom Server
            int reqresponseCode = conn.getResponseCode();

            //Abarbeiten des Response Codes, bei OK Daten Empfangen
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

            // Abfangen sonstiger Fehler beim Verbindungsaufbau
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Verbindung zum Server konnte nicht hergestellt werden\n\n" + e + "\n";
            return null;
        }
        return response;
    }
}
