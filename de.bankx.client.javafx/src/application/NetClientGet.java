package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class NetClientGet {

	public static String getRequest(String _serverip, String _url,Label _lblWarnung) {
		return getRequest(_serverip, _url,_lblWarnung,null);
	}

	// http://	+	IP	+	:9998/rest/	+	URL
	public static String getRequest(String _serverip, String _url,Label _lblWarnung,Label _lblLoginWarning) {

		String _return ="";
	  try {

		URL url = new URL("http://"+_serverip+":9998/rest/"+_url);
		System.out.println(url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setReadTimeout(15001);
		conn.setConnectTimeout(15001);

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK ) {
			String Failed = "";
			switch(conn.getResponseCode()){
				case 301:
					Failed = "Fehler: "+conn.getResponseCode()+":\nZugang nicht mehr gültig";
					break;
				case 400:
					Failed="Fehler: "+conn.getResponseCode()+":\nFehlerhaftes Request";
					break;
				case 404:
					Failed="Fehler: "+conn.getResponseCode()+":\nZugang wurde nicht gefunden";
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
			if(_lblLoginWarning != null){
				_lblLoginWarning.setTextFill(Color.RED);
				_lblLoginWarning.setText(Failed);
			}
			_lblWarnung.setTextFill(Color.RED);
			_lblWarnung.setText("Offline");
			return "conn.getResponseCode()";
			//throw new RuntimeException(Failed);
		}

		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			_lblWarnung.setTextFill(Color.GREEN);
			_lblWarnung.setText("Online");
			//System.out.println("http Connetion is OK: "+conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()),"UTF-8"));
		String output;
		while ((output = br.readLine()) != null) {
			_return += output;
		}

		conn.disconnect();

	  }catch (final java.net.SocketTimeoutException e) {
		  if(_lblLoginWarning != null){
				_lblLoginWarning.setTextFill(Color.RED);
				_lblLoginWarning.setText("TIMEOUT");
			}
			_lblWarnung.setTextFill(Color.RED);
			_lblWarnung.setText("TIMEOUT");
			return "";

	  }catch (MalformedURLException e) {
		  e.printStackTrace();

	  }catch (IOException e) {

		e.printStackTrace();

	  }
	  return _return;
	}

}
