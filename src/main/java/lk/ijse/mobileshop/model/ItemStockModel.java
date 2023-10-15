package lk.ijse.mobileshop.model;

import lk.ijse.mobileshop.Util.Cruddutil;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.dto.Cart;
import lk.ijse.mobileshop.dto.Customer;
import lk.ijse.mobileshop.dto.ItemStock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ItemStockModel {
    private final static String URL = "jdbc:mysql://localhost:3306/mobileshop";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "12345");
    }

    public static boolean saveItemStock(ItemStock itemStock) throws SQLException {
        String sql = "INSERT INTO item_stock(Item_Id,Qty,Item_Name,unitPrice) VALUES(?,?, ?,?)";

        return Cruddutil.execute(sql,itemStock.getItem_Id(),itemStock.getQty(),itemStock.getItem_Name(),itemStock.getUnitPrice());
    }

    public static String generateNextItemId() throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();

        String sql = "SELECT Item_Id FROM item_stock ORDER BY Item_Id DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return splitItem_Id(resultSet.getString(1));
        }
        return splitItem_Id(null);
    }



    public static String splitItem_Id(String splitItem_Id) {
        if (splitItem_Id != null) {
            String[] strings = splitItem_Id.split("It-0");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "It-00" + id;
        }
        return "It-001";
    }

    public static List<ItemStock> getAll() throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();
        String sql = "SELECT * FROM item_stock";
        List<ItemStock> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            data.add(new ItemStock(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
            ));
        }
        return data;
    }
    public static ItemStock search(String Id) throws SQLException {
        ResultSet rs = Cruddutil.execute("SELECT * FROM item_stock WHERE Item_Id = ?",Id);
        if(rs.next())return new ItemStock(rs.getString(1)
                ,rs.getInt(2),rs.getString(3),rs.getDouble(4));
        return null;
    }
    public static boolean delete(String id) throws SQLException {
        return Cruddutil.execute("delete from item_stock where Item_Id = ?",id);
    }
    public static boolean update(ItemStock correctData)throws SQLException {
        System.out.println(correctData);
        return Cruddutil.execute("update item_stock set Qty = ? ,Item_Name = ?,unitPrice = ?  where Item_Id = ?"
                ,correctData.getQty(),correctData.getItem_Name(),correctData.getUnitPrice(),correctData.getItem_Id());
    }
    public static boolean updateStock(List<Cart> cartList) throws SQLException {
        for (Cart cart : cartList) {
            if(!updateQty(cart)) {
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(Cart cart) throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();
        String sql = "UPDATE item_stock SET Qty = (Qty - ?) WHERE Item_id = ?";

        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setInt(1, cart.getQty());
        pstm.setString(2, cart.getItemId());

        return pstm.executeUpdate() > 0;
    }
}
