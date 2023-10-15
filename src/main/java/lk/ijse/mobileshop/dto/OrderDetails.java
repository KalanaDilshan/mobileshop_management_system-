package lk.ijse.mobileshop.dto;

import java.util.Date;

public class OrderDetails {
    String item_Id;
    String order_Id;
    Date date;
    int qty;
    double price;
    String custId;

    public OrderDetails(String item_Id, String order_Id, Date date, int qty, double price, String custId) {
        this.item_Id = item_Id;
        this.order_Id = order_Id;
        this.date = date;
        this.qty = qty;
        this.price = price;
        this.custId = custId;
    }

    public OrderDetails() {
    }

    public String getItem_Id() {
        return item_Id;
    }

    public void setItem_Id(String item_Id) {
        this.item_Id = item_Id;
    }

    public String getOrder_Id() {
        return order_Id;
    }

    public void setOrder_Id(String order_Id) {
        this.order_Id = order_Id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }
}
