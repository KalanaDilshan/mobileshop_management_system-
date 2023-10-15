package lk.ijse.mobileshop.tm;

public class ItemStockTm {
    private String Item_Id;
    private  int Qty;
    private  String Item_Name;
    private  double unitPrice;

    public ItemStockTm() {
    }

    public ItemStockTm(String item_Id, int qty, String item_Name,double unitprice) {
        Item_Id = item_Id;
        Qty = qty;
        Item_Name = item_Name;
        this.unitPrice = unitPrice;
    }

    public String getItem_Id() {
        return Item_Id;
    }

    public void setItem_Id(String item_Id) {
        Item_Id = item_Id;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name; }
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
