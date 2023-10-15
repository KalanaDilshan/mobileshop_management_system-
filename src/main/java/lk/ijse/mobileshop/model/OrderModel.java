package lk.ijse.mobileshop.model;

import lk.ijse.mobileshop.Util.Cruddutil;
import lk.ijse.mobileshop.dto.Cart;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.dto.ItemStock;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderModel {
    public static boolean placeOrder(List<Cart>cartList, String orderId,String itemName, double unitPrice,String custId, Date date,double total) throws SQLException {
        Connection connection = null;

        try {
            connection = dbconnection.getInstance().getConnection();

            connection.setAutoCommit(false);

            boolean isSaved = OrderModel.saveOrder(orderId);
            if (isSaved) {
                boolean isSavedDetail = OrderDetailModel.saveDetail(cartList,orderId,date,total,custId);
                if (isSavedDetail) {
                    boolean isUpdated = ItemStockModel.updateStock(cartList);
                    if(isUpdated) {
                        connection.commit();
                        return true;
                    }
                }
            }
            return false;
        }catch (SQLException err){
            err.printStackTrace();
            return false;
        }finally {
            connection.rollback();
            connection.setAutoCommit(true);
        }
    }

    private static boolean saveOrder(String orderId) throws SQLException {
        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO order_stock(orderId,date) VALUES(?, ?)";

        return Cruddutil.execute(sql,orderId,sqlDate);
    }

    public static String generateNextId() throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();

        String sql = "SELECT orderId FROM order_stock ORDER BY orderId DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return splitEmp_Id(resultSet.getString(1));
        }
        return splitEmp_Id(null);
    }

    public static String splitEmp_Id(String splitEmp_Id) {
        if (splitEmp_Id != null) {
            String[] strings = splitEmp_Id.split("Or-0");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "Or-00" + id;
        }
        return "Or-001";
    }
}
