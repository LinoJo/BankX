package de.bankx.server.core;


public class AccountWrapper {
    private int id;
    private String owner;
    private String number;

    public AccountWrapper(){};

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
