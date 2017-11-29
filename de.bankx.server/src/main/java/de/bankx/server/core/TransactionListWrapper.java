package de.bankx.server.core;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class TransactionListWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Transaction> items;

    public TransactionListWrapper() {
        this.items = new ArrayList<Transaction>();
    }

    public TransactionListWrapper(List<Transaction> items) {
        this.items = items;
    }

    public List<Transaction> getItems() {
        return items;
    }

    public void setItems(List<Transaction> items) {
        this.items = items;
    }
}
