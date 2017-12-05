package de.bankx.client.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by denni on 02.12.2017.
 */

public class GetJson {

    public GetJson(){
    }

    public static String getJSON (String jsonUrl){
        HttpsURLConnection con = null;
        try{
            URL url = new URL (jsonUrl);
            con = (HttpsURLConnection) url.openConnection();

            con.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                sBuilder.append(line + "\n");
            }
            reader.close();
            return sBuilder.toString();
        } catch (MalformedURLException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            if (con !=null){
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
}
