package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class NetClientPost {

	// http://	+	IP	+	:9998/rest/	+	URL
	public static String postRequest(String _serverip,HashMap<String,String> jsonStr,Label _lblWarnung) {
		String _return = "";
	  try {

		//System.out.println(_serverip +""+jsonStr.toString());

		URL url = new URL("http://"+_serverip+":9998/rest/transaction");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setReadTimeout(150001);
		conn.setConnectTimeout(15001);
		//conn.setRequestProperty("Content-Type", "application/json");


		OutputStream os = conn.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
		StringBuilder requestres = new StringBuilder();
		boolean one = true;
		for(Map.Entry<String, String> entry : jsonStr.entrySet() ){
			if(one)
				one=false;
			else
				requestres.append("&");
			requestres.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			requestres.append("=");
			requestres.append(URLEncoder.encode(entry.getValue() , "UTF-8"));
			//Debug--Variable
			_return += URLEncoder.encode(entry.getValue() , "UTF-8") + "|";
		}
		//System.out.println(_return.toString());
		writer.write(requestres.toString());

		writer.flush();
		writer.close();
		os.close();

		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
			_lblWarnung.setTextFill(Color.GREEN);
			_return = "Auftrag wurde entgegen genommen";
		}

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK ) {
			String Failed = "";
			switch(conn.getResponseCode()){
			case 301:
				Failed = "Fehler: "+conn.getResponseCode()+":\nServerIP nicht mehr gültig";
				break;
			case 400:
				Failed="Fehler: "+conn.getResponseCode()+":\nFehlerhaftes Request";
				break;
			case 404:
				Failed="Fehler: "+conn.getResponseCode()+":\nServer wurde nicht gefunden";
				break;
			case 500:
				Failed="Fehler: "+conn.getResponseCode()+":\nSammel-Statuscode für unerwartete Serverfehler";
				break;
			case 504:
				Failed="Fehler: "+conn.getResponseCode()+":\nTimeout...";
				break;
			default:
				Failed="Fehler: "+conn.getResponseCode()+"Unbekannter Fehler";
				break;
		}
			_lblWarnung.setTextFill(Color.RED);
			_lblWarnung.setText(Failed);
			_return = "";
			//throw new RuntimeException(Failed);
		}
		conn.disconnect();

	  }catch (final java.net.SocketTimeoutException e) {
			_lblWarnung.setTextFill(Color.RED);
			_lblWarnung.setText("TIMEOUT");
			_return="";

	  }catch (MalformedURLException e) {

		e.printStackTrace();

	  } catch (IOException e) {

		e.printStackTrace();

	 }
	  return _return;
	}
}