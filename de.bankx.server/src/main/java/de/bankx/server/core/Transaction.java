package de.bankx.server.core;

import de.bankx.server.services.DatabaseService;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Transaktionsklasse
 * @author Timon Caspari
 */
@XmlRootElement
public class Transaction {
	private int id;
	private AccountWrapper sender;
	private AccountWrapper receiver;
	private BigDecimal amount;
	private String reference;
	private Date transactionDate;

	static Logger log = Logger.getLogger(Transaction.class);

	/**
	 * Leerer Konstruktor Transaction
	 */
	public Transaction(){}
	public Transaction(Integer id){
		try{
			Connection con = DatabaseService.getInstance().getConnection();
			Statement sta = con.createStatement();
			ResultSet res = sta.executeQuery("SELECT * FROM Transactions WHERE id = " + id + " FETCH FIRST ROW ONLY");
			while (res.next()){
				this.id = res.getInt(1);
				this.sender = new AccountWrapper(res.getString(2));
				this.receiver = new AccountWrapper(res.getString(3));
				this.amount = res.getBigDecimal(4);
				this.reference = res.getString(5).replaceAll("\\s+$", "");
				this.transactionDate = res.getTimestamp(6);
				if (this.id == 0){
					// Keine Transaktion unter der ID gefunden!
					log.info("Keine Transaktion unter der ID '"+ id +"' gefunden");
				}
				log.debug("Objekt erzeugt: Transaktion(id: " + id + ")");
			}
			res.close();
			sta.close();
			con.close();
		}catch(SQLException e) {
			log.error("SQLException Class:Transaction Constructor(): " + e.getMessage());
		}
	}

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

	/**
	 * Zur Datenbank hinzufügen
	 */
	public void addToDB(){
		Connection con = null;
		PreparedStatement prep = null;
		String sql = "INSERT INTO Transactions(sender , receiver, amount, reference) values(?,?,?,?)";

		try {
			con = DatabaseService.getInstance().getConnection();
			prep = con.prepareStatement(sql);

			prep.setString(1, this.sender.getNumber());
			prep.setString(2, this.receiver.getNumber());
			prep.setBigDecimal(3, this.amount);
			prep.setString(4, this.reference);

			prep.executeUpdate();

			log.info("transaction '" + this.sender.getNumber() + "' to '" + this.receiver.getNumber() + "' with amount '" + this.amount + "' added to db");

			prep.close();
			con.close();
		} catch (SQLException e) {
			log.error("SQLException Transaction.addToDb(): " + e.getMessage());
		}
	}


	/**
	 * Liste von Transaktionen abrufen
	 * @return Liste von Transaktionen
	 */
	public List<Transaction> getListOfTransactions(){

		List<Transaction> accList = new ArrayList<Transaction>();

		try{
			Connection con = DatabaseService.getInstance().getConnection();
			Statement sta = con.createStatement();
			ResultSet res = sta.executeQuery("SELECT ID FROM TRANSACTIONS ORDER BY ID DESC");
			while(res.next()){
				Transaction acc = new Transaction( res.getInt( 1));
				accList.add(acc);
			}
			res.close();
			sta.close();
			con.close();

			log.debug("getListOfTransactions() executed");
			return accList;
		}catch(SQLException e) {
			log.error("SQLException getListOfTransactions(): " + e.getMessage());
			return accList;
		}
	}

}
