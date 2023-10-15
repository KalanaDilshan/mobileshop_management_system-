package lk.ijse.mobileshop.dto;

import lk.ijse.mobileshop.Util.Cruddutil;
import lk.ijse.mobileshop.db.dbconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemStock {
    private String Item_Id;
    private  int Qty;
    private  String Item_Name;
    private  double unitPrice;

    public ItemStock() {
    }

    public ItemStock(String item_Id, int qty, String item_Name, double unitPrice) {
        Item_Id = item_Id;
        Qty = qty;
        Item_Name = item_Name;
        this.unitPrice = unitPrice;
    }



    public static List<String> getId() throws SQLException {
            Connection con = dbconnection.getInstance().getConnection();
            String sql = "SELECT * FROM item_stock";

            ResultSet rst = con.createStatement().executeQuery(sql);
            List<String> ids = new ArrayList<>();
            while (rst.next()) {
                ids.add(rst.getString(1));
            }
            return ids;
        }

    public static ItemStock searchById(String itemId) throws SQLException {
            ResultSet rs = Cruddutil.execute("SELECT * FROM item_stock WHERE item_Id = ?",itemId);
            if(rs.next())return new ItemStock(rs.getString(1)
                    ,rs.getInt(2),rs.getString(3),rs.getDouble(4));
            return null;
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
        Item_Name = item_Name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
