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

import java.sql.Connection;
import java.sql.SQLException;

@Path("/")
@Singleton
public class RestResource {

	@GET
	@Path("/account/{number}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response account(@PathParam("number") String number) {
		Account acc = new Account();
		acc.setId(0);
		acc.setNumber(number);
		acc.setOwner("Peter Pan");

		try {

			Connection con = DatabaseService.getInstance().getConnection();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Response.ok(acc).build();
	}
}
