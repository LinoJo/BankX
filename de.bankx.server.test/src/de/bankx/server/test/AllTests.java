package de.bankx.server.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.bankx.server.test.tests.ConcurrentBankTest;
import de.bankx.server.test.tests.GetAccountTest;
import de.bankx.server.test.tests.TransferMoneyTest;

@RunWith(Suite.class)
@SuiteClasses({ GetAccountTest.class, TransferMoneyTest.class, ConcurrentBankTest.class })
public class AllTests {

}
