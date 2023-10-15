package lk.ijse.mobileshop.tm;

import java.util.Date;

public class SupplierTm {
    private  String id;
    private  java.util.Date Date;
    private  String Item;

    public SupplierTm(String id, java.util.Date date, String item) {
        this.id = id;
        this.Date = date;
        this.Item = item;
    }

    public SupplierTm() {
    }

    public  String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public  String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }
}
