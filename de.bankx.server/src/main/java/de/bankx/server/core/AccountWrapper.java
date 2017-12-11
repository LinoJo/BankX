package de.bankx.server.core;


import de.bankx.server.services.DatabaseService;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountWrapper {
    private int id;
    private String owner;
    private String number;

    static Logger log = Logger.getLogger(AccountWrapper.class);

    public AccountWrapper(){}

    public AccountWrapper(String number){
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
                log.debug("Objekt erzeugt: AccountWrapper(number: " + number + ")");
                res.close();
            }

            sta.close();
            con.close();
        } catch(SQLException e) {
            log.error("SQLException Constructor(String number): " + e.getMessage());
        }
    }

    public AccountWrapper(Account account)
    {
        this.id = account.getId();
        this.owner = account.getOwner();
        this.number = account.getNumber();
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

    @XmlTransient
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActualValue(String number){
        try{
            Float gesamount = new Float(0);
            Float curamount = new Float(0);

            Connection con = DatabaseService.getInstance().getConnection();
            Statement sta = con.createStatement();
            ResultSet res = sta.executeQuery("SELECT AMOUNT FROM TRANSACTIONS WHERE RECEIVER ='" + number + "'");
            while (res.next()){
                curamount = res.getFloat(1);
                gesamount = gesamount + curamount;
            }
            res.close();
            sta.close();

            sta = con.createStatement();
            res = sta.executeQuery("SELECT AMOUNT FROM TRANSACTIONS WHERE SENDER ='" + number + "'");
            while (res.next()){
                curamount = res.getFloat(1);
                gesamount = gesamount - curamount;
            }
            res.close();
            sta.close();
            con.close();
            return gesamount.toString();
        }
        catch(SQLException e) {
            log.error("SQLException getActualValue(String number): " + e.getMessage());
            return "0";
        }
    }

    public void addToDb(String startamount){
        Connection con = null;
        PreparedStatement prep = null;
        Transaction tra = new Transaction();

        String sql = "INSERT INTO Accounts"
                + "(owner, number) VALUES"
                + "(?,?)";

        try {
            this.number = this.getNextFreeAccountNumber();

            con = DatabaseService.getInstance().getConnection();
            prep = con.prepareStatement(sql);

            prep.setString(1, this.owner);
            prep.setString(2, this.number);

            prep.executeUpdate();

            log.info("owner '" + this.owner + "' with number '" + this.number + "' added to db");

            prep.close();
            con.close();

            tra.setReceiver(new AccountWrapper(this.number));
            tra.setSender(new AccountWrapper("0000"));
            tra.setAmount(new BigDecimal(startamount));
            tra.setReference("Darlehen");
            tra.addToDB();
            tra = null;
        } catch (SQLException e) {
            log.error("SQLException AccountWrapper.addToDb(): " + e.getMessage());
        }
    }

    private String getNextFreeAccountNumber(){
        try{
            String returnValue = "0000";
            Connection con = DatabaseService.getInstance().getConnection();
            Statement sta = con.createStatement();
            ResultSet res = sta.executeQuery("SELECT NUMBER FROM ACCOUNTS ORDER BY NUMBER DESC FETCH FIRST ROW ONLY");
            if (!res.next()){
                // Kein Account gefunden!
                this.id = 0;
                log.error("Keine Accounts in Datenbank!");
            }
            else{
                Integer maxnum = res.getInt( "number");
                maxnum += 1;
                returnValue = ("1000" + maxnum.toString()).substring(maxnum.toString().length());
                res.close();
            }
            sta.close();
            con.close();
            return returnValue;
        } catch (SQLException e){
            log.error("SQLException AccountWrapper.getNextFreeAccountNumber(): " + e.getMessage());
            return "0000";
        }
    }

    public List<AccountWrapper> getListOfAccounts(){

        List<AccountWrapper> accList = new ArrayList<AccountWrapper>();

        try{
            Connection con = DatabaseService.getInstance().getConnection();
            Statement sta = con.createStatement();
            ResultSet res = sta.executeQuery("SELECT NUMBER FROM ACCOUNTS");
            while(res.next()){
                AccountWrapper acc = new AccountWrapper( res.getString( "number"));
                accList.add(acc);
            }
            res.close();
            sta.close();
            con.close();

            log.debug("getListOfAccounts() executed");
            return accList;
        }catch(SQLException e) {
            log.error("SQLException getListOfAccounts(): " + e.getMessage());
            return accList;
        }
    }
}
