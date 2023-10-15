package lk.ijse.mobileshop.model;

import lk.ijse.mobileshop.Util.Cruddutil;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.dto.RepairItem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class RepairItemModel {
    private final static String URL = "jdbc:mysql://localhost:3306/mobileshop";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "12345");
    }

    public static boolean saveRepairItem(RepairItem repairItem) throws SQLException {
        String sql = "INSERT INTO repair_item(Rep_Id,Cust_Id,Item_Id,Price,Contact,Description) VALUES(?, ?, ?,?,?,?)";
        System.out.println(repairItem);
        return Cruddutil.execute(sql, repairItem.getId(), repairItem.getCust_id(), repairItem.getItem_id(), repairItem.getPayment(), repairItem.getContact(), repairItem.getDiscription());
    }

    public static   String generateNextRepairItem_Id() throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();

        String sql = "SELECT Rep_Id FROM repair_item ORDER BY Rep_Id DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return splitRepairItem_id(resultSet.getString(1));
        }
        return splitRepairItem_id(null);
    }

    public static String splitRepairItem_id(String splitRepairItem_Id) {
        if (splitRepairItem_Id != null) {
            String[] strings = splitRepairItem_Id.split("Re-0");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "Re-00" + id;
        }
        return "Re-001";
    }

    public static ResultSet getAll(){
        ResultSet resultSet = null;
        try {
            resultSet=Cruddutil.execute("SELECT * FROM repair_item");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
    public static RepairItem search(String id) throws SQLException {
        ResultSet rs = Cruddutil.execute("SELECT * FROM repair_item WHERE Rep_Id = ?",id);
        if(rs.next())return new RepairItem(rs.getString(1)
                ,rs.getString(2),rs.getString(3),rs.getDouble(4),
                 rs.getString(5),rs.getString(6));
        return null;
    }
    public static boolean delete(String id) throws SQLException {
        return Cruddutil.execute("delete from repair_item where Rep_Id = ?",id);
    }
    public static boolean update(RepairItem correctData) throws SQLException {
        System.out.println(correctData);
        return Cruddutil.execute("update repair_item set Cust_Id = ? ,Item_Id = ?,Price = ? ,Contact = ?,Description = ? where Rep_Id = ?"
                ,correctData.getCust_id(),correctData.getItem_id(),correctData.getPayment(),correctData.getContact(),correctData.getDiscription(),correctData.getId());
    }
}
