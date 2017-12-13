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
import java.util.concurrent.locks.ReentrantLock;

/**
 * Klasse der Rest-Ressourcen von BankX
 * @author Timon Caspari
 */
@Path("/")
@Singleton
public class RestResource {

	static Logger log = Logger.getLogger(RestResource.class);
	private final ReentrantLock lock = new ReentrantLock();

	/**
	 * Gibt alle Accounts für das Admin-Interface aus
	 * @return allAccounts as json
	 */
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

	/**
	 * Gibt alle Transaktionen für das Admin-Interface aus
	 * @return allTransactions as json
	 */
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

	/**
	 *
	 * @param number Nummer des Accounts
	 * @return Account-Details mit Transaktionen
	 */
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

	/**
	 *
	 * @param number Nummer des Accounts
	 * @return Guthaben des Accounts
	 */
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

	/**
	 *
	 * @param senderNumber Sender der Transaktion
	 * @param receiverNumber Empfänger der Transaktion
	 * @param amount Summe der Transaktion
	 * @param reference Verwendungszweck der Transaktion
	 * @return ResponseCode
	 */
	@POST
	@Path("/transaction")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response execTransact(@FormParam("senderNumber") String senderNumber, @FormParam("receiverNumber") String receiverNumber, @FormParam("amount") String amount, @FormParam("reference") String reference){
		try{
			// Eingabeüberprüfung senderNumber
			if (!senderNumber.matches("[0-9]+") || senderNumber.length() != 4 || senderNumber.equals(receiverNumber) || receiverNumber.equals(senderNumber)){
				log.info("REST-API Call: 'localhost:9998/rest/transaction/" + " - 400 - BAD REQUEST - Daten mit falschem Format etc., sonstige Client-seitige Fehler");
				return Response.status(Response.Status.BAD_REQUEST).entity("senderNumber '" + senderNumber + "' invalid").type(MediaType.APPLICATION_JSON).build();
			}

			// Eingabeüberprüfung receiverNumber
			if (!receiverNumber.matches("[0-9]+") || (receiverNumber.length() != 4)){
				log.info("REST-API Call: 'localhost:9998/rest/transaction/" + " - 400 - BAD REQUEST - Daten mit falschem Format etc., sonstige Client-seitige Fehler");
				return Response.status(Response.Status.BAD_REQUEST).entity("receiverNumber '" + receiverNumber + "' invalid").type(MediaType.APPLICATION_JSON).build();
			}

			// Eingabeüberprüfung amount
			if (!amount.matches("\\d+(\\.\\d{1,2})?")){
				log.info("REST-API Call: 'localhost:9998/rest/transaction/" + " - 400 - BAD REQUEST - Daten mit falschem Format etc., sonstige Client-seitige Fehler");
				return Response.status(Response.Status.BAD_REQUEST).entity("amount '" + amount + "' invalid").type(MediaType.APPLICATION_JSON).build();
			}

			// Eingabeüberprüfung
			if (reference.length() == 0 || !reference.matches("^[A-Za-z0-9 ]+$")){
				log.info("REST-API Call: 'localhost:9998/rest/transaction/" + " - 400 - BAD REQUEST - Daten mit falschem Format etc., sonstige Client-seitige Fehler");
				return Response.status(Response.Status.BAD_REQUEST).entity("reference empty or invalid").type(MediaType.APPLICATION_JSON).build();
			}

			AccountWrapper recAcc = new AccountWrapper(receiverNumber);

			if (recAcc.getId() == 0){
				log.info("REST-API Call: 'localhost:9998/rest/transaction/" + " - NOT FOUND - Account '" + receiverNumber + "' nicht gefunden");
				return Response.status(Response.Status.NOT_FOUND).entity("404 (NOT FOUND - Account nicht gefunden)").type(MediaType.APPLICATION_JSON).build();
			}

			recAcc = null;

			lock.lock();

			try {
				// Überprüfung des Guthabens
				AccountWrapper sendAcc = new AccountWrapper();

				if (senderNumber.equals("0000") || new Float(sendAcc.getActualValue(senderNumber)) - new Float(amount) >= 0){
					Transaction transaction = new Transaction();
					transaction.setSender(new AccountWrapper(senderNumber));
					transaction.setReceiver(new AccountWrapper(receiverNumber));
					transaction.setAmount(new BigDecimal(amount));
					transaction.setReference(reference);
					transaction.addToDB();
					transaction = null;
					log.info("Transaction executed: "+ senderNumber + " an " + receiverNumber + " - " +  amount + "€ with reference '" + reference + "'");
					return Response.ok().build();
				}
				else{
					log.info("Transaction not executed (not sufficient) "+ senderNumber + " an " + receiverNumber + " - " +  amount + "€ with reference '" + reference + "'");
					return Response.status(Response.Status.PRECONDITION_FAILED).entity("account value of '" + sendAcc.getActualValue(senderNumber) + "'€ not sufficient for transaction'").type(MediaType.APPLICATION_JSON).build();
				}
			} finally {
				lock.unlock();
			}

		} catch (Exception ex){
			log.error("Exception in @Path('transaction'): " + ex.getMessage());
			return Response.serverError().build();
		}
	}

	/**
	 *
	 * @param owner Account-Name
	 * @param amount Account-Startguthaben
	 * @return
	 */
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

	/**
	 * Funktion zum Bearbeiten von Accounts über Angular
	 * @param number Account-Nummer
	 * @param owner Account-Owner
	 * @return
	 */
	@POST
	@Path("/admin/editAccount")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response editAccount(@FormParam("post_number") String number, @FormParam("post_owner") String owner){
		try{
			AccountWrapper acc = new AccountWrapper();
			acc.updateAccount(number, owner);
		} catch (Exception ex){
			log.error("Exception in @Path('/admin/editAccount'): " + ex.getMessage());
			return Response.serverError().build();
		}

		return Response.ok().build();
	}
}

