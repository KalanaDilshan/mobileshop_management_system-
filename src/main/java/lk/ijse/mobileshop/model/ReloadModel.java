package lk.ijse.mobileshop.model;

import lk.ijse.mobileshop.Util.Cruddutil;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.dto.Reload;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ReloadModel {
    private final static String URL = "jdbc:mysql://localhost:3306/mobileshop";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "12345");
    }

    public static boolean saveReload(Reload reload) throws SQLException {
        String sql = "INSERT INTO reload(Reload_Id,Emp_id,Amount) VALUES(?, ?, ?)";

        return Cruddutil.execute(sql, reload.getId(), reload.getEmpid(), reload.getAmount());
    }

    public static String generateNextReload_Id() throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();

        String sql = "SELECT Reload_Id FROM reload ORDER BY Reload_Id DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return splitReload_Id(resultSet.getString(1));
        }
        return splitReload_Id(null);
    }

    public static String splitReload_Id(String splitReload_Id) {
        if ( splitReload_Id!= null) {
            String[] strings = splitReload_Id.split("Re-0");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "Re-00" + id;
        }
        return "Re-001";
    }

    public static ResultSet getAll() {
        ResultSet resultSet = null;
        try {
            resultSet=Cruddutil.execute("SELECT * FROM reload ");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
    public static Reload search(String Id) throws SQLException {
        ResultSet rs = Cruddutil.execute("SELECT * FROM reload WHERE Reload_Id = ?",Id);
        if(rs.next())return new Reload(rs.getString(1)
                ,rs.getString(2),rs.getDouble(3));
        return null;
    }
    public static boolean delete(String id) throws SQLException {
        return Cruddutil.execute("delete from reload where Reload_Id = ?",id);
    }
    public static boolean update(Reload correctData) throws SQLException {
        System.out.println(correctData);
        return Cruddutil.execute("update reload set Emp_Id = ? ,Amount = ? where Reload_Id = ?"
                ,correctData.getEmpid(),correctData.getAmount(),correctData.getId());
    }

}

