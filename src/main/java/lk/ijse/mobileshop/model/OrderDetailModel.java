package lk.ijse.mobileshop.model;

import lk.ijse.mobileshop.Util.Cruddutil;
import lk.ijse.mobileshop.dto.Cart;

import java.sql.*;
import java.util.List;
import lk.ijse.mobileshop.db.dbconnection;

public class OrderDetailModel {
    public static boolean saveDetail(List<Cart> cartList, String orderId, Date date, double total, String custId) throws SQLException {

        for(Cart cart :  cartList) {
            if(!save(cart,orderId,date,total,custId)) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(Cart cart, String orderId, Date date, double total, String custId) throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();

        String sql = "INSERT INTO oder_detail(Item_Id,Oder_Id,Date,Qty,Price,custId) VALUES(?, ?, ?, ?, ?, ?)";

        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, cart.getItemId());
        pstm.setString(2,orderId);
        pstm.setDate(3,date);
        pstm.setInt(4, cart.getQty());
        pstm.setDouble(5,total);
        pstm.setString(6,custId);

        return pstm.executeUpdate() > 0;
    }
    public static ResultSet getAll(){
        ResultSet resultSet = null;
        try {
            resultSet= Cruddutil.execute("SELECT * FROM oder_detail");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
}
