package de.bankx.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	@Path("/account/{number}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response account(@PathParam("number") String number) {

		log.debug("REST-API: 'localhost:9998/rest/account/" + number + "'");

		Account bank = new Account();
		bank.setId(0);
		bank.setNumber("0000");
		bank.setOwner("Bank");

		Account acc = new Account();
		acc.setId(1);
		acc.setNumber(number);
		acc.setOwner("Peter Pan");

		List<Transaction> transactionList = new ArrayList<>();

		Transaction testTransaction = new Transaction();
		testTransaction.setId(0);
		testTransaction.setAmount(new BigDecimal(150));
		testTransaction.setReceiver(new AccountWrapper(acc));
		testTransaction.setSender(new AccountWrapper(bank));
		testTransaction.setTransactionDate(new Date());

		transactionList.add(testTransaction);

		acc.setTransactions(transactionList);

		//try {
		//	Connection con = DatabaseService.getInstance().getConnection();
		//	con.close();
		//} catch (SQLException e) {
		//	e.printStackTrace();
		//}


		return Response.ok(acc).build();
	}
}
