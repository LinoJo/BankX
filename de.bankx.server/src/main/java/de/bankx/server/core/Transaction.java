package de.bankx.server.core;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Transaction {
	private int id;
	private AccountWrapper sender;
	private AccountWrapper receiver;
	private BigDecimal amount;
	private String reference;
	private Date transactionDate;

	@XmlTransient
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AccountWrapper getSender() {
		return sender;
	}

	public void setSender(AccountWrapper sender) {
		this.sender = sender;
	}

	public AccountWrapper getReceiver() {
		return receiver;
	}

	public void setReceiver(AccountWrapper receiver) {
		this.receiver = receiver;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

}
