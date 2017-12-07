package de.bankx.server.core;

import de.bankx.server.services.DatabaseService;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Account {
	private int id;
	private String owner;
	private String number;
	private List<Transaction> transactions;

	static Logger log = Logger.getLogger(Account.class);

	public Account(){}
	public Account(String number){
		try{
			Connection con = DatabaseService.getInstance().getConnection();
			Statement sta = con.createStatement();
			ResultSet res = sta.executeQuery("SELECT * FROM Accounts WHERE number ='" + number + "' FETCH FIRST ROW ONLY");
			if (!res.next()){
				// Kein Account gefunden!
				this.id = 0;
				log.info("Kein Account unter der Nummer '"+ number +"' gefunden");
			}
			else{
				// ID, Number, Owner erhalten
				this.id = res.getInt("id");
				this.number = res.getString( "number");
				this.owner = res.getString("owner").replaceAll("\\s+$", "");
				log.debug("Objekt erzeugt: Account(number: " + number + ")");
				res.close();
			}
			sta.close();

			if (this.id != 0){
				// Transaktionen zum Objekt hinzufügen
				sta = con.createStatement();
				res = sta.executeQuery("SELECT id FROM Transactions WHERE SENDER ='" + number + "'");
				List<Integer> transactionIDs = new ArrayList<>();

				// Für alle sender
				while (res.next()){
					Integer currid = res.getInt("id");
					if(!transactionIDs.contains(currid)){
						transactionIDs.add(currid);
					}
				}
				sta.close();
				res.close();

				sta = con.createStatement();
				res = sta.executeQuery("SELECT id FROM Transactions WHERE RECEIVER ='" + number + "'");

				// Für alle empfänger
				while (res.next()){
					Integer currid = res.getInt("id");
					if(!transactionIDs.contains(currid)){
						transactionIDs.add(currid);
					}
				}
				sta.close();
				res.close();
				con.close();

				// Transaktionen sortieren und Objekt Account als Attribut hinzufügen
				Collections.sort(transactionIDs, Collections.reverseOrder());
				List<Transaction> transactionList = new ArrayList<>();
				for (Integer i : transactionIDs){
					Transaction ta = new Transaction(i);
					transactionList.add(ta);
				}
				this.transactions = transactionList;
			}

			sta.close();
			con.close();
		} catch(SQLException e) {
			log.error("SQLException Class:Account Constructor(): " + e.getMessage());
		}

	}

	@XmlTransient
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Account getById(Integer id){
		Account acc = new Account();
		return acc;
	}
}
