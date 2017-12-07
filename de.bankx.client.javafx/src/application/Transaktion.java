package application;

import javafx.beans.property.SimpleStringProperty;

public class Transaktion {
	private final SimpleStringProperty sender = new SimpleStringProperty("");
	private final SimpleStringProperty empfaenger = new SimpleStringProperty("");
	private final SimpleStringProperty betrag = new SimpleStringProperty("");

	public Transaktion() {
		this("","","");
	}

	public Transaktion(String sender, String empfaenger, String betrag) {
		setSender(sender);
		setEmpaenger(empfaenger);
		setBetrag(betrag);
	}

	public void setBetrag(String betrag2) {
		betrag.set(betrag2);
	}

	public void setEmpaenger(String empfaenger2) {
		empfaenger.set(empfaenger2);
	}

	public void setSender(String sender2) {
		sender.set(sender2);
	}
	
	public String getBetrag() {
		return betrag.get();
	}

	public String getEmpaenger() {
		return empfaenger.get();
	}

	public String getSender() {
		return sender.get();
	}
}
