package de.bankx.server.test.tests;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.bankx.server.test.utils.Repositoy;
import de.fhdw.bank.server.test.model.Account;
import de.fhdw.bank.server.test.model.Transaction;

public class TransferMoneyTest extends AbstractBankTest {
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
	public void successfulTransactionTest() {
		int initTransactionSize;
		BigDecimal initBalance;
		Account account;
		Transaction transaction;
		repo.setAccountNumber(SENDER_NUMBER);

		account = repo.read();
		initTransactionSize = account.getTransactions().size();
		initBalance = account.calcBalance();
		Assert.assertEquals(200, repo.transferMoney(RECEIVER_NUMBER, AMOUNT, REFERENCE));

		account = repo.read();
		transaction = account.getTransactions().get(0);

		Assert.assertEquals(initTransactionSize + 1, account.getTransactions().size());
		Assert.assertTrue("Unexpected Balance", new BigDecimal(AMOUNT).compareTo(transaction.getAmount()) == 0);
		Assert.assertEquals(SENDER_NUMBER, transaction.getSender().getNumber());
		Assert.assertEquals(RECEIVER_NUMBER, transaction.getReceiver().getNumber());
		Assert.assertEquals(REFERENCE, transaction.getReference());
		Assert.assertEquals(initBalance.subtract(new BigDecimal(AMOUNT)), account.calcBalance());
	}
	
	@Test
    public void transactionOrderTest() {
        Account account;
        Transaction transaction1;
        Transaction transaction2;
        repo.setAccountNumber(SENDER_NUMBER);

        Assert.assertEquals(200, repo.transferMoney(RECEIVER_NUMBER, AMOUNT, REFERENCE));
        Assert.assertEquals(200, repo.transferMoney(RECEIVER_NUMBER, AMOUNT_SECOND, REFERENCE));

        account = repo.read();
        transaction1 = account.getTransactions().get(1);
        transaction2 = account.getTransactions().get(0);

        Assert.assertTrue("Unexpected Balance", new BigDecimal(AMOUNT).compareTo(transaction1.getAmount()) == 0);
        Assert.assertTrue("Unexpected Balance", new BigDecimal(AMOUNT_SECOND).compareTo(transaction2.getAmount()) == 0);
    }

	@Test
	public void insufficientBalanceTest() {
		int initTransactionSize;
		Account account;
		BigDecimal initBalance;
		repo.setAccountNumber(SENDER_NUMBER);

		account = repo.read();
		initTransactionSize = account.getTransactions().size();
		initBalance = account.calcBalance();
		// Test
		Assert.assertEquals(412, repo.transferMoney(RECEIVER_NUMBER, initBalance.add(BigDecimal.ONE).toPlainString(), REFERENCE));

		account = repo.read();
		Assert.assertEquals(initTransactionSize, account.getTransactions().size());
		Assert.assertEquals(initBalance, account.calcBalance());
	}

	@Test
	public void accountNotFoundTest() {
		int initTransactionSize;
		Account account;
		BigDecimal initBalance;
		repo.setAccountNumber(SENDER_NUMBER);

		account = repo.read();
		initTransactionSize = account.getTransactions().size();
		initBalance = account.calcBalance();

		// Sender
		repo.setAccountNumber(SENDER_NUMBER_FALSE);
		Assert.assertEquals(404, repo.transferMoney(RECEIVER_NUMBER, AMOUNT, REFERENCE));

		// Receiver
		repo.setAccountNumber(SENDER_NUMBER);
		Assert.assertEquals(404, repo.transferMoney(RECEIVER_NUMBER_FALSE, AMOUNT, REFERENCE));
		account = repo.read();
		Assert.assertEquals(initTransactionSize, account.getTransactions().size());
		Assert.assertEquals(initBalance, account.calcBalance());

		// Sender and Receiver
		repo.setAccountNumber(SENDER_NUMBER_FALSE);
		Assert.assertEquals(404, repo.transferMoney(RECEIVER_NUMBER_FALSE, AMOUNT, REFERENCE));
	}

	@Test
	public void badRequestTest() {
		int initTransactionSize;
		Account account;
		BigDecimal initBalance;
		repo.setAccountNumber(SENDER_NUMBER);

		account = repo.read();
		initTransactionSize = account.getTransactions().size();
		initBalance = account.calcBalance();

		// Sender
		repo.setAccountNumber(SENDER_NUMBER_BAD_FORMAT);
		Assert.assertEquals(400, repo.transferMoney(RECEIVER_NUMBER, AMOUNT, REFERENCE));

		// Receiver
		repo.setAccountNumber(SENDER_NUMBER);
		Assert.assertEquals(400, repo.transferMoney(RECEIVER_NUMBER_BAD_FORMAT, AMOUNT, REFERENCE));
		account = repo.read();
		Assert.assertEquals(initTransactionSize, account.getTransactions().size());
		Assert.assertEquals(initBalance, account.calcBalance());

		// Amount
		Assert.assertEquals(400, repo.transferMoney(RECEIVER_NUMBER, AMOUNT_BAD_FORMAT, REFERENCE));
		account = repo.read();
		Assert.assertEquals(initTransactionSize, account.getTransactions().size());
		Assert.assertEquals(initBalance, account.calcBalance());

		// Reference
		Assert.assertEquals(400, repo.transferMoney(RECEIVER_NUMBER, AMOUNT, REFERENCE_BAD_FORMAT));
		account = repo.read();
		Assert.assertEquals(initTransactionSize, account.getTransactions().size());
		Assert.assertEquals(initBalance, account.calcBalance());
	}

	@Test()
	public void transferFractionAmountTest() {
		repo.setAccountNumber(SENDER_NUMBER);
		BigDecimal initialBalance = repo.read().calcBalance();

		int response = repo.transferMoney(RECEIVER_NUMBER, AMOUNT_WITH_DECIMAL, REFERENCE);
		BigDecimal newBalance = repo.read().calcBalance();

		Assert.assertEquals(200, response);
		Assert.assertEquals(initialBalance.subtract(new BigDecimal(AMOUNT_WITH_DECIMAL)), newBalance);
	}

	@Test()
	public void transferToSameAccountTest() {
		repo.setAccountNumber(SENDER_NUMBER);
		BigDecimal initialBalance = repo.read().calcBalance();

		int response = repo.transferMoney(SENDER_NUMBER, initialBalance.toString(), REFERENCE);
		BigDecimal newBalance = repo.read().calcBalance();

		Assert.assertTrue("Expected Statuscode 400", response == 400);
		Assert.assertEquals(initialBalance, newBalance);
	}

	@Test()
	public void transferFullAmountTest() {
		repo.setAccountNumber(SENDER_NUMBER);
		BigDecimal initialBalance = repo.read().calcBalance();

		int response = repo.transferMoney(RECEIVER_NUMBER, initialBalance.toString(), REFERENCE);
		BigDecimal newBalance = repo.read().calcBalance();

		Assert.assertTrue(response == 200);
		Assert.assertTrue("Unexpected Balance", BigDecimal.ZERO.compareTo(newBalance) == 0);

		repo.setAccountNumber(RECEIVER_NUMBER);
		response = repo.transferMoney(SENDER_NUMBER, initialBalance.toString(), REFERENCE);
	}
}
