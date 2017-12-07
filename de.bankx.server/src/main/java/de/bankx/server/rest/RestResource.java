package de.bankx.server.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.spi.resource.Singleton;
import de.bankx.server.core.*;
import de.bankx.server.services.DatabaseService;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/")
@Singleton
public class RestResource {

	static Logger log = Logger.getLogger(RestResource.class);
	private TransactionLock trlck = new TransactionLock(false);

	@GET
	@Path("/admin/getAllAccounts")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAccounts(){
		try {
			log.info("REST-API Call: 'localhost:9998/rest/admin/getAllAccounts");
			AccountWrapper acc = new AccountWrapper();
			AccountListWrapper accList = new AccountListWrapper(acc.getListOfAccounts());
			return Response.ok(accList).build();
		} catch (Exception ex){
			log.error("Exception in @Path('/admin/getAllAccounts'): " + ex.getMessage());
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/admin/getAllTransactions")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getTransactions(){
		try {
			log.info("REST-API Call: 'localhost:9998/rest/admin/getAllTransactions");
			Transaction tra = new Transaction();
			TransactionListWrapper traList = new TransactionListWrapper(tra.getListOfTransactions());
			return Response.ok(traList).build();
		} catch (Exception ex){
			log.error("Exception in @Path('/admin/getAllTransactions'): " + ex.getMessage());
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/account/{number}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response account(@PathParam("number") String number) {

		try{

			// Eingabeüberprüfung
			if (!number.matches("[0-9]+") || (number.length() != 4)){
				log.info("REST-API Call: 'localhost:9998/rest/account/" + number + "' - 400 - FEHLERHAFTE EINGABE!");
				return Response.status(Response.Status.BAD_REQUEST).entity("account '" + number + "' invalid").type(MediaType.APPLICATION_JSON).build();
			}

			// Objekt Account erstellen und Daten aus der DB zum Objekt laden
			Account acc = new Account(number);

			// Objekt nicht in DB vorhanden
			if (acc.getId() == 0){
				log.info("REST-API Call: 'localhost:9998/rest/account/" + number + "' - 404 - ACCOUNT NICHT GEFUNDEN!");
				return Response.status(Response.Status.NOT_FOUND).entity("account '" + number + "' not found").type(MediaType.APPLICATION_JSON).build();
			}

			log.info("REST-API Call: 'localhost:9998/rest/account/" + number + "'");
			return Response.ok(acc).build();

		} catch (Exception ex){
			log.error("Exception in @Path('/account/{number}'): " + ex.getMessage());
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/account/{number}/value")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response accountValue(@PathParam("number") String number) {

		try{
			// Eingabeüberprüfung
			if (!number.matches("[0-9]+") || (number.length() != 4)){
				log.info("REST-API Call: 'localhost:9998/rest/account/" + number + "' - 400 - FEHLERHAFTE EINGABE!");
				return Response.status(Response.Status.BAD_REQUEST).entity("account '" + number + "' invalid").type(MediaType.APPLICATION_JSON).build();
			}

			// Objekt Account erstellen und Daten aus der DB zum Objekt laden
			AccountWrapper acc = new AccountWrapper(number);
			String value = acc.getActualValue(number);

			// Objekt nicht in DB vorhanden
			if (acc.getId() == 0){
				log.info("REST-API Call: 'localhost:9998/rest/account/value" + number + "' - 404 - ACCOUNT NICHT GEFUNDEN!");
				return Response.status(Response.Status.NOT_FOUND).entity("account '" + number + "' not found").type(MediaType.APPLICATION_JSON).build();
			}

			log.info("REST-API Call: 'localhost:9998/rest/account/" + number + "/value'");
			return Response.ok(value).build();

		} catch (Exception ex){
			log.error("Exception in @Path('/account/{number}/value'): " + ex.getMessage());
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/transaction")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response execTransact(@FormParam("senderNumber") String senderNumber, @FormParam("receiverNumber") String receiverNumber, @FormParam("amount") String amount, @FormParam("reference") String reference){
		try{
			// Eingabeüberprüfung
			if (!senderNumber.matches("[0-9]+") || (senderNumber.length() != 4)){
				log.info("REST-API Call: 'localhost:9998/rest/transaction/" + " - 400 - FEHLERHAFTE EINGABE!");
				return Response.status(Response.Status.BAD_REQUEST).entity("senderNumber '" + senderNumber + "' invalid").type(MediaType.APPLICATION_JSON).build();
			}

			// Eingabeüberprüfung
			if (!receiverNumber.matches("[0-9]+") || (receiverNumber.length() != 4)){
				log.info("REST-API Call: 'localhost:9998/rest/transaction/" + " - 400 - FEHLERHAFTE EINGABE!");
				return Response.status(Response.Status.BAD_REQUEST).entity("receiverNumber '" + receiverNumber + "' invalid").type(MediaType.APPLICATION_JSON).build();
			}

			// Eingabeüberprüfung
			if (reference.length() == 0){
				log.info("REST-API Call: 'localhost:9998/rest/transaction/" + " - 400 - FEHLERHAFTE EINGABE!");
				return Response.status(Response.Status.BAD_REQUEST).entity("transaction empty or invalid").type(MediaType.APPLICATION_JSON).build();
			}

			// Lock-Überprüfung
			while (trlck.getLocked()){
				wait(1000);
				log.debug("execTransact - Transaction not executed because locked!");
			}
			trlck.setLocked(true);

			// Überprüfung des Guthabens
			AccountWrapper acc = new AccountWrapper();
			if (new Float(acc.getActualValue(senderNumber)) - new Float(amount) < 0){
				log.info("Transaction not executed (not sufficient) "+ senderNumber + " an " + receiverNumber + " - " +  amount + "€ with reference '" + reference + "'");
				trlck.setLocked(false);
				return Response.status(Response.Status.NOT_ACCEPTABLE).entity("account value of '" + acc.getActualValue(senderNumber) + "'€ not sufficient for transaction'").type(MediaType.APPLICATION_JSON).build();
			}
			else{
				Transaction transaction = new Transaction();
				transaction.setSender(new AccountWrapper(senderNumber));
				transaction.setReceiver(new AccountWrapper(receiverNumber));
				transaction.setAmount(new BigDecimal(amount));
				transaction.setReference(reference);
				transaction.addToDB();
				transaction = null;
				log.info("Transaction executed: "+ senderNumber + " an " + receiverNumber + " - " +  amount + "€ with reference '" + reference + "'");
				trlck.setLocked(false);
				return Response.ok().build();
			}

		} catch (Exception ex){
			log.error("Exception in @Path('transaction'): " + ex.getMessage());
			return Response.serverError().build();
		}

	}

	@POST
	@Path("/admin/addAccount")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addAccount(@FormParam("post_owner") String owner, @FormParam("post_amount") String amount){
		try{
			AccountWrapper acc = new AccountWrapper();
			acc.setOwner(owner);
			acc.addToDb(amount);
		} catch (Exception ex){
			log.error("Exception in @Path('/admin/addAccount'): " + ex.getMessage());
			return Response.serverError().build();
		}

		return Response.ok().build();
	}
}

