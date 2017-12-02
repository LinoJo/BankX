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

	@POST
	@Path("/transaction")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response execTransact(@FormParam("senderNumber") String senderNumber, @FormParam("receiverNumber") String receiverNumber, @FormParam("amount") String amount, @FormParam("reference") String reference){
		try{
			log.info(senderNumber + ", " + receiverNumber + ", " +  amount + ", " + reference);
		} catch (Exception ex){
			log.error("Exception in @Path('transaction'): " + ex.getMessage());
			return Response.serverError().build();
		}

		return Response.ok().build();
	}

	@POST
	@Path("/admin/addAccount")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addAccount(@FormParam("post_owner") String owner, @FormParam("post_number") String number, @FormParam("post_amount") String amount){
		try{
			log.info(owner + number + amount);
		} catch (Exception ex){
			log.error("Exception in @Path('/admin/addAccount'): " + ex.getMessage());
			return Response.serverError().build();
		}

		return Response.ok().build();
	}
}

