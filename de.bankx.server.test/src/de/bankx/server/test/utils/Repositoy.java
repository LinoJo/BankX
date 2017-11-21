package de.bankx.server.test.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import de.fhdw.bank.server.test.model.Account;

public class Repositoy {

	private static final String PARAM_AMOUNT = "amount";
	private static final String PARAM_REFERENCE = "reference";
	private static final String PARAM_SENDER_NUMBER = "senderNumber";
	private static final String PARAM_RECEIVER_NUMBER = "receiverNumber";

	private static final String RESS_GET_ACCOUNT = "/account/";
	private static final String RESS_TRANSFER_MONEY = "/transaction/";

	private String url;
	private String accountNumber;

	private Client client;

	public Repositoy() {
		ClientConfig config = new ClientConfig();
		config.register(new JacksonFeature());

		client = ClientBuilder.newClient(config);
	}

	public Account read() {
		return client.target(url + RESS_GET_ACCOUNT)
				.path(accountNumber).request()
				.get(Account.class);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int transferMoney(String receiverNumber, String amount,
			String reference) {
		Form form = new Form();
		form.param(PARAM_SENDER_NUMBER, accountNumber);
		form.param(PARAM_RECEIVER_NUMBER, receiverNumber);
		form.param(PARAM_AMOUNT, amount);
		form.param(PARAM_REFERENCE, reference);

		Response response = client
				.target(url + RESS_TRANSFER_MONEY)
				.request()
				.post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE),
						Response.class);
		response.close();
		
		return response.getStatus();
	}

}
