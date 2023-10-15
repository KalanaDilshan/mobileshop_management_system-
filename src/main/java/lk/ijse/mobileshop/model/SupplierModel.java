package lk.ijse.mobileshop.model;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lk.ijse.mobileshop.Util.Cruddutil;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.dto.ItemStock;
import lk.ijse.mobileshop.dto.Supplier;

public class SupplierModel {
    private final static String URL = "jdbc:mysql://localhost:3306/mobileshop";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "12345");
    }
    public static boolean saveSupplier(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO supplier(Suplier_Id,date,item) VALUES(?, ?, ?)";

        return Cruddutil.execute(sql, supplier.getId(), supplier.getDate(),supplier.getItem());
    }

    public static String generateNextSup_Id() throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();

        String sql = "SELECT Suplier_Id FROM Supplier ORDER BY Suplier_Id DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if(resultSet.next()) {
            return splitSupplier_Id(resultSet.getString(1));
        }
        return splitSupplier_Id(null);
    }

    public static String splitSupplier_Id(String splitSupplier_Id) {
        if(splitSupplier_Id != null) {
            String[] strings = splitSupplier_Id.split("Su-0");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "Su-00"+id;
        }
        return "Su-001";
    }
    public static List<Supplier>  getAll() throws SQLException {
            Connection con = dbconnection.getInstance().getConnection();
            String sql = "SELECT * FROM supplier";
            List<Supplier> data = new ArrayList<>();

            ResultSet resultSet = con.createStatement().executeQuery(sql);
            while (resultSet.next()){
                data.add(new Supplier(
                        resultSet.getString(1),
                        resultSet.getDate(2),
                        resultSet.getString(3)
                ));
            }
            return data;
    }

    public static Supplier search(String Id) throws SQLException {
        ResultSet rs = Cruddutil.execute("SELECT * FROM supplier WHERE Suplier_Id = ?",Id);
        if(rs.next())return new Supplier(rs.getString(1)
                ,rs.getDate(2),rs.getString(3));
        return null;
    }
    public static boolean delete(String id) throws SQLException {
        return Cruddutil.execute("delete from supplier where Suplier_Id = ?",id);
    }
    public static boolean update(Supplier correctData) throws SQLException {
        System.out.println(correctData);
        return Cruddutil.execute("update supplier set Date = ? ,Item = ?  where Suplier_Id = ?"
                ,correctData.getDate(),correctData.getItem(),correctData.getId());
    }
}
