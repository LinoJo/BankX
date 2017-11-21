package de.bankx.server.test.tests;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.bankx.server.test.utils.Repositoy;
import de.fhdw.bank.server.test.model.Account;

public class GetAccountTest extends AbstractBankTest {
	private static Repositoy repo;

	// =====
	// Setup
	// =====

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setUpBeforeClasses();
		repo = new Repositoy();
		repo.setUrl(URL);
	}

	// =====
	// Tests
	// =====

	@Test
	public void successfulRequestTest() {
		repo.setAccountNumber(SENDER_NUMBER);
		Account account = repo.read();
		Assert.assertEquals(SENDER_NUMBER, account.getNumber());
		Assert.assertTrue("account.owner has unexpected value", account.getOwner() != null && !"".equals(account.getOwner()));
		Assert.assertNotNull("Transactions empty", account.getTransactions());
	}

	@Test(expected = BadRequestException.class)
	public void badRequestTest() {
		repo.setAccountNumber(SENDER_NUMBER_BAD_FORMAT);
		repo.read();
	}

	@Test(expected = NotFoundException.class)
	public void notFoundTest() {
		repo.setAccountNumber(SENDER_NUMBER_FALSE);
		repo.read();
	}
}
