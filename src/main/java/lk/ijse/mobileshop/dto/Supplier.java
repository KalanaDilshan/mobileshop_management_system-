package lk.ijse.mobileshop.dto;

import java.util.Date;

public class Supplier {
    private  String id;
    private  java.util.Date Date;
    private  String Item;

    public Supplier(String id, Date date, String item) {
        this.id = id;
        this.Date = date;
        this.Item = item;
    }

    public Supplier() {
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
