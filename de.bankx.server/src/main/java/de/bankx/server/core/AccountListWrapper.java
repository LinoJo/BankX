package de.bankx.server.core;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class AccountListWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<AccountWrapper> items;

    public AccountListWrapper() {
        this.items = new ArrayList<AccountWrapper>();
    }

    public AccountListWrapper(List<AccountWrapper> items) {
        this.items = items;
    }

    public List<AccountWrapper> getItems() {
        return items;
    }

    public void setItems(List<AccountWrapper> items) {
        this.items = items;
    }
}
