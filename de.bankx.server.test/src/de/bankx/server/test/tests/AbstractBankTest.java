package de.bankx.server.test.tests;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public abstract class AbstractBankTest {

	protected static String URL;
	protected static String SENDER_NUMBER;
	protected static String RECEIVER_NUMBER;
	protected static String SENDER_NUMBER_FALSE;
	protected static String RECEIVER_NUMBER_FALSE;
	protected final static String SENDER_NUMBER_BAD_FORMAT = "$$$&";
	protected final static String RECEIVER_NUMBER_BAD_FORMAT = "asdf";
	protected final static String REFERENCE = "Test mit Toast42 23";
	protected final static String REFERENCE_BAD_FORMAT = "reference!";
	protected final static String AMOUNT = "10";
	protected final static String AMOUNT_SECOND = "5";
	protected final static String AMOUNT_WITH_DECIMAL = "0.01";
	protected final static String AMOUNT_BAD_FORMAT = "overninethousand";

	protected static void setUpBeforeClasses() {
		setUpFromProperties();
	}

	private static void setUpFromProperties() {
		Reader reader = null;
		try {
			reader = new FileReader("TestSettings.properties");
			Properties properties = new Properties();
			properties.load(reader);
			URL = properties.getProperty("url");
			SENDER_NUMBER = properties.getProperty("senderNumber");
			RECEIVER_NUMBER = properties.getProperty("receiverNumber");
			SENDER_NUMBER_FALSE = properties.getProperty("senderNumberFalse");
			RECEIVER_NUMBER_FALSE = properties.getProperty("receiverNumberFalse");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
