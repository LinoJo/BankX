package de.bankx.server.core;

import javax.xml.bind.annotation.XmlTransient;

public class TransactionLock {
    private Boolean locked;

    public TransactionLock(){}

    public TransactionLock(Boolean lock){
        if (lock == true){
            this.locked = true;
        }
        else{
            this.locked = false;
        }
    }

    @XmlTransient

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
}
