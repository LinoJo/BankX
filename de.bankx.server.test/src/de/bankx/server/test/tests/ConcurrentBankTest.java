package de.bankx.server.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mycila.sandbox.junit.runner.Concurrent;
import com.mycila.sandbox.junit.runner.ConcurrentJunitRunner;

import de.bankx.server.test.utils.Repositoy;

@RunWith(ConcurrentJunitRunner.class)
@Concurrent(threads = 4)
public class ConcurrentBankTest extends AbstractBankTest {
	private static Repositoy senderRepository;
	private static Repositoy receiverRepository;

	private static final int NUMBER_OF_TRANSACTIONS = 30;

	private static BigDecimal initialBalance;
	private static BigDecimal initialSenderBalance;
	private static BigDecimal initialReceiverBalance;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setUpBeforeClasses();

		senderRepository = new Repositoy();
		senderRepository.setUrl(URL);
		senderRepository.setAccountNumber(SENDER_NUMBER);

		receiverRepository = new Repositoy();
		receiverRepository.setUrl(URL);
		receiverRepository.setAccountNumber(RECEIVER_NUMBER);

		initialSenderBalance = senderRepository.read().calcBalance();
		initialReceiverBalance = receiverRepository.read().calcBalance();

		initialBalance = initialSenderBalance.add(initialReceiverBalance);

		System.out.println("Sender balance: " + initialSenderBalance);
		System.out.println("Receiver balance: " + initialReceiverBalance);

		System.out.println("Initial balance: " + initialBalance);
	}

	@AfterClass
	public static void checkFinalBalance() throws Exception {
		BigDecimal senderBalance = senderRepository.read().calcBalance();
		BigDecimal receiverBalance = receiverRepository.read().calcBalance();

		BigDecimal finalBalance = senderBalance.add(receiverBalance);

		System.out.println("Sender balance: " + senderBalance);
		System.out.println("Receiver balance: " + receiverBalance);

		System.out.println("Final balance: " + finalBalance);

		assertEquals(initialBalance, finalBalance);
		assertTrue(finalBalance.compareTo(BigDecimal.ZERO) >= 0);
		assertTrue(senderBalance.compareTo(BigDecimal.ZERO) >= 0);
		assertTrue(receiverBalance.compareTo(BigDecimal.ZERO) >= 0);
	}

	@Test
	public void client1() throws Throwable {
		doTransactions();
	}

	@Test
	public void client2() throws Throwable {
		doTransactions();
	}

	@Test
	public void client3() throws Throwable {
		doTransactions();
	}

	@Test
	public void client4() throws Throwable {
		doTransactions();
	}

	@Test
	public void client5() throws Throwable {
		doTransactions();
	}

	@Test
	public void client6() throws Throwable {
		doTransactions();
	}

	@Test
	public void client7() throws Throwable {
		doTransactions();
	}

	@Test
	public void client8() throws Throwable {
		doTransactions();
	}

	private void doTransactions() {
		for (int i = 0; i < NUMBER_OF_TRANSACTIONS; i++) {
			BigDecimal amount = initialSenderBalance.divide(new BigDecimal(100)).setScale(2, RoundingMode.CEILING);
			int response = senderRepository.transferMoney(RECEIVER_NUMBER, amount.toString(), REFERENCE);

			assertTrue("ResponseCode 200 or 412 expexted but got " + response, response == 200 || response == 412);
		}
	}

}
