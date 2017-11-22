package de.bankx.server.core;


import de.bankx.server.services.DatabaseService;
import org.apache.log4j.Logger;

import java.sql.*;

public class AccountWrapper {
    private int id;
    private String owner;
    private String number;

    static Logger log = Logger.getLogger(AccountWrapper.class);

    public AccountWrapper(){}

    public AccountWrapper(Account account)
    {
        this.id = account.getId();
        this.owner = account.getOwner();
        this.number = account.getNumber();
    }

    public AccountWrapper(String number){
        try {
            Connection con = DatabaseService.getInstance().getConnection();
            Statement sta = con.createStatement();
            ResultSet res = sta.executeQuery("SELECT * FROM Accounts WHERE number ='" + number + "' FETCH FIRST ROW ONLY");
            if (!res.next()) {
                // Kein Account gefunden!
                this.id = 0;
                log.info("Kein Account unter der Nummer '" + number + "' gefunden");
            } else {
                // ID, Number, Owner erhalten
                this.id = res.getInt("id");
                this.number = res.getString("number");
                this.owner = res.getString("owner").replaceAll("\\s+$", "");
                log.debug("Objekt erzeugt: AccountWrapper(number: " + number + ")");
                res.close();
            }
            sta.close();
            con.close();
        } catch(SQLException e) {
            log.error("SQLException Class:AccountWrapper Constructor(): " + e.getMessage());
        }
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addToDb(AccountWrapper acc){
        Connection con = null;
        PreparedStatement prep = null;
        String sql = "INSERT INTO Accounts"
                + "(owner, number) VALUES"
                + "(?,?)";

        try {
            con = DatabaseService.getInstance().getConnection();
            prep = con.prepareStatement(sql);

            prep.setString(1, acc.owner);
            prep.setString(2, acc.number);

            prep.executeUpdate();

            log.info("owner '" + acc.owner + "' mit number '" + acc.number + "' added");

            prep.close();
            con.close();
        } catch (SQLException e) {
            log.error("SQLException AccountWrapper.addToDb(): " + e.getMessage());
        }

    }
}
