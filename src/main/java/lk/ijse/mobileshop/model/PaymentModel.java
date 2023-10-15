package lk.ijse.mobileshop.model;

import lk.ijse.mobileshop.Util.Cruddutil;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.dto.Customer;
import lk.ijse.mobileshop.dto.Payment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PaymentModel {
    private final static String URL = "jdbc:mysql://localhost:3306/mobileshop";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "12345");
    }

    public static boolean savePayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payment(Pay_id,Cust_id,orderId,Rep_id,Date,Total_Price) VALUES(?, ?, ?,?,?,?)";

        return Cruddutil.execute(sql, payment.getPay_id(), payment.getCust_id(), payment.getOder_id(), payment.getRep_id(), payment.getDate(), payment.getTotal_Price());
    }

    public static String generateNextPay_Id() throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();

        String sql = "SELECT Pay_id FROM Payment ORDER BY Pay_id DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return splitPay_id(resultSet.getString(1));
        }
        return splitPay_id(null);
    }

    public static String splitPay_id(String splitPay_id) {
        if (splitPay_id != null) {
            String[] strings = splitPay_id.split("Pa-0");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "Pa-00" + id;
        }
        return "Pa-001";
    }

    public static List<Payment> getAll() throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();
        String sql = "SELECT * FROM payment";
        List<Payment> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            data.add(new Payment(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDate(5),
                    resultSet.getDouble(6)

            ));
        }

        return data;
    }
    public static Payment search(String Id) throws SQLException {
        ResultSet rs = Cruddutil.execute("SELECT * FROM payment WHERE Pay_Id = ?",Id);
        if(rs.next())return new Payment(rs.getString(1)
                ,rs.getString(2),rs.getString(3),
                rs.getString(4),rs.getDate(5),rs.getDouble(6));
        return null;
    }
    public static boolean delete(String Id) throws SQLException {
        return Cruddutil.execute("delete from payment where Pay_Id = ?",Id);
    }
    public static boolean update(Payment correctData) throws SQLException {
        System.out.println(correctData);
        return Cruddutil.execute("update payment set Cust_Id = ? ,orderId = ?,Rep_Id = ? ,Date = ? ,Total_Price = ?  where Pay_Id = ?"
                ,correctData.getCust_id(),correctData.getOder_id(),correctData.getRep_id(),correctData.getDate(),correctData.getTotal_Price());
    }

}
