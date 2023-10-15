package lk.ijse.mobileshop.model;
import lk.ijse.mobileshop.Util.Cruddutil;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.dto.Customer;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CustomerModel {
    private final static String URL = "jdbc:mysql://localhost:3306/mobileshop";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "12345");
    }

    public static boolean saveCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer(Cust_Id,Name,Address,Contact) VALUES(?, ?, ?,?)";

        return Cruddutil.execute(sql, customer.getId(), customer.getName(), customer.getAddress(),customer.getContact());
    }


    public static String generateNextCust_Id() throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();

        String sql = "SELECT Cust_Id FROM customer ORDER BY Cust_Id DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return splitCustomer_Id(resultSet.getString(1));
        }
        return splitCustomer_Id(null);
    }
    public static String splitCustomer_Id(String splitCustomer_Id) {
        if (splitCustomer_Id != null) {
            String[] strings = splitCustomer_Id.split("C0-0");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "C0-00" + id;
        }
        return "C0-001";
    }


    public static ResultSet getAll(){
        ResultSet resultSet = null;
        try {
            resultSet=Cruddutil.execute("SELECT * FROM customer");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public static Customer search(String Id) throws SQLException {
        ResultSet rs = Cruddutil.execute("SELECT * FROM customer WHERE Cust_Id = ?",Id);
        if(rs.next())return new Customer(rs.getString(1)
                ,rs.getString(2),rs.getString(3),rs.getString(4));
        return null;
    }

    public static boolean update(Customer correctData) throws SQLException {

        System.out.println(correctData);
        return Cruddutil.execute("update customer set Name = ? ,Address = ?,Contact=?  where Cust_Id = ?"
                ,correctData.getName(),correctData.getAddress(),correctData.getContact(),correctData.getId());
    }

    public static boolean delete(String id) throws SQLException {
        return Cruddutil.execute("delete from customer where Cust_Id = ?",id);
    }


    public static List<String> getId() throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();
        String sql = "SELECT * FROM customer";

        ResultSet rst = con.createStatement().executeQuery(sql);
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(rst.getString(1));
        }
        return ids;
    }
}
