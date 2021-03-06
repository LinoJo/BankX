package de.bankx.server.rest;
import de.bankx.server.core.*;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

//Wird benötigt, wenn Listen von Objekten als JSON zurückgegeben werden sollen

/**
 * JSON Content Resolver Klasse
 */
@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

	private static final Class<?>[] CLASSES = new Class[] { Account.class, Transaction.class, AccountWrapper.class, AccountListWrapper.class, TransactionListWrapper.class /*
																			 * Klassen
																			 * des
																			 * Datenmodells
																			 */};
	private final JAXBContext context;

	/**
	 * JAXB Kontext Auflöser
	 * @throws Exception
	 */
	public JAXBContextResolver() throws Exception {
		this.context = new JSONJAXBContext(JSONConfiguration.natural().humanReadableFormatting(true).build(), CLASSES);
	}

	@Override
	public JAXBContext getContext(Class<?> objectType) {
		return context;
	}

}
