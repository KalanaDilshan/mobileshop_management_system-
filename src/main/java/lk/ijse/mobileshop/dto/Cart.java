package lk.ijse.mobileshop.dto;

import javafx.scene.control.Button;

public class Cart {
    private String itemId;
    private int qty;
    private double total;
    private Button btn;

    public Cart() {
    }

    public Cart(String itemId, int qty, double total, Button btn) {
        this.itemId = itemId;
        this.qty = qty;
        this.total = total;
        this.btn = btn;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
