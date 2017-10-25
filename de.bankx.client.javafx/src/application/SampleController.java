package application;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SampleController {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnSub;

    @FXML
    private Button btnMul;

    @FXML
    private Button btnDiv;

    @FXML
    private Button btnClear;

    @FXML
    private Label lblResult;

    @FXML
    private TextField txtNum1;

    @FXML
    private TextField txtNum2;

    @FXML
    void handleButtonAction(ActionEvent event) {
    	double num1 = Double.parseDouble(txtNum1.getText());
    	double num2 = Double.parseDouble(txtNum2.getText());
    	double result = 0;

    	if(event.getSource().equals(btnAdd)){
//    		result = num1 + num2;
    		result = restAddition(num1, num2);
    	}
    	else if (event.getSource().equals(btnSub)){
    		result = num1 - num2;
    	}
    	else if (event.getSource().equals(btnMul)){
    		result = num1 * num2;
    	}
    	else if (event.getSource().equals(btnDiv)){
    		result = num1 / num2;
    	}
    	else {
    		txtNum1.setText("");
    		txtNum2.setText("");
    		txtNum1.requestFocus();
    	}
    	lblResult.setText(String.valueOf(result));
    }

	private double restAddition(double num1, double num2){
		double sum = -1;
		try {
			HttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet("http://localhost:9998/rest/add/" + num1 + "/" + num2);
			HttpResponse response = httpClient.execute(httpGet);
			if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String result = EntityUtils.toString(response.getEntity());
				sum = Double.parseDouble(result);

//				String json = EntityUtils.toString(response.getEntity());
//				Gson gson = new GsonBuilder().create();
//				ResultWrapper myResultWrapper = gson.fromJson(json, ResultWrapper.class);
//				sum = Double.parseDouble(myResultWrapper.getResult());

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sum;
	}

}
