package lk.ijse.mobileshop.dto;

public class RepairItem {
    private  String Discription;
    private  String id;
    private  String Cust_id;
    private  String Item_id;
    private  String Contact;
    private  double Payment;

    public RepairItem(String id, String cust_id, String item_id,double payment, String contact, String discription) {
        this.id = id;
        Cust_id = cust_id;
        Item_id = item_id;
        Payment = payment;
        Contact = contact;
        Discription = discription;
    }

    public RepairItem() {
    }

    public  String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  String getCust_id() {
        return Cust_id;
    }

    public void setCust_id(String cust_id) {
        Cust_id = cust_id;
    }

    public  String getItem_id() {
        return Item_id;
    }

    public void setItem_id(String item_id) {
        Item_id = item_id;
    }

    public  String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public  double getPayment() {
        return Payment;
    }

    public void setPayment(double payment) {
        Payment = payment;
    }

    public  String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    @Override
    public String toString() {
        return "RepairItem{" +
                "Discription='" + Discription + '\'' +
                ", id='" + id + '\'' +
                ", Cust_id='" + Cust_id + '\'' +
                ", Item_id='" + Item_id + '\'' +
                ", Contact='" + Contact + '\'' +
                ", Payment=" + Payment +
                '}';
    }
}
