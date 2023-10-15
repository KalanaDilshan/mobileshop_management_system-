package lk.ijse.mobileshop.tm;

import java.util.Date;

public class PaymentTm {
    private  String Pay_id;
    private  String Cust_id;
    private  String Oder_id;
    private  String Rep_id;
    private  Date Date;
    private  double Total_Price;


    public PaymentTm(String pay_id, String cust_id, String oder_id, String rep_id, Date date, double total_Price) {
        Pay_id = pay_id;
        Cust_id = cust_id;
        Oder_id = oder_id;
        Rep_id = rep_id;
        Date = date;
        Total_Price = total_Price;
    }

    public PaymentTm() {
    }

    public  String getPay_id() {
        return Pay_id;
    }

    public void setPay_id(String pay_id) {
        Pay_id = pay_id;
    }

    public  String getCust_id() {
        return Cust_id;
    }

    public void setCust_id(String cust_id) {
        Cust_id = cust_id;
    }

    public  String getOder_id() {
        return Oder_id;
    }

    public void setOder_id(String oder_id) {
        Oder_id = oder_id;
    }

    public  String getRep_id() {
        return Rep_id;
    }

    public void setRep_id(String rep_id) {
        Rep_id = rep_id;
    }

    public  Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public  double getTotal_Price() {
        return Total_Price;
    }

    public void setTotal_Price(double total_Price) {
        Total_Price = total_Price;
    }
}

